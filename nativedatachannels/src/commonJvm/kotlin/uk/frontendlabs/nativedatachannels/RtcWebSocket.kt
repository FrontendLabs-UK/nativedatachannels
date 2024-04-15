package uk.frontendlabs.nativedatachannels

import com.sun.jna.Pointer
import kotlin.math.absoluteValue
import kotlin.properties.Delegates

class RtcWebSocket private constructor (
    private val handler: DataHandler
): RtcCommon, RtcCommonClient {

    private var handle by Delegates.notNull<Int>()

    private val openCallback = object: LibDatachannels.rtcOpenCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onOpen(this@RtcWebSocket)
        }
    }

    private val closedCallback = object: LibDatachannels.rtcClosedCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onClosed(this@RtcWebSocket)
        }
    }

    private val errorCallback = object: LibDatachannels.rtcErrorCallbackFunc {
        override fun invoke(id: Int, error: String, pointer: Pointer?) {
            handler.onError(this@RtcWebSocket, error)
        }
    }

    private val messageCallback = object: LibDatachannels.rtcMessageCallbackFunc {
        override fun invoke(id: Int, message: Pointer, size: Int, pointer: Pointer?) {
            handler.onMessage(this@RtcWebSocket, message.getByteArray(0, size.absoluteValue))
        }
    }

    private val bufferedAmountLowCallback = object: LibDatachannels.rtcBufferedAmountLowCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onBufferedAmountLow(this@RtcWebSocket)
        }
    }

    private val availableCallback = object: LibDatachannels.rtcAvailableCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onAvailable(this@RtcWebSocket)
        }
    }

    constructor(url: String, handler: DataHandler): this(handler) {
        handle = native.rtcCreateWebSocket(url)
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcWebSocket")
        }
        init()
    }

    constructor(handle: Int, handler: DataHandler): this(handler) {
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
    ): this(handler) {
        val webSocketConfig = LibDatachannels.rtcWsConfiguration()
        webSocketConfig.disableTlsVerification = disableTlsVerification
        webSocketConfig.protocols = protocols
        webSocketConfig.connectionTimeoutMs = connectionTimeoutMs
        webSocketConfig.pingIntervalMs = pingIntervalMs
        webSocketConfig.maxOutstandingPings = maxOutstandingPings
        handle = native.rtcCreateWebSocketEx(url, webSocketConfig)
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcWebSocket")
        }
        init()
    }

    private fun init() {
        setOpenCallback(handle, openCallback)
        setClosedCallback(handle, closedCallback)
        setErrorCallback(handle, errorCallback)
        setMessageCallback(handle, messageCallback)
        setBufferedAmountLowCallback(handle, bufferedAmountLowCallback)
        setAvailableCallback(handle, availableCallback)
    }

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

        other as RtcWebSocket

        return handle == other.handle
    }

    override fun hashCode(): Int {
        return handle
    }
}