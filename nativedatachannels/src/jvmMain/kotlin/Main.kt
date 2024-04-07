import kotlinx.coroutines.runBlocking
import uk.frontendlabs.nativedatachannels.DataHandler
import uk.frontendlabs.nativedatachannels.PeerConnectionHandler
import uk.frontendlabs.nativedatachannels.Rtc
import uk.frontendlabs.nativedatachannels.RtcCommonClient
import uk.frontendlabs.nativedatachannels.peer.Peer

fun main() {
    println("This is the [MobileApp]")
//    val peer = Peer(
//        webSocketHost = "ws://localhost",
//        webSocketPort = 8094,
//        peerHandler = object: PeerConnectionHandler {
//            override fun dataChannelHandler(): DataHandler {
//                return object: DataHandler {
//                    override fun onMessage(client: RtcCommonClient, message: ByteArray) {
//                        println(String(message))
//                    }
//
//                    override fun onOpen(client: RtcCommonClient) {
//                        super.onOpen(client)
//                        client.sendMessage("[MobileApp] Hello from the mobile app".toByteArray())
//                    }
//                }
//            }
//        }
//    )
//
//    runBlocking {
//        peer.phrase.collect {
//            println("Phrase: $it")
//        }
//    }
}