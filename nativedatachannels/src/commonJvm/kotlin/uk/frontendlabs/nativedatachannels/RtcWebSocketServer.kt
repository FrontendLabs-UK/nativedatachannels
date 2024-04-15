package uk.frontendlabs.nativedatachannels

import com.sun.jna.Pointer

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

    private val webSocketCallback = object: LibDatachannels.rtcWebSocketClientCallbackFunc {
        override fun invoke(wsserver: Int, ws: Int, pointer: Pointer?) {
            onWebSocketConnection(RtcWebSocket(ws, clientHandler))
        }
    }

    init {
        val config = LibDatachannels.rtcWsServer()
        config.port = port
        config.enableTls = enableTls
        config.certificatePemFile = certificatePemFile
        config.keyPemFile = keyPemFile
        config.keyPemPass = keyPemPass
        config.connectionTimeoutMs = connectionTimeoutMs

        native.rtcCreateWebSocketServer(config, webSocketCallback).also { handle = it }
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcWebSocket")
        }
        println("WSServer Handle is: $handle")
    }

    fun getPort() = native.rtcGetWebSocketServerPort(handle)

    override fun sendMessage(message: Pointer, size: Int): Result<Unit> {
        val result = sendMessage(handle, message, size)
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
        native.rtcDelete(handle)
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