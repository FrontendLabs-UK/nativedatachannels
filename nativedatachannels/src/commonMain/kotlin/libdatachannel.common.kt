//import kotlin.native.CName
//
//// rtcState
//typealias rtcState = UInt
//expect val RTC_NEW: rtcState
//expect val RTC_CONNECTING: rtcState
//expect val RTC_CONNECTED: rtcState
//expect val RTC_DISCONNECTED: rtcState
//expect val RTC_FAILED: rtcState
//expect val RTC_CLOSED: rtcState
//
//// rtcIceState
//typealias rtcIceState = UInt
//expect val RTC_ICE_NEW: rtcIceState
//expect val RTC_ICE_CHECKING: rtcIceState
//expect val RTC_ICE_CONNECTED: rtcIceState
//expect val RTC_ICE_FAILED: rtcIceState
//expect val RTC_ICE_DISCONNECTED: rtcIceState
//expect val RTC_ICE_CLOSED: rtcIceState
//
//// rtcGatheringState
//typealias rtcGatheringState = UInt
//expect val RTC_GATHERING_NEW: rtcGatheringState
//expect val RTC_GATHERING_INPROGRESS: rtcGatheringState
//expect val RTC_GATHERING_COMPLETE: rtcGatheringState
//
//// rtcSignalingState
//typealias rtcSignalingState = UInt
//expect val RTC_SIGNALING_STABLE: rtcSignalingState
//expect val RTC_SIGNALING_HAVE_LOCAL_OFFER: rtcSignalingState
//expect val RTC_SIGNALING_HAVE_REMOTE_OFFER: rtcSignalingState
//expect val RTC_SIGNALING_HAVE_LOCAL_PRANSWER: rtcSignalingState
//expect val RTC_SIGNALING_HAVE_REMOTE_PRANSWER: rtcSignalingState
//
//// rtcLogLevel
//typealias rtcLogLevel = UInt
//expect val RTC_LOG_NONE: rtcLogLevel
//expect val RTC_LOG_FATAL: rtcLogLevel
//expect val RTC_LOG_ERROR: rtcLogLevel
//expect val RTC_LOG_WARNING: rtcLogLevel
//expect val RTC_LOG_INFO: rtcLogLevel
//expect val RTC_LOG_DEBUG: rtcLogLevel
//expect val RTC_LOG_VERBOSE: rtcLogLevel
//
//// rtcCertificateType
//typealias rtcCertificateType = UInt
//expect val RTC_CERTIFICATE_DEFAULT: rtcCertificateType
//expect val RTC_CERTIFICATE_ECDSA: rtcCertificateType
//expect val RTC_CERTIFICATE_RSA: rtcCertificateType
//
//// rtcCodec
//typealias rtcCodec = UInt
//expect val RTC_CODEC_H264: rtcCodec
//expect val RTC_CODEC_VP8: rtcCodec
//expect val RTC_CODEC_VP9: rtcCodec
//expect val RTC_CODEC_H265: rtcCodec
//expect val RTC_CODEC_AV1: rtcCodec
//expect val RTC_CODEC_OPUS: rtcCodec
//expect val RTC_CODEC_PCMU: rtcCodec
//expect val RTC_CODEC_PCMA: rtcCodec
//expect val RTC_CODEC_AAC: rtcCodec
//
//// rtcDirection
//typealias rtcDirection = UInt
//expect val RTC_DIRECTION_UNKNOWN: rtcDirection
//expect val RTC_DIRECTION_SENDONLY: rtcDirection
//expect val RTC_DIRECTION_RECVONLY: rtcDirection
//expect val RTC_DIRECTION_SENDRECV: rtcDirection
//expect val RTC_DIRECTION_INACTIVE: rtcDirection
//
//// rtcTransportPolicy
//typealias rtcTransportPolicy = UInt
//expect val RTC_TRANSPORT_POLICY_ALL: rtcTransportPolicy
//expect val RTC_TRANSPORT_POLICY_RELAY: rtcTransportPolicy
//
//typealias rtcObuPacketization = UInt
//expect val RTC_OBU_PACKETIZED_OBU: rtcObuPacketization
//expect val RTC_OBU_PACKETIZED_TEMPORAL_UNIT: rtcObuPacketization
//
//typealias rtcNalUnitSeparator = UInt
//expect val RTC_NAL_SEPARATOR_LENGTH: rtcNalUnitSeparator
//expect val RTC_NAL_SEPARATOR_LONG_START_SEQUENCE: rtcNalUnitSeparator
//expect val RTC_NAL_SEPARATOR_SHORT_START_SEQUENCE: rtcNalUnitSeparator
//expect val RTC_NAL_SEPARATOR_START_SEQUENCE: rtcNalUnitSeparator
//
//// Global
//const val RTC_ERR_SUCCESS = 0
//const val RTC_ERR_INVALID = -1 // Invalid argument
//const val RTC_ERR_FAILURE = -2 // Runtime error
//const val RTC_ERR_NOT_AVAIL = -3 // Element not available
//const val RTC_ERR_TOO_SMALL = -4 // Buffer too small
//
//typealias rtcLocalDescriptionCallbackFunc = (pc: Int, sdp: String?, type: String?, ptr: Any?) -> Unit
//typealias rtcLogCallbackFunc = (level: rtcLogLevel, message: String?) -> Unit
//typealias rtcCandidateCallbackFunc = (pc: Int, cand: String?, mid: String?, ptr: Any?) -> Unit
//typealias rtcStateChangeCallbackFunc = (pc: Int, state: rtcState, ptr: Any?) -> Unit
//typealias rtcIceStateChangeCallbackFunc = (pc: Int, state: rtcIceState, ptr: Any?) -> Unit
//typealias rtcGatheringStateChangeCallbackFunc = (pc: Int, state: rtcGatheringState, ptr: Any?) -> Unit
//typealias rtcSignalingStateChangeCallbackFunc = (pc: Int, state: rtcSignalingState, ptr: Any?) -> Unit
//typealias rtcDataChannelCallbackFunc = (pc: Int, dc: Int, ptr: Any?) -> Unit
//typealias rtcTrackCallbackFunc = (pc: Int, tr: Int, ptr: Any?) -> Unit
//typealias rtcOpenCallbackFunc = (id: Int, ptr: Any?) -> Unit
//typealias rtcClosedCallbackFunc = (id: Int, ptr: Any?) -> Unit
//typealias rtcErrorCallbackFunc = (id: Int, error: String?, ptr: Any?) -> Unit
//typealias rtcMessageCallbackFunc = (id: Int, message: List<Int>?, ptr: Any?) -> Unit
//typealias rtcBufferedAmountLowCallbackFunc = (id: Int, ptr: Any?) -> Unit
//typealias rtcAvailableCallbackFunc = (id: Int, ptr: Any?) -> Unit
//typealias rtcPliHandlerCallbackFunc = (tr: Int, ptr: Any?) -> Unit
//typealias rtcInterceptorCallbackFunc = (pc: Int, message: ByteArray, ptr: Any?) -> Unit
//typealias rtcWebSocketClientCallbackFunc = (wsserver: Int, ws: Int, ptr: Any?) -> Unit
//
//expect fun rtcInitLogger(level: rtcLogLevel, cb: rtcLogCallbackFunc)
//expect fun rtcSetUserPointer(id: Int, ptr: Any)
//expect fun rtcGetUserPointer(id: Int): Any?
//
//data class rtcConfiguration(
//    val iceServers: List<String>,
//    val proxyServer: String?,
//    val bindAddress: String?,
//    val certificateType: rtcCertificateType,
//    val iceTransportPolicy: rtcTransportPolicy,
//    val enableIceTcp: Boolean,
//    val enableIceUdpMux: Boolean,
//    val disableAutoNegotiation: Boolean,
//    val forceMediaTransport: Boolean,
//    val portRangeBegin: Int,
//    val portRangeEnd: Int,
//    val mtu: Int,
//    val maxMessageSize: Int
//)
//
//expect fun rtcCreatePeerConnection(configuration: rtcConfiguration): Result<Int>
//expect fun rtcClosePeerConnection(pc: Int): Result<Unit>
//expect fun rtcDeletePeerConnection(pc: Int): Result<Unit>
//
//expect fun rtcSetLocalDescriptionCallback(pc: Int, cb: rtcLocalDescriptionCallbackFunc): Result<Unit>
//expect fun rtcSetLocalCandidateCallback(pc: Int, cb: rtcCandidateCallbackFunc): Result<Unit>
//expect fun rtcSetStateChangeCallback(pc: Int, cb: rtcStateChangeCallbackFunc): Result<Unit>
//expect fun rtcSetIceStateChangeCallback(pc: Int, cb: rtcIceStateChangeCallbackFunc): Result<Unit>
//expect fun rtcSetGatheringStateChangeCallback(pc: Int, cb: rtcGatheringStateChangeCallbackFunc): Result<Unit>
//expect fun rtcSetSignalingStateChangeCallback(pc: Int, cb: rtcSignalingStateChangeCallbackFunc): Result<Unit>
//
//expect fun rtcSetLocalDescription(pc: Int, type: String): Result<Unit>
//expect fun rtcSetRemoteDescription(pc: Int, sdp: String, type: String): Result<Unit>
//expect fun rtcAddRemoteCandidate(pc: Int, cand: String, mid: String): Result<Unit>
//
//expect fun rtcGetLocalDescription(pc: Int): Result<String>
//expect fun rtcGetRemoteDescription(pc: Int): Result<String>
//
//expect fun rtcGetLocalDescriptionType(pc: Int): Result<List<Byte>>
//expect fun rtcGetRemoteDescriptionType(pc: Int): Result<List<Byte>>
//
//expect fun rtcGetLocalAddress(pc: Int): Result<String>
//expect fun rtcGetRemoteAddress(pc: Int): Result<String>
//
//expect fun rtcGetSelectedCandidatePair(pc: Int): Result<Pair<List<Byte>, List<Byte>>> // (Local, Remote)
//
//expect fun rtcGetMaxDataChannelStream(pc: Int): Result<Int>
//expect fun rtcGetRemoteMaxMessageSize(pc: Int): Result<Int>
//
//// DataChannel, Track, and WebSocket common API
//expect fun rtcSetOpenCallback(id: Int, cb: rtcOpenCallbackFunc): Result<Unit>
//expect fun rtcSetClosedCallback(id: Int, cb: rtcClosedCallbackFunc): Result<Unit>
//expect fun rtcSetErrorCallback(id: Int, cb: rtcErrorCallbackFunc): Result<Unit>
//expect fun rtcSetMessageCallback(id: Int, cb: rtcMessageCallbackFunc): Result<Unit>
//expect fun rtcSendMessage(id: Int, msg: List<Byte>): Result<Unit>
//expect fun rtcClose(id: Int): Result<Unit>
//expect fun rtcDelete(id: Int): Result<Unit>
//expect fun rtcIsOpen(id: Int): Boolean
//expect fun rtcIsClosed(id: Int): Boolean
//
//expect fun rtcMaxMessageSize(id: Int): Result<Int>
//expect fun rtcGetBufferedAmount(id: Int): Result<Int>
//expect fun rtcSetBufferedAmountLowThreshold(id: Int, amount: Int): Result<Int>
//expect fun rtcSetBufferedAmountLowCallback(id: Int, cb: rtcBufferedAmountLowCallbackFunc): Result<Int>
//
//// DataChannel, Track, and WebSocket common extended API
//expect fun rtcGetAvailableAmount(id: Int): Result<Int> // Total size available to receive
//expect fun rtcSetAvailableCallback(id: Int, cb: rtcAvailableCallbackFunc): Result<Unit>
//expect fun rtcReceiveMessage(id: Int): Result<List<Byte>>
//
//// DataChannel
//data class RtcReliability(
//    val unordered: Boolean,
//    val unreliable: Boolean,
//    val maxPacketLifeTime: UInt, // ignored if reliable
//    val maxRetransmits: UInt // ignored if reliable
//)
//
//data class RtcDataChannelInit(
//    val reliability: RtcReliability,
//    val protocol: String?, // empty string if null
//    val negotiated: Boolean,
//    val manualStream: Boolean,
//    val stream: UInt // numeric ID 0-65534, ignored if manualStream is false
//)
//
//expect fun rtcSetDataChannelCallback(pc: Int, cb: rtcDataChannelCallbackFunc): Result<Unit>
//expect fun rtcCreateDataChannel(pc: Int, label: String): Result<Int> // Returns dc id
//expect fun rtcCreateDataChannelEx(pc: Int, label: String, init: RtcDataChannelInit): Result<Int> // Returns dc id
//expect fun rtcDeleteDataChannel(dc: Int): Result<Unit>
//
//expect fun rtcGetDataChannelStream(dc: Int): Result<Int>
//expect fun rtcGetDataChannelLabel(dc: Int): Result<String?>
//expect fun rtcGetDataChannelProtocol(dc: Int): Result<String?>
//expect fun rtcGetDataChannelReliability(dc: Int): RtcReliability
//
//// Track
//
//data class RtcTrackInit(
//    val direction: rtcDirection,
//    val codec: rtcCodec,
//    val payloadType: Int,
//    val ssrc: UInt,
//    val mid: ByteArray?,
//    val name: String?, // optional
//    val msid: ByteArray?, // optional
//    val trackId: ByteArray?, // optional, track id used in MSID
//    val profile: ByteArray? // optional, codec profile
//) {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || this::class != other::class) return false
//
//        other as RtcTrackInit
//
//        if (direction != other.direction) return false
//        if (codec != other.codec) return false
//        if (payloadType != other.payloadType) return false
//        if (ssrc != other.ssrc) return false
//        if (mid != null) {
//            if (other.mid == null) return false
//            if (!mid.contentEquals(other.mid)) return false
//        } else if (other.mid != null) return false
//        if (name != other.name) return false
//        if (msid != null) {
//            if (other.msid == null) return false
//            if (!msid.contentEquals(other.msid)) return false
//        } else if (other.msid != null) return false
//        if (trackId != null) {
//            if (other.trackId == null) return false
//            if (!trackId.contentEquals(other.trackId)) return false
//        } else if (other.trackId != null) return false
//        if (profile != null) {
//            if (other.profile == null) return false
//            if (!profile.contentEquals(other.profile)) return false
//        } else if (other.profile != null) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = direction.hashCode()
//        result = 31 * result + codec.hashCode()
//        result = 31 * result + payloadType
//        result = 31 * result + ssrc.hashCode()
//        result = 31 * result + (mid?.contentHashCode() ?: 0)
//        result = 31 * result + (name?.hashCode() ?: 0)
//        result = 31 * result + (msid?.contentHashCode() ?: 0)
//        result = 31 * result + (trackId?.contentHashCode() ?: 0)
//        result = 31 * result + (profile?.contentHashCode() ?: 0)
//        return result
//    }
//}
//
//expect fun rtcSetTrackCallback(pc: Int, cb: rtcTrackCallbackFunc): Result<Unit>
//expect fun rtcAddTrack(pc: Int, mediaDescriptionSdp: ByteArray): Result<Int> // returns tr id
//expect fun rtcAddTrackEx(pc: Int, init: RtcTrackInit): Result<Unit>
//expect fun rtcDeleteTrack(tr: Int): Result<Unit>
//
//expect fun rtcGetTrackDescription(tr: Int): Result<ByteArray>
//expect fun rtcGetTrackMid(tr: Int): Result<ByteArray>
//expect fun rtcGetTrackDirection(tr: Int): Result<rtcDirection>
//
//expect fun rtcRequestKeyframe(tr: Int): Result<Unit>
//expect fun rtcRequestBitrate(tr: Int, bitrate: UInt): Result<Unit>
//
//// Media (have to enable)
//
//data class RtcPacketizerInit(
//    val ssrc: UInt,
//    val cname: String,
//    val payloadType: UInt,
//    val clockRate: UInt,
//    val sequenceNumber: UInt,
//    val timestamp: UInt,
//
//    // H264, H265, AV1
//    val maxFragmentSize: UInt, // Maximum fragment size, 0 means default
//
//    // H264/H265 only
//    val nalSeparator: rtcNalUnitSeparator, // NAL unit separator
//
//    // AV1 only
//    val obuPacketization: rtcObuPacketization // OBU paketization for AV1 samples
//)
//
//data class RtcSsrcForTypeInit(
//    val ssrc: UInt,
//    val name: String?,
//    val msid: ByteArray?,
//    val trackId: ByteArray? // Optional, track ID used in MSID
//) {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || this::class != other::class) return false
//
//        other as RtcSsrcForTypeInit
//
//        if (ssrc != other.ssrc) return false
//        if (name != other.name) return false
//        if (msid != null) {
//            if (other.msid == null) return false
//            if (!msid.contentEquals(other.msid)) return false
//        } else if (other.msid != null) return false
//        if (trackId != null) {
//            if (other.trackId == null) return false
//            if (!trackId.contentEquals(other.trackId)) return false
//        } else if (other.trackId != null) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = ssrc.hashCode()
//        result = 31 * result + (name?.hashCode() ?: 0)
//        result = 31 * result + (msid?.contentHashCode() ?: 0)
//        result = 31 * result + (trackId?.contentHashCode() ?: 0)
//        return result
//    }
//}
//
//// Allocate a new opaque message.
//// Must be explicitly freed by rtcDeleteOpaqueMessage() unless
//// explicitly returned by a media interceptor callback;
//expect fun rtcCreateOpaqueMessage(data: ByteArray): Any
//expect fun rtcDeleteOpaqueMessage(msg: Any)
//
//// Set MediaInterceptor on peer connection
//expect fun rtcSetMediaInterceptorCallback(id: Int, cb: rtcInterceptorCallbackFunc): Result<Unit>
//
//// Set a packetizer on track
//expect fun rtcSetH264Packetizer(tr: Int, init: RtcPacketizerInit): Result<Unit>
//expect fun rtcSetH265Packetizer(tr: Int, init: RtcPacketizerInit): Result<Unit>
//expect fun rtcSetAV1Packetizer(tr: Int, init: RtcPacketizerInit): Result<Unit>
//expect fun rtcSetOpusPacketizer(tr: Int, init: RtcPacketizerInit): Result<Unit>
//expect fun rtcSetAACPacketizer(tr: Int, init: RtcPacketizerInit): Result<Unit>
//
//expect fun rtcChainRtcpReceivingSession(tr: Int): Result<Unit>
//
//expect fun rtcChainRtcpSrReporter(tr: Int): Result<Unit>
//
//expect fun rtcChainRtcpNackResponder(tr: Int, maxStoredPacketsCount: UInt): Result<Unit>
//
//expect fun rtcChainPliHandler(tr: Int, cb: rtcPliHandlerCallbackFunc): Result<Unit>
//
//// Transform seconds to timestamp using track's clock rate
//expect fun rtcTransformSecondsToTimestamp(tr: Int, seconds: Double): Result<UInt>
//
//// Transform timestamp to seconds using track's clock rate
//expect fun rtcTransformTimestampToSeconds(tr: Int, timestamp: UInt): Result<Double>
//
//expect fun rtcGetCurrentTrackTimestamp(tr: Int, timestamp: UInt): Result<UInt>
//
//expect fun rtcSetTrackRtpTimestamp(tr: Int, timestamp: UInt): Result<Unit>
//
//// Get timestamp of last RTCP SR
//expect fun rtcGetLastTrackSenderReportTimestamp(tr: Int): Result<UInt>
//
//// Set NeedsToReport flag in RtcpSrReporter handler identified by given track id
//expect fun rtcSetNeedsToSendRtcpSr(tr: Int): Result<UInt>
//
//// Get all available payload types for given codec
//expect fun rtcGetTrackPayloadTypesForCodec(tr: Int, codec: ByteArray): Result<ByteArray>
//
//expect fun rtcGetSsrcsForTrack(tr: Int): Result<List<UInt>>
//
//expect fun rtcGetCNameForSsrc(tr: Int, ssrc: UInt): Result<ByteArray>
//
//expect fun rtcGetSsrcsForType(mediaType: ByteArray, sdp: ByteArray, init: RtcSsrcForTypeInit): Result<ByteArray>
//
//// END Media
//
//// WebSocket (have to enable)
//
//data class RtcWsConfiguration(
//    val disableTlsVerification: Boolean, // if true, don't verify the TLS certificate
//    val proxyServer: String?, // only non-authenticated http supported for now
//    val protocols: List<String>,
//    val connectionTimeoutMs: Int, // 0 means default, < 0 means disabled
//    val pingIntervalMs: Int, // 0 means default, < 0 means disabled
//    val maxOutstandingPings: Int, // 0 means default, < 0 means disabled
//    val maxMessageSize: Int // <= 0 means default
//)
//
//expect fun rtcCreateWebSocket(url: String): Result<Int> // returns ws id
//expect fun rtcCreateWebSocketEx(url: String, config: RtcWsConfiguration): Result<Int>
//expect fun rtcDeleteWebSocket(ws: Int)
//
//expect fun rtcGetWebSocketRemoteAddress(ws: Int): Result<String>
//expect fun rtcGetWebSocketPath(ws: Int): Result<String>
//
//// WebSocketServer
//
//data class RtcWsServerConfiguration(
//    val port: UInt, // 0 means automatic selection
//    val enableTls: Boolean, // If true, enable TLS (WSS)
//    val certificatePemFile: String?, // NULL for auto-generated certificate
//    val keyPemFile: String?, // NULL for auto-generated certificate
//    val keyPemPass: String?, // NULL if no pass
//    val bindAddress: String?, // NULL for any
//    val connectionTimeoutMs: Int, // 0 means default, < 0 means disabled
//    val maxMessageSize: Int // <= 0 means default
//)
//
//expect fun rtcCreateWebSocketServer(config: RtcWsServerConfiguration, cb: rtcWebSocketClientCallbackFunc): Result<Int> // returns wsserver id
//expect fun rtcDeleteWebSocketServer(wsserver: Int): Result<Unit>
//expect fun rtcGetWebSocketServerPort(wsserver: Int): Result<UInt>
//
//// END WebSocket
//
//// Optional global preload and cleanup
//
//expect fun rtcPreload()
//expect fun rtcCleanup()
//
//// SCTP global settings
//
//data class RtcSctpSettings(
//    val recvBufferSize: Int,                // in bytes, <= 0 means optimized default
//    val sendBufferSize: Int,                // in bytes, <= 0 means optimized default
//    val maxChunksOnQueue: Int,              // in chunks, <= 0 means optimized default
//    val initialCongestionWindow: Int,       // in MTUs, <= 0 means optimized default
//    val maxBurst: Int,                      // in MTUs, 0 means optimized default, < 0 means disabled
//    val congestionControlModule: Int,       // 0: RFC2581 (default), 1: HSTCP, 2: H-TCP, 3: RTCC
//    val delayedSackTimeMs: Int,             // in milliseconds, 0 means optimized default, < 0 means disabled
//    val minRetransmitTimeoutMs: Int,        // in milliseconds, <= 0 means optimized default
//	val maxRetransmitTimeoutMs: Int,        // in milliseconds, <= 0 means optimized default
//	val initialRetransmitTimeoutMs: Int,    // in milliseconds, <= 0 means optimized default
//	val maxRetransmitAttempts: Int,         // number of retransmissions, <= 0 means optimized default
//	val heartbeatIntervalMs: Int,           // in milliseconds, <= 0 means optimized default
//)
//
//// Note: SCTP settings apply to newly-created PeerConnections only
//expect fun rtcSetSctpSettings(settings: RtcSctpSettings): Result<Unit>