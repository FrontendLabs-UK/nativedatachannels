package uk.frontendlabs.nativedatachannels

import com.sun.jna.Pointer
import kotlin.math.absoluteValue

enum class RtcTrackDirection(val value: Int) {
    UNKNOWN(0),
    SEND_ONLY(1),
    RECV_ONLY(2),
    SEND_RECV(3),
    INACTIVE(4);

    companion object {
        fun fromValue(value: Int): RtcTrackDirection {
            return entries.firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}

class RtcTrack(
    private val handle: Int,
    private val handler: DataHandler
): RtcCommon, RtcCommonClient {

    private val openCallback = object: LibDatachannels.rtcOpenCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onOpen(this@RtcTrack)
        }
    }

    private val closedCallback = object: LibDatachannels.rtcClosedCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onClosed(this@RtcTrack)
        }
    }

    private val errorCallback = object: LibDatachannels.rtcErrorCallbackFunc {
        override fun invoke(id: Int, error: String, pointer: Pointer?) {
            handler.onError(this@RtcTrack, error)
        }
    }

    private val messageCallback = object: LibDatachannels.rtcMessageCallbackFunc {
        override fun invoke(id: Int, message: Pointer, size: Int, pointer: Pointer?) {
            handler.onMessage(this@RtcTrack, message.getByteArray(0, size.absoluteValue))
        }
    }

    private val bufferedAmountLowCallback = object: LibDatachannels.rtcBufferedAmountLowCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onBufferedAmountLow(this@RtcTrack)
        }
    }

    private val availableCallback = object: LibDatachannels.rtcAvailableCallbackFunc {
        override fun invoke(id: Int, pointer: Pointer?) {
            handler.onAvailable(this@RtcTrack)
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

    val description: String?
        get() {
            val buffer = ByteArray(256)
            val result = native.rtcGetTrackDescription(handle, buffer, buffer.size)
            return if (result > 0) {
                String(buffer, 0, result)
            } else {
                null
            }
        }

    val mid: String?
        get() {
            val buffer = ByteArray(256)
            val result = native.rtcGetTrackMid(handle, buffer, buffer.size)
            return if (result > 0) {
                String(buffer, 0, result)
            } else {
                null
            }
        }

    val direction: RtcTrackDirection
        get() {
            val directionArray = IntArray(1)
            native.rtcGetTrackDirection(handle, directionArray)
            return RtcTrackDirection.fromValue(directionArray[0])
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RtcTrack

        return handle == other.handle
    }

    override fun hashCode(): Int {
        return handle
    }
}