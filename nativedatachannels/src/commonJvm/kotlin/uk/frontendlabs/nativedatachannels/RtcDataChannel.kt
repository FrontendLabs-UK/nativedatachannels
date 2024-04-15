package uk.frontendlabs.nativedatachannels

import com.sun.jna.Pointer
import kotlin.math.absoluteValue

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
    private val openCallback = object: LibDatachannels.rtcOpenCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onOpen(this@RtcDataChannel)
        }
    }

    private val closedCallback = object: LibDatachannels.rtcClosedCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onClosed(this@RtcDataChannel)
        }
    }

    private val errorCallback = object: LibDatachannels.rtcErrorCallbackFunc {
        override fun invoke(id: Int, error: String, pointer: Pointer?) {
            handler.onError(this@RtcDataChannel, error)
        }
    }

    private val messageCallback = object: LibDatachannels.rtcMessageCallbackFunc {
        override fun invoke(id: Int, message: Pointer, size: Int, pointer: Pointer?) {
            handler.onMessage(this@RtcDataChannel, message.getByteArray(0, size.absoluteValue))
        }
    }

    private val bufferedAmountLowCallback = object: LibDatachannels.rtcBufferedAmountLowCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onBufferedAmountLow(this@RtcDataChannel)
        }
    }

    private val availableCallback = object: LibDatachannels.rtcAvailableCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onAvailable(this@RtcDataChannel)
        }
    }

    init {
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
        delete(handle)
    }

    val stream: Int
        get() = native.rtcGetDataChannelStream(handle)

    val label: String?
        get() {
            val labelBytes = ByteArray(256)
            val result = native.rtcGetDataChannelLabel(handle, labelBytes, labelBytes.size)
            if (result < 0) {
                return null
            }

            return String(labelBytes, 0, result)
        }

    val protocol: String?
        get() {
            val protocolBytes = ByteArray(256)
            val result = native.rtcGetDataChannelProtocol(handle, protocolBytes, protocolBytes.size)
            if (result < 0) {
                return null
            }

            return String(protocolBytes, 0, result)
        }

    val reliability: LibDatachannels.rtcReliability
        get() {
            val reliability = LibDatachannels.rtcReliability()
            native.rtcGetDataChannelReliability(handle, reliability)
            return reliability
        }

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