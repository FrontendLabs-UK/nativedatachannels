package uk.frontendlabs.nativedatachannels

import com.sun.jna.Pointer
import java.awt.Point

internal interface RtcCommon {
    val native: LibDatachannels get() = LibDatachannels.INSTANCE

    fun setOpenCallback(handle: Int, callback: LibDatachannels.rtcOpenCallbackFunc) {
        native.rtcSetOpenCallback(handle, callback)
    }
    fun setClosedCallback(handle: Int, callback: LibDatachannels.rtcClosedCallbackFunc) {
        native.rtcSetClosedCallback(handle, callback)
    }
    fun setErrorCallback(handle: Int, callback: LibDatachannels.rtcErrorCallbackFunc) {
        native.rtcSetErrorCallback(handle, callback)
    }
    fun setMessageCallback(handle: Int, callback: LibDatachannels.rtcMessageCallbackFunc) {
        native.rtcSetMessageCallback(handle, callback)
    }
    fun setBufferedAmountLowCallback(handle: Int, callback: LibDatachannels.rtcBufferedAmountLowCallbackFunc) {
        native.rtcSetBufferedAmountLowCallback(handle, callback)
    }
    fun setAvailableCallback(handle: Int, callback: LibDatachannels.rtcAvailableCallbackFunc) {
        native.rtcSetAvailableCallback(handle, callback)
    }
    fun sendMessage(handle: Int, message: Pointer, size: Int): Int {
        return native.rtcSendMessage(handle, message, size)
    }
    fun close(handle: Int): Int {
        return native.rtcClose(handle)
    }
    fun delete(handle: Int) {
        native.rtcDelete(handle)
    }
    fun isOpen(handle: Int): Boolean {
        return native.rtcIsOpen(handle)
    }
    fun isClosed(handle: Int): Boolean {
        return native.rtcIsClosed(handle)
    }
    fun getMaxMessageSize(handle: Int): Int {
        return native.rtcGetRemoteMaxMessageSize(handle)
    }
    fun getBufferedAmount(handle: Int): Int {
        return native.rtcGetBufferedAmount(handle)
    }
    fun setBufferedAmountLowThreshold(handle: Int, amount: Int): Int {
        return native.rtcSetBufferedAmountLowThreshold(handle, amount)
    }
}

interface RtcCommonClient {
    fun sendMessage(message: Pointer, size: Int): Result<Unit>
    fun close(): Result<Unit>
    fun isOpen(): Result<Boolean>
    fun isClosed(): Result<Boolean>
    fun getMaxMessageSize(): Result<Int>
    fun getBufferedAmount(): Result<Int>
    fun setBufferedAmountLowThreshold(amount: Int): Result<Int>
    fun free()
}

internal fun resultFromCode(value: Int): Result<Unit> {
    return if (value == 0) {
        Result.success(Unit)
    } else {
        Result.failure(RuntimeException("Failed with code $value"))
    }
}