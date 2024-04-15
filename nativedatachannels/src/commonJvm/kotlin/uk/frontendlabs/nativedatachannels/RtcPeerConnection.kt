package uk.frontendlabs.nativedatachannels

import com.sun.jna.Pointer

enum class RtcPeerConnectionState(val value: Int) {
    NEW(0),
    CONNECTING(1),
    CONNECTED(2),
    DISCONNECTED(3),
    FAILED(4),
    CLOSED(5)
}

enum class RtcGatheringState(val value: Int) {
    NEW(0),
    GATHERING(1),
    COMPLETE(2)
}

interface PeerConnectionHandler {
    fun onLocalDescription(pc: RtcPeerConnection, sdp: String, type: String) {}
    fun onLocalCandidate(pc: RtcPeerConnection, candidate: String, mid: String) {}
    fun onStateChange(pc: RtcPeerConnection, state: RtcPeerConnectionState) {}
    fun onGatheringStateChange(pc: RtcPeerConnection, gatheringState: RtcGatheringState) {}
    fun onDataChannel(pc: RtcPeerConnection, dataChannel: RtcDataChannel) {}
    fun onTrack(pc: RtcPeerConnection, track: RtcTrack) {}
    fun dataChannelHandler(): DataHandler
}

private const val fiveMegabytes = 1024 * 1024 * 5

