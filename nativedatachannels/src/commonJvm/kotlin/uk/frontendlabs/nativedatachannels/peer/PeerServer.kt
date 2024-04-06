package uk.frontendlabs.nativedatachannels.peer

import uk.frontendlabs.nativedatachannels.DataHandler
import uk.frontendlabs.nativedatachannels.RtcCommonClient
import uk.frontendlabs.nativedatachannels.RtcWebSocket
import uk.frontendlabs.nativedatachannels.RtcWebSocketServer
import uk.frontendlabs.nativedatachannels.WordGenerator

private data class PeerInfo(
    val first: RtcWebSocket,
    val second: RtcWebSocket? = null
)

class PeerServer(port: Short): DataHandler {
    private val waitingSockets = mutableMapOf<String, PeerInfo>()
    private val wordGenerator = WordGenerator()
    private val webSocketServer = RtcWebSocketServer(
        clientHandler = this,
        port = port,
    ) {
        println("WebSocket connection established")
    }

    override fun onMessage(client: RtcCommonClient, message: ByteArray) {
        println("[PeerServer] onMessage")
        val messageStr = String(message)
        when (val message = PeerMessage.fromJson(messageStr)) {
            is CreateGroup -> {
                val phrase = wordGenerator.newWord(6)
                val groupInfo = GroupInfo(phrase)
                client.sendMessage(groupInfo.toJson().toByteArray()).onSuccess {
                    println("Sent group info to client")
                    waitingSockets[phrase] = PeerInfo(client as RtcWebSocket)
                }
            }
            is JoinGroup -> {
                val group = message.group
                val peer = waitingSockets[group]
                peer?.let {
                    if (it.first == client) return

                    if (it.second != null) {
                        // Close this connection, someone else wants to join the group
                        it.second.close()
                        it.second.free()
                    }

                    waitingSockets[group] = it.copy(second = client as RtcWebSocket)
                    it.first.sendMessage(message.toJson().toByteArray())
                }
            }
            is AnswerGroup -> {
                val group = message.group
                val peer = waitingSockets[group]
                peer?.let {
                    println("XXX: [PeerServer] AnswerGroup: Group found")
                    it.second?.sendMessage(message.toJson().toByteArray())
                } ?: run {
                    println("XXX: [PeerServer] AnswerGroup: Group not found")
                }
            }
            is LocalCandidate -> {
                val group = message.group
                val peer = waitingSockets[group]
                peer?.let {
                    if (message.isFor == PeerRole.CREATOR.name) {
                        it.first.sendMessage(message.toJson().toByteArray())
                    } else {
                        it.second?.sendMessage(message.toJson().toByteArray())
                    }
                }
            }
            else -> {}
        }
    }

    override fun onOpen(client: RtcCommonClient) {
        println("[PeerServer] onOpen")
    }

    override fun onClosed(client: RtcCommonClient) {
        println("[PeerServer] onClosed")
    }
}