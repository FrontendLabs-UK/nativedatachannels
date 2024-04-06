package uk.frontendlabs.nativedatachannels

internal class Reliability {
    internal var handle: Long

    constructor() {
        nativeCreate().also { handle = it }
        if (handle == -1L) {
            throw RuntimeException("Failed to create native Reliability")
        }
    }

    internal constructor(handle: Long) {
        this.handle = handle
    }

    var unordered: Boolean
        set(value) = unordered(handle, value)
        get() = getUnordered(handle)

    var unreliable: Boolean
        set(value) = unreliable(handle, value)
        get() = getUnreliable(handle)

    var maxPacketLifeTime: Int
        set(value) = maxPacketLifeTime(handle, value)
        get() = getMaxPacketLifeTime(handle)

    var maxRetransmits: Int
        set(value) = maxRetransmits(handle, value)
        get() = getMaxRetransmits(handle)

    private external fun nativeCreate(): Long
    private external fun nativeDelete(handle: Long)

    private external fun unordered(handle: Long, value: Boolean)
    private external fun getUnordered(handle: Long): Boolean

    private external fun unreliable(handle: Long, value: Boolean)
    private external fun getUnreliable(handle: Long): Boolean

    private external fun maxPacketLifeTime(handle: Long, value: Int)
    private external fun getMaxPacketLifeTime(handle: Long): Int

    private external fun maxRetransmits(handle: Long, value: Int)
    private external fun getMaxRetransmits(handle: Long): Int

    fun free() {
        nativeDelete(handle)
    }
}

data class RtcReliability(
    val unordered: Boolean,
    val unreliable: Boolean,
    val maxPacketLifeTime: Int,
    val maxRetransmits: Int
) {
    internal constructor(native: Reliability): this(
        native.unordered, native.unreliable, native.maxPacketLifeTime, native.maxRetransmits
    )

    internal fun toNative(): Reliability {
        return Reliability().apply {
            unordered = unordered
            unreliable = unreliable
            maxPacketLifeTime = maxPacketLifeTime
            maxRetransmits = maxRetransmits
        }
    }
}

data class RtcDataChannelInit(
    var reliability: RtcReliability,
    var protocol: String,
    var negotiated: Boolean,
    var manualStream: Boolean,
    var stream: Short
) {
    internal constructor(native: DataChannelInit): this(
        native.reliability, native.protocol, native.negotiated, native.manualStream, native.stream
    )

    internal fun toNative(): DataChannelInit {
        return DataChannelInit().apply {
            reliability = reliability
            protocol = protocol
            negotiated = negotiated
            manualStream = manualStream
            stream = stream
        }
    }
}

internal class DataChannelInit {
    internal val handle: Long

    init {
        nativeCreate().also { handle = it }
        if (handle == -1L) {
            throw RuntimeException("Failed to create native DataChannelInit")
        }
    }

    var reliability: RtcReliability
        set(value) {
            val tmp = value.toNative()
            setReliability(handle, tmp.handle)
            tmp.free()
        }
        get() {
            val tmp = Reliability(getReliability(handle))
            val reliability = RtcReliability(tmp)
            tmp.free()
            return reliability
        }

    var protocol: String
        set(value) = setProtocol(handle, value)
        get() = getProtocol(handle)

    var negotiated: Boolean
        set(value) = setNegotiated(handle, value)
        get() = getNegotiated(handle)

    var manualStream: Boolean
        set(value) = setManualStream(handle, value)
        get() = getManualStream(handle)

    var stream: Short
        set(value) = setStream(handle, value)
        get() = getStream(handle)

    fun free() {
        nativeDelete(handle)
    }

    private external fun nativeCreate(): Long
    private external fun nativeDelete(handle: Long)

    private external fun setReliability(handle: Long, reliabilityHandle: Long)
    private external fun getReliability(handle: Long): Long

    private external fun setProtocol(handle: Long, value: String)
    private external fun getProtocol(handle: Long): String

    private external fun setNegotiated(handle: Long, value: Boolean)
    private external fun getNegotiated(handle: Long): Boolean

    private external fun setManualStream(handle: Long, value: Boolean)
    private external fun getManualStream(handle: Long): Boolean

    private external fun setStream(handle: Long, value: Short)
    private external fun getStream(handle: Long): Short
}

interface DataHandler {
    fun onOpen(client: RtcCommonClient) {}
    fun onClosed(client: RtcCommonClient) { client.free() }
    fun onError(client: RtcCommonClient, error: String) {}
    fun onMessage(client: RtcCommonClient, message: ByteArray) {}
    fun onBufferedAmountLow(client: RtcCommonClient) {}
    fun onAvailable(client: RtcCommonClient) {}
}

class RtcDataChannel(
    private val handler: DataHandler,
    private val handle: Int
): RtcCommon, RtcCommonClient {
    init {
        setOpenCallback(handle) { handler.onOpen(this) }
        setClosedCallback(handle) { handler.onClosed(this) }
        setErrorCallback(handle) { handler.onError(this, it) }
        setMessageCallback(handle) { handler.onMessage(this, it) }
        setBufferedAmountLowCallback(handle) { handler.onBufferedAmountLow(this) }
        setAvailableCallback(handle) { handler.onAvailable(this) }
    }

    override fun sendMessage(message: ByteArray): Result<Unit> {
        val result = sendMessage(handle, message)
        return resultFromCode(result)
    }

    override fun close(): Result<Unit> {
        return resultFromCode(close(handle))
    }

    override fun isOpen(): Result<Boolean> {
        return Result.success(isOpen(handle))
    }

    override fun isClosed(): Result<Boolean> {
        return Result.success(isClosed(handle))
    }

    override fun getMaxMessageSize(): Result<Int> {
        return Result.success(getMaxMessageSize(handle))
    }

    override fun getBufferedAmount(): Result<Int> {
        return Result.success(getBufferedAmount(handle))
    }

    override fun setBufferedAmountLowThreshold(amount: Int): Result<Int> {
        return Result.success(setBufferedAmountLowThreshold(handle, amount))
    }

    override fun free() {
        delete(handle)
    }

    val stream: Int
        get() = getStream(handle)

    val label: String?
        get() = getLabel(handle)

    val protocol: String?
        get() = getProtocol(handle)

    val reliability: RtcReliability
        get() {
            val native = Reliability(getReliability(handle))
            val reliability = RtcReliability(native)
            native.free()
            return reliability
        }

    private external fun getStream(handle: Int): Int
    private external fun getLabel(handle: Int): String?
    private external fun getProtocol(handle: Int): String?
    private external fun getReliability(handle: Int): Long
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RtcDataChannel

        return handle == other.handle
    }

    override fun hashCode(): Int {
        return handle
    }


}