class RtcPeerConnection(
    config: RtcConfiguration,
    private val handler: PeerConnectionHandler
) {
    private val native = LibDatachannels.INSTANCE

    private val handle: Int

    private val localDescriptionCallback = object: LibDatachannels.rtcDescriptionCallbackFunc {
        override fun invoke(pc: Int, sdp: String, type: String, pointer: Pointer?) {
            handler.onLocalDescription(this@RtcPeerConnection, sdp, type)
        }
    }

    private val localCandidateCallback = object: LibDatachannels.rtcCandidateCallbackFunc {
        override fun invoke(pc: Int, cand: String, mid: String, pointer: Pointer?) {
            handler.onLocalCandidate(this@RtcPeerConnection, cand, mid)
        }
    }

    private val stateChangeCallback = object: LibDatachannels.rtcStateChangeCallbackFunc {
        override fun invoke(pc: Int, rtcState: Int, pointer: Pointer?) {
            handler.onStateChange(this@RtcPeerConnection, RtcPeerConnectionState.entries.firstOrNull { it.value == rtcState } ?: RtcPeerConnectionState.NEW)
        }
    }

    private val gatheringStateChangeCallback = object: LibDatachannels.rtcGatheringStateCallbackFunc {
        override fun invoke(pc: Int, state: Int, pointer: Pointer?) {
            handler.onGatheringStateChange(this@RtcPeerConnection, RtcGatheringState.entries.firstOrNull { it.value == state } ?: RtcGatheringState.NEW)
        }
    }

    private val dataChannelCallback = object: LibDatachannels.rtcDataChannelCallbackFunc {
        override fun invoke(pc: Int, dataChannel: Int, pointer: Pointer?) {
            handler.onDataChannel(this@RtcPeerConnection, RtcDataChannel(handler.dataChannelHandler(), dataChannel))
        }
    }

    private val trackCallback = object: LibDatachannels.rtcTrackCallbackFunc {
        override fun invoke(pc: Int, track: Int, pointer: Pointer?) {
            handler.onTrack(this@RtcPeerConnection, RtcTrack(track, handler.dataChannelHandler()))
        }
    }

    init {
        handle = native.rtcCreatePeerConnection(config)
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcPeerConnection")
        }

        initializeCallbacks()
    }

    private fun initializeCallbacks() {
        native.rtcSetLocalDescriptionCallback(handle, localDescriptionCallback)
        native.rtcSetLocalCandidateCallback(handle, localCandidateCallback)
        native.rtcSetStateChangeCallback(handle, stateChangeCallback)
        native.rtcSetGatheringStateChangeCallback(handle, gatheringStateChangeCallback)
        native.rtcSetDataChannelCallback(handle, dataChannelCallback)
        native.rtcSetTrackCallback(handle, trackCallback)
    }

    fun close() {
        native.rtcClose(handle)
    }

    fun free() {
        native.rtcDelete(handle)
    }

    fun setLocalDescription(type: String?): Int {
        return native.rtcSetLocalDescription(handle, type)
    }

    fun setRemoteDescription(sdp: String, type: String): Int {
        return native.rtcSetRemoteDescription(handle, sdp, type)
    }

    fun addRemoteCandidate(candidate: String, mid: String): Int {
        return native.rtcAddRemoteCandidate(handle, candidate, mid)
    }

    val localDescription: String?
        get() {
            val buffer = ByteArray(fiveMegabytes)
            val count = native.rtcGetLocalDescription(handle, buffer, buffer.size)
            return if (count > 0) {
                String(buffer, 0, count - 1)
            } else {
                null
            }
        }

    val remoteDescription: String?
        get() {
            val buffer = ByteArray(fiveMegabytes)
            val count = native.rtcGetRemoteDescription(handle, buffer, buffer.size)
            return if (count > 0) {
                String(buffer, 0, count)
            } else {
                null
            }
        }

    val localDescriptionType: String?
        get() {
            val buffer = ByteArray(fiveMegabytes)
            val count = native.rtcGetLocalDescriptionType(handle, buffer, buffer.size)
            return if (count > 0) {
                String(buffer, 0, count)
            } else {
                null
            }
        }

    val remoteDescriptionType: String?
        get() {
            val buffer = ByteArray(fiveMegabytes)
            val count = native.rtcGetRemoteDescriptionType(handle, buffer, buffer.size)
            return if (count > 0) {
                String(buffer, 0, count)
            } else {
                null
            }
        }

    val localAddress: String?
        get() {
            val buffer = ByteArray(fiveMegabytes)
            val count = native.rtcGetLocalAddress(handle, buffer, buffer.size)
            return if (count > 0) {
                String(buffer, 0, count)
            } else {
                null
            }
        }

    val remoteAddress: String?
        get() {
            val buffer = ByteArray(fiveMegabytes)
            val count = native.rtcGetRemoteAddress(handle, buffer, buffer.size)
            return if (count > 0) {
                String(buffer, 0, count)
            } else {
                null
            }
        }

    // TODO: A potential bug here!!!
    val selectedCandidatePair: Pair<String?, String?>?
        get() {
            val localBuffer = ByteArray(fiveMegabytes)
            val remoteBuffer = ByteArray(fiveMegabytes)
            val count = native.rtcGetSelectedCandidatePair(handle, localBuffer, localBuffer.size, remoteBuffer, remoteBuffer.size)
            return if (count > 0) {
                Pair(String(localBuffer, 0, count), String(remoteBuffer, 0, count))
            } else {
                null
            }
        }

    val maximumDataChannelStream: Int
        get() = native.rtcGetMaxDataChannelStream(handle)

    val remoteMaxMessageSize: Int
        get() = native.rtcGetRemoteMaxMessageSize(handle)

    fun createDataChannel(label: String): Result<RtcDataChannel> {
        val dataChannelHandle = native.rtcCreateDataChannel(handle, label)
        if (dataChannelHandle < 0) {
            return Result.failure(RuntimeException("Failed to create data channel"))
        }
        return Result.success(RtcDataChannel(handler.dataChannelHandler(), dataChannelHandle))
    }

    fun createDataChannel(label: String, init: LibDatachannels.rtcDataChannelInit): Result<RtcDataChannel> {
        val dataChannelHandle = native.rtcCreateDataChannelEx(handle, label, init)
        if (dataChannelHandle < 0) {
            return Result.failure(RuntimeException("Failed to create data channel"))
        }
        return Result.success(RtcDataChannel(handler.dataChannelHandler(), dataChannelHandle))
    }

    fun addTrack(mediaDescriptionSdp: String): Int {
        return native.rtcAddTrack(handle, mediaDescriptionSdp)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RtcPeerConnection

        return handle == other.handle
    }

    override fun hashCode(): Int {
        return handle
    }
}
