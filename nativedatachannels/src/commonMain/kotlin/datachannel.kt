
data class Reliability(
    var unordered: Boolean,
    var unreliable: Boolean,
    var maxPacketLifetime: Int,
    var maxRetransmits: Int
)

data class DataChannelInit(
    var reliability: Reliability,
    var protocol: String,
    var negotiated: Boolean,
    var manualStream: Boolean,
    var stream: UShort
)

interface DataChannelHandler {
    fun onOpen()
    fun onClosed()
    fun onError(error: String)
    fun onMessage(message: ByteArray)
    fun onBufferedAmountLow()
    fun onAvailable()
}

data class RtcDataChannel(
    val id: Int,
    val handler: DataChannelHandler
) {
    
}
