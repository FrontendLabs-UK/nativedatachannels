package uk.frontendlabs.nativedatachannels

enum class RtcPeerConnectionState(val value: Int) {
    NEW(0),
    CONNECTING(1),
    CONNECTED(2),
    DISCONNECTED(3),
    FAILED(4),
    CLOSED(5)
}

enum class RtcGatheringState(val value: Int) {
    NEW(0),
    GATHERING(1),
    COMPLETE(2)
}

interface PeerConnectionHandler {
    fun onLocalDescription(pc: RtcPeerConnection, sdp: String, type: String) {}
    fun onLocalCandidate(pc: RtcPeerConnection, candidate: String, mid: String) {}
    fun onStateChange(pc: RtcPeerConnection, state: RtcPeerConnectionState) {}
    fun onGatheringStateChange(pc: RtcPeerConnection, gatheringState: RtcGatheringState) {}
    fun onDataChannel(pc: RtcPeerConnection, dataChannel: RtcDataChannel) {}
    fun onTrack(pc: RtcPeerConnection, track: RtcTrack) {}
    fun dataChannelHandler(): DataHandler
}

class RtcPeerConnection(
    config: RtcConfiguration,
    private val handler: PeerConnectionHandler
) {
    private val handle: Int

    private fun localDescriptionCallback(sdp: String, type: String) {
        handler.onLocalDescription(this, sdp, type)
    }

    private fun localCandidateCallback(candidate: String, mid: String) {
        handler.onLocalCandidate(this, candidate, mid)
    }

    private fun stateChangeCallback(state: Int) {
        handler.onStateChange(this, RtcPeerConnectionState.entries.firstOrNull { it.value == state } ?: RtcPeerConnectionState.NEW)
    }

    private fun gatheringStateChangeCallback(gatheringState: Int) {
        handler.onGatheringStateChange(this, RtcGatheringState.entries.firstOrNull { it.value == gatheringState } ?: RtcGatheringState.NEW)
    }

    private fun dataChannelCallback(dataChannelHandle: Int) {
        handler.onDataChannel(this, RtcDataChannel(handler.dataChannelHandler(), dataChannelHandle))
    }

    private fun trackCallback(trackHandle: Int) {
        handler.onTrack(this, RtcTrack(trackHandle, handler.dataChannelHandler()))
    }

    init {
        handle = nativeCreate(config.nativeHandle)
        if (handle == -1) {
            throw RuntimeException("Failed to create native RtcPeerConnection")
        }

        initializeCallbacks()
    }

    private fun initializeCallbacks() {
        nativeSetLocalDescriptionCallback(handle, ::localDescriptionCallback)
        nativeSetLocalCandidateCallback(handle, ::localCandidateCallback)
        nativeSetStateChangeCallback(handle, ::stateChangeCallback)
        nativeSetGatheringStateChangeCallback(handle, ::gatheringStateChangeCallback)
        nativeSetDataChannelCallback(handle, ::dataChannelCallback)
        nativeSetTrackCallback(handle, ::trackCallback)
    }

    fun close() {
        nativeClose(handle)
    }

    fun free() {
        nativeDelete(handle)
    }

    fun setLocalDescription(type: String?): Int {
        return nativeSetLocalDescription(handle, type)
    }

    fun setRemoteDescription(sdp: String, type: String): Int {
        return nativeSetRemoteDescription(handle, sdp, type)
    }

    fun addRemoteCandidate(candidate: String, mid: String): Int {
        return addRemoteCandidate(handle, candidate, mid)
    }

    val localDescription: String?
        get() = nativeGetLocalDescription(handle)

    val remoteDescription: String?
        get() = nativeGetRemoteDescription(handle)

    val localDescriptionType: String?
        get() = nativeGetLocalDescriptionType(handle)

    val remoteDescriptionType: String?
        get() = nativeGetRemoteDescriptionType(handle)

    val localAddress: String?
        get() = nativeGetLocalAddress(handle)

    val remoteAddress: String?
        get() = nativeGetRemoteAddress(handle)

    val selectedCandidatePair: Pair<String?, String?>?
        get() = nativeGetSelectedCandidatePair(handle)

    val maximumDataChannelStream: Long
        get() = nativeGetMaximumDataChannelStream(handle)

    val remoteMaxMessageSize: Int
        get() = nativeGetRemoteMaxMessageSize(handle)

    fun createDataChannel(label: String): Result<RtcDataChannel> {
        val dataChannelHandle = nativeCreateDataChannel(handle, label)
        if (dataChannelHandle < 0) {
            return Result.failure(RuntimeException("Failed to create data channel"))
        }
        return Result.success(RtcDataChannel(handler.dataChannelHandler(), dataChannelHandle))
    }

    fun createDataChannel(label: String, init: RtcDataChannelInit): Result<RtcDataChannel> {
        val native = init.toNative()
        val dataChannelHandle = nativeCreateDataChannelEx(handle, label, native.handle)
        if (dataChannelHandle < 0) {
            return Result.failure(RuntimeException("Failed to create data channel"))
        }
        native.free()
        return Result.success(RtcDataChannel(handler.dataChannelHandler(), dataChannelHandle))
    }

    fun addTrack(mediaDescriptionSdp: String): Int {
        return nativeAddTrack(handle, mediaDescriptionSdp)
    }

    private external fun nativeCreate(configHandle: Long): Int
    private external fun nativeDelete(handle: Int)
    private external fun nativeClose(handle: Int): Int
    private external fun nativeSetLocalDescriptionCallback(handle: Int, callback: (sdp: String, type: String) -> Unit): Int
    private external fun nativeSetLocalCandidateCallback(handle: Int, callback: (candidate: String, mid: String) -> Unit): Int
    private external fun nativeSetStateChangeCallback(handle: Int, callback: (state: Int) -> Unit): Int
    private external fun nativeSetGatheringStateChangeCallback(handle: Int, callback: (gatheringState: Int) -> Unit): Int
    private external fun nativeSetDataChannelCallback(handle: Int, callback: (dataChannel: Int) -> Unit): Int
    private external fun nativeSetTrackCallback(handle: Int, callback: (track: Int) -> Unit): Int

    private external fun nativeSetLocalDescription(handle: Int, type: String?): Int
    private external fun nativeSetRemoteDescription(handle: Int, sdp: String, type: String): Int
    private external fun addRemoteCandidate(handle: Int, candidate: String, mid: String): Int
    private external fun nativeGetLocalDescription(handle: Int): String?
    private external fun nativeGetRemoteDescription(handle: Int): String?
    private external fun nativeGetLocalDescriptionType(handle: Int): String?
    private external fun nativeGetRemoteDescriptionType(handle: Int): String?
    private external fun nativeGetLocalAddress(handle: Int): String?
    private external fun nativeGetRemoteAddress(handle: Int): String?
    private external fun nativeGetSelectedCandidatePair(handle: Int): Pair<String?, String?>?
    private external fun nativeGetMaximumDataChannelStream(handle: Int): Long
    private external fun nativeGetRemoteMaxMessageSize(handle: Int): Int

    private external fun nativeCreateDataChannel(handle: Int, label: String): Int
    private external fun nativeCreateDataChannelEx(handle: Int, label: String, initHandle: Long): Int

    private external fun nativeAddTrack(handle: Int, mediaDescriptionSdp: String): Int
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RtcPeerConnection

        return handle == other.handle
    }

    override fun hashCode(): Int {
        return handle
    }


}
