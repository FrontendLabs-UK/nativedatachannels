package uk.frontendlabs.nativedatachannels

class RtcWebSocketServer(
    private val clientHandler: DataHandler,
    port: Short = 0,
    enableTls: Boolean = false,
    certificatePemFile: String? = null,
    keyPemFile: String? = null,
    keyPemPass: String? = null,
    connectionTimeoutMs: Int = 0,
    val onWebSocketConnection: (socket: RtcWebSocket) -> Unit
) : RtcCommon, RtcCommonClient {
    private var handle: Int

    private fun webSocketCallback(socketHandle: Int) {
        onWebSocketConnection(RtcWebSocket(socketHandle, clientHandler))
    }

    init {
        nativeCreate(port, enableTls, certificatePemFile, keyPemFile, keyPemPass, connectionTimeoutMs, ::webSocketCallback).also { handle = it }
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcWebSocket")
        }
        println("WSServer Handle is: $handle")
    }

    private external fun nativeCreate(
        port: Short, enableTls: Boolean,
        certificatePemFile: String?, keyPemFile: String?,
        keyPemPass: String?, connectionTimeoutMs: Int,
        cb: (Int) -> Unit
    ): Int

    private external fun nativeDelete(handle: Int)

    private external fun nativeGetPort(handle: Int): Int

    fun getPort() = nativeGetPort(handle)

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

        other as RtcWebSocketServer

        return handle == other.handle
    }

    override fun hashCode(): Int {
        return handle
    }

}