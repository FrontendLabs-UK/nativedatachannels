package uk.frontendlabs.nativedatachannels.peer

import uk.frontendlabs.nativedatachannels.DataHandler
import uk.frontendlabs.nativedatachannels.PeerConnectionHandler
import uk.frontendlabs.nativedatachannels.RtcCommonClient
import uk.frontendlabs.nativedatachannels.RtcConfiguration
import uk.frontendlabs.nativedatachannels.RtcDataChannel
import uk.frontendlabs.nativedatachannels.RtcGatheringState
import uk.frontendlabs.nativedatachannels.RtcPeerConnection
import uk.frontendlabs.nativedatachannels.RtcPeerConnectionState
import uk.frontendlabs.nativedatachannels.RtcTrack
import uk.frontendlabs.nativedatachannels.RtcWebSocket
import kotlinx.coroutines.flow.MutableStateFlow

enum class PeerRole {
    CREATOR,
    JOINER
}

sealed class PeerState {
    data object Idle: PeerState()
    data object OfferPending: PeerState()
    data object WaitingForAnswer: PeerState()
    data object WaitingForOffer: PeerState()
    data object AnswerPending: PeerState()
    data object AnswerSent: PeerState()
    data object Connected: PeerState()
}

class Peer: DataHandler, PeerConnectionHandler {

    private val webSocketHost: String
    private val webSocketPort: Short
    private val peerHandler: PeerConnectionHandler
    val role: PeerRole
    private var group: String? = null
    private lateinit var webSocket: RtcWebSocket
    private val state = MutableStateFlow<PeerState>(PeerState.Idle)

    val phrase: MutableStateFlow<String> = MutableStateFlow("")

    private val peerConnection = RtcPeerConnection(
        config = RtcConfiguration(),
        handler = this
    )

    private constructor(
        webSocketHost: String,
        webSocketPort: Short,
        peerHandler: PeerConnectionHandler,
        role: PeerRole
    ) {
        this.webSocketHost = webSocketHost
        this.webSocketPort = webSocketPort
        this.peerHandler = peerHandler
        this.role = role
        initWebSocket()
    }

    constructor(
        webSocketHost: String,
        webSocketPort: Short,
        peerHandler: PeerConnectionHandler,
    ): this(webSocketHost, webSocketPort, peerHandler, PeerRole.CREATOR)

    constructor(
        group: String,
        webSocketHost: String,
        webSocketPort: Short,
        peerHandler: PeerConnectionHandler
    ) : this(webSocketHost, webSocketPort, peerHandler, PeerRole.JOINER) {
        this.group = group
    }

    private fun initWebSocket() {
        state.value = when (role) {
            PeerRole.CREATOR -> {
                peerConnection.setLocalDescription(null)
                PeerState.WaitingForOffer
            }
            PeerRole.JOINER -> PeerState.OfferPending
        }
        webSocket = RtcWebSocket(
            url = "$webSocketHost:$webSocketPort",
            handler = this
        )
    }

    override fun onAvailable(client: RtcCommonClient) {}

    override fun onOpen(client: RtcCommonClient) {
        // Connection is open, send that message
        when (role) {
            PeerRole.CREATOR -> createGroup()
            PeerRole.JOINER -> joinGroup()
        }
    }

    private fun createGroup() {
        val message = CreateGroup
        webSocket.sendMessage(CreateGroup.toJson().toByteArray())
    }

    private fun joinGroup() {
        if (role == PeerRole.JOINER) {
            state.value = PeerState.OfferPending
            peerConnection.createDataChannel("peer")
        }
    }

    override fun onClosed(client: RtcCommonClient) {
        super.onClosed(client)
        println("XXX: [Peer] Closed")
    }

    override fun onMessage(client: RtcCommonClient, message: ByteArray) {
        val msg = String(message)
        when (val messg = PeerMessage.fromJson(msg)) {
            is GroupInfo -> {
                phrase.value = messg.phrase
            }
            is JoinGroup -> {
                if (role == PeerRole.CREATOR) {
                    state.value = PeerState.AnswerPending
                    peerConnection.setRemoteDescription(messg.sdp, messg.type)
                }
            }
            is AnswerGroup -> {
                if (role == PeerRole.JOINER && state.value == PeerState.WaitingForAnswer) {
                    peerConnection.setRemoteDescription(messg.sdp, messg.type)
                }
            }
            is LocalCandidate -> {
                peerConnection.addRemoteCandidate(messg.candidate, messg.mid)
            }
            else -> {}
        }
    }

    override fun onError(client: RtcCommonClient, error: String) {}

    override fun onLocalDescription(pc: RtcPeerConnection, sdp: String, type: String) {
        peerHandler.onLocalDescription(pc, sdp, type)
    }

    override fun onLocalCandidate(pc: RtcPeerConnection, candidate: String, mid: String) {
        peerHandler.onLocalCandidate(pc, candidate, mid)
    }

    override fun onStateChange(pc: RtcPeerConnection, state: RtcPeerConnectionState) {
        when (state) {
            RtcPeerConnectionState.NEW -> {
                println("XXX: [Peer] Connection new")
            }
            RtcPeerConnectionState.CONNECTING -> {
                println("XXX: [Peer] Connection connecting")
            }
            RtcPeerConnectionState.CONNECTED -> {
                println("XXX: [Peer] Connection connected")
                this.state.value = PeerState.Connected
            }
            RtcPeerConnectionState.DISCONNECTED -> {
                println("XXX: [Peer] Connection disconnected")
            }
            RtcPeerConnectionState.FAILED -> {
                println("XXX: [Peer] Connection failed")
            }
            RtcPeerConnectionState.CLOSED -> {
                println("XXX: [Peer] Connection closed")
            }
        }
        peerHandler.onStateChange(pc, state)
    }

    override fun onGatheringStateChange(pc: RtcPeerConnection, gatheringState: RtcGatheringState) {
        if (gatheringState != RtcGatheringState.COMPLETE) return
        if (role == PeerRole.JOINER && state.value == PeerState.OfferPending) {
            peerConnection.localDescription?.let {
                val message = JoinGroup(group!!, it, "offer")
                webSocket.sendMessage(message.toJson().toByteArray())
                state.value = PeerState.WaitingForAnswer
            }
        } else if (role == PeerRole.CREATOR && state.value == PeerState.AnswerPending) {
            peerConnection.localDescription?.let {
                val message = AnswerGroup(phrase.value, it, "answer")
                webSocket.sendMessage(message.toJson().toByteArray())
                state.value = PeerState.AnswerSent
            }
        }
        peerHandler.onGatheringStateChange(pc, gatheringState)
    }

    override fun onDataChannel(pc: RtcPeerConnection, dataChannel: RtcDataChannel) {
        peerHandler.onDataChannel(pc, dataChannel)
    }

    override fun onTrack(pc: RtcPeerConnection, track: RtcTrack) {
        peerHandler.onTrack(pc, track)
    }

    override fun dataChannelHandler(): DataHandler {
        return peerHandler.dataChannelHandler()
    }
}