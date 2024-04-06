package uk.frontendlabs.nativedatachannels

internal interface RtcCommon {
    fun setOpenCallback(handle: Int, callback: () -> Unit) {
        Rtc.setOpenCallback(handle, callback)
    }
    fun setClosedCallback(handle: Int, callback: () -> Unit) {
        Rtc.setClosedCallback(handle, callback)
    }
    fun setErrorCallback(handle: Int, callback: (String) -> Unit) {
        Rtc.setErrorCallback(handle, callback)
    }
    fun setMessageCallback(handle: Int, callback: (ByteArray) -> Unit) {
        Rtc.setMessageCallback(handle, callback)
    }
    fun setBufferedAmountLowCallback(handle: Int, callback: () -> Unit) {
        Rtc.setBufferedAmountLowCallback(handle, callback)
    }
    fun setAvailableCallback(handle: Int, callback: () -> Unit) {
        Rtc.setAvailableCallback(handle, callback)
    }
    fun sendMessage(handle: Int, message: ByteArray): Int {
        return Rtc.sendMessage(handle, message)
    }
    fun close(handle: Int): Int {
        return Rtc.close(handle)
    }
    fun delete(handle: Int) {
        Rtc.free(handle)
    }
    fun isOpen(handle: Int): Boolean {
        return Rtc.isOpen(handle)
    }
    fun isClosed(handle: Int): Boolean {
        return Rtc.isClosed(handle)
    }
    fun getMaxMessageSize(handle: Int): Int {
        return Rtc.getMaxMessageSize(handle)
    }
    fun getBufferedAmount(handle: Int): Int {
        return Rtc.getBufferedAmount(handle)
    }
    fun setBufferedAmountLowThreshold(handle: Int, amount: Int): Int {
        return Rtc.setBufferedAmountLowThreshold(handle, amount)
    }
}

interface RtcCommonClient {
    fun sendMessage(message: ByteArray): Result<Unit>
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