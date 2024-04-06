package uk.frontendlabs.nativedatachannels

class RtcWebSocket: RtcCommon, RtcCommonClient {

    private var handle: Int
    var handler: DataHandler

    constructor(url: String, handler: DataHandler) {
        this.handler = handler
        nativeCreate(url).also { handle = it }
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcWebSocket")
        }
        init()
    }

    constructor(handle: Int, handler: DataHandler) {
        this.handler = handler
        this.handle = handle
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcWebSocket")
        }
        init()
    }

    constructor(
        handler: DataHandler,
        url: String,
        disableTlsVerification: Boolean,
        protocols: Array<String>,
        connectionTimeoutMs: Int,
        pingIntervalMs: Int,
        maxOutstandingPings: Int,
    ) {
        this.handler = handler
        nativeCreateEx(url, disableTlsVerification, protocols, connectionTimeoutMs, pingIntervalMs, maxOutstandingPings).also { handle = it }
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcWebSocket")
        }
        init()
    }

    private fun init() {
        setOpenCallback(handle) { handler.onOpen(this) }
        setClosedCallback(handle) { handler.onClosed(this) }
        setErrorCallback(handle) { handler.onError(this, it) }
        setMessageCallback(handle) { handler.onMessage(this, it) }
        setBufferedAmountLowCallback(handle) { handler.onBufferedAmountLow(this) }
        setAvailableCallback(handle) { handler.onAvailable(this) }
    }

    private external fun nativeCreate(url: String): Int
    private external fun nativeCreateEx(
        url: String,
        disableTlsVerification: Boolean,
        protocols: Array<String>,
        connectionTimeoutMs: Int,
        pingIntervalMs: Int,
        maxOutstandingPings: Int,
    ): Int
    private external fun nativeDelete(handle: Int)
    private external fun nativeGetRemoteAddress(handle: Int): String?
    private external fun nativeGetPath(handle: Int): String?

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
        nativeDelete(handle)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RtcWebSocket

        return handle == other.handle
    }

    override fun hashCode(): Int {
        return handle
    }
}