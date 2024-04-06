package uk.frontendlabs.nativedatachannels

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

    init {
        setOpenCallback(handle) { handler.onOpen(this) }
        setClosedCallback(handle) { handler.onClosed(this) }
        setErrorCallback(handle) { handler.onError(this, it) }
        setMessageCallback(handle) { handler.onMessage(this, it) }
        setBufferedAmountLowCallback(handle) { handler.onBufferedAmountLow(this) }
        setAvailableCallback(handle) { handler.onAvailable(this) }
    }

    val description: String?
        get() = getDescription(handle)

    val mid: String?
        get() = getMid(handle)

    val direction: RtcTrackDirection
        get() = RtcTrackDirection.fromValue(getDirection(handle))

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

    private external fun getDescription(handle: Int): String?
    private external fun getMid(handle: Int): String?
    private external fun getDirection(handle: Int): Int
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