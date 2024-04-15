package uk.frontendlabs.nativedatachannels

import com.sun.jna.Callback
import com.sun.jna.Library
import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.Structure.FieldOrder

interface LibDatachannels: Library {

    // CONSTANTS

    companion object {

        var path: String? = null
        val INSTANCE: LibDatachannels by lazy {
            if (path != null) Native.load(path, LibDatachannels::class.java)
            else Native.load(LibDatachannels::class.java)
        }

        // rtcState
        const val RTC_NEW = 0
        const val RTC_CONNECTING = 1
        const val RTC_CONNECTED = 2
        const val RTC_DISCONNECTED = 3
        const val RTC_FAILED = 4
        const val RTC_CLOSED = 5

        // rtcIceState
        const val RTC_ICE_NEW = 0
        const val RTC_ICE_CHECKING = 1
        const val RTC_ICE_CONNECTED = 2
        const val RTC_ICE_COMPLETED = 3
        const val RTC_ICE_FAILED = 4
        const val RTC_ICE_DISCONNECTED = 5
        const val RTC_ICE_CLOSED = 6

        // rtcGatheringState
        const val RTC_GATHERING_NEW = 0
        const val RTC_GATHERING_INPROGRESS = 1
        const val RTC_GATHERING_COMPLETE = 2

        // rtcSignallingState
        const val RTC_SIGNALING_STABLE = 0
        const val RTC_SIGNALING_HAVE_LOCAL_OFFER = 1
        const val RTC_SIGNALING_HAVE_REMOTE_OFFER = 2
        const val RTC_SIGNALING_HAVE_LOCAL_PRANSWER = 3
        const val RTC_SIGNALING_HAVE_REMOTE_PRANSWER = 4

        // rtcLogLevel
        const val RTC_LOG_NONE = 0
        const val RTC_LOG_FATAL = 1
        const val RTC_LOG_ERROR = 2
        const val RTC_LOG_WARNING = 3
        const val RTC_LOG_INFO = 4
        const val RTC_LOG_DEBUG = 5
        const val RTC_LOG_VERBOSE = 6

        // rtcCertificateType
        const val RTC_CERTIFICATE_DEFAULT = 0
        const val RTC_CERTIFICATE_ECDSA = 1
        const val RTC_CERTIFICATE_RSA = 2

        // rtcCodec
        // (video
        const val RTC_CODEC_H264 = 0
        const val RTC_CODEC_VP8 = 1
        const val RTC_CODEC_VP9 = 2
        const val RTC_CODEC_H265 = 3
        const val RTC_CODEC_AV1 = 4
        // (audio)
        const val RTC_CODEC_OPUS = 128
        const val RTC_CODEC_PCMU = 129
        const val RTC_CODEC_PCMA = 130
        const val RTC_CODEC_AAC = 131

        // rtcDirection
        const val RTC_DIRECTION_UNKNOWN = 0
        const val RTC_DIRECTION_SENDONLY = 1
        const val RTC_DIRECTION_RECVONLY = 2
        const val RTC_DIRECTION_SENDRECV = 3
        const val RTC_DIRECTION_INACTIVE = 4

        // rtcTransportPolicy
        const val RTC_TRANSPORT_POLICY_ALL = 0
        const val RTC_TRANSPORT_POLICY_RELAY = 1

        // response codes
        const val RTC_ERR_SUCCESS = 0
        const val RTC_ERR_INVALID = -1   // invalid argument
        const val RTC_ERR_FAILURE = -2   // runtime error
        const val RTC_ERR_NOT_AVAIL = -3 // element not available
        const val RTC_ERR_TOO_SMALL = -4 // buffer too small

        // Define how OBUs are packetizied in a AV1 Sample
        // rtcObuPacketization
        const val RTC_OBU_PACKETIZED_OBU = 0
        const val RTC_OBU_PACKETIZED_TEMPORAL_UNIT = 1

        // Define how NAL units are separated in a H264/H265 sample
        // rtcNalUnitSeparator
        const val RTC_NAL_SEPARATOR_LENGTH = 0 // first 4 bytes are NAL unit length
        const val RTC_NAL_SEPARATOR_LONG_START_SEQUENCE = 1 // 0x00, 0x00, 0x00, 0x01
        const val RTC_NAL_SEPARATOR_SHORT_START_SEQUENCE = 2 // 0x00, 0x00, 0x01
        const val RTC_NAL_SEPARATOR_START_SEQUENCE = 3 // long or short start sequence

    }

    // CALLBACKS
    interface rtcLogCallbackFunc: Callback {
        fun invoke(level: Int, message: String)
    }

    interface rtcDescriptionCallbackFunc: Callback {
        fun invoke(pc: Int, sdp: String, type: String, pointer: Pointer?)
    }

    interface rtcCandidateCallbackFunc: Callback {
        fun invoke(pc: Int, cand: String, mid: String, pointer: Pointer?)
    }

    interface rtcStateChangeCallbackFunc: Callback {
        fun invoke(pc: Int, rtcState: Int, pointer: Pointer?)
    }

    interface rtcIceStateChangeCallbackFunc: Callback {
        fun invoke(pc: Int, iceState: Int, pointer: Pointer?)
    }

    interface rtcGatheringStateCallbackFunc: Callback {
        fun invoke(pc: Int, state: Int, pointer: Pointer?)
    }

    interface rtcSignalingStateCallbackFunc: Callback {
        fun invoke(pc: Int, state: Int, pointer: Pointer?)
    }

    interface rtcDataChannelCallbackFunc: Callback {
        fun invoke(pc: Int, dataChannel: Int, pointer: Pointer?)
    }

    interface rtcTrackCallbackFunc: Callback {
        fun invoke(pc: Int, track: Int, pointer: Pointer?)
    }

    interface rtcOpenCallbackFunc: Callback {
        fun invoke(id: Int, pointer: Pointer?)
    }

    interface rtcClosedCallbackFunc: Callback {
        fun invoke(id: Int, pointer: Pointer?)
    }

    interface rtcErrorCallbackFunc: Callback {
        fun invoke(id: Int, error: String, pointer: Pointer?)
    }

    interface rtcMessageCallbackFunc: Callback {
        fun invoke(id: Int, message: Pointer, size: Int, pointer: Pointer?)
    }

    interface rtcInterceptorCallbackFunc: Callback {
        fun invoke(id: Int, message: Pointer, size: Int, pointer: Pointer?)
    }

    interface rtcBufferedAmountLowCallbackFunc: Callback {
        fun invoke(id: Int, pointer: Pointer?)
    }

    interface rtcAvailableCallbackFunc: Callback {
        fun invoke(id: Int, pointer: Pointer?)
    }

    interface rtcPliHandlerCallbackFunc: Callback {
        fun invoke(tr: Int, pointer: Pointer?)
    }

    interface rtcWebSocketClientCallbackFunc: Callback {
        fun invoke(wsserver: Int, ws: Int, pointer: Pointer?)
    }

    // FUNCTIONS
    fun rtcInitLogger(level: Int, log: rtcLogCallbackFunc)
    fun rtcSetUserPointer(id: Int, ptr: Pointer)
    fun rtcGetUserPointer(id: Int): Pointer

    // (PeerConnection)
    @FieldOrder("iceServers", "iceServersCount", "proxyServer", "bindAddress", "certificateType", "iceTransportPolicy", "enableIceTcp", "enableIceUdpMux", "disableAutoNegotiation", "forceMediaTransport", "portRangeBegin", "portRangeEnd", "mtu", "maxMessageSize")
    open class rtcConfiguration: Structure() {
        @JvmField var iceServers: Pointer? = Pointer.NULL
        @JvmField var iceServersCount: Int = 0
        @JvmField var proxyServer: String? = null
        @JvmField var bindAddress: String? = null
        @JvmField var certificateType: Int = 0
        @JvmField var iceTransportPolicy: Int = 0
        @JvmField var enableIceTcp: Boolean = false
        @JvmField var enableIceUdpMux: Boolean = false
        @JvmField var disableAutoNegotiation: Boolean = false
        @JvmField var forceMediaTransport: Boolean = false
        @JvmField var portRangeBegin: Short = 0
        @JvmField var portRangeEnd: Short = 0
        @JvmField var mtu: Int = 0
        @JvmField var maxMessageSize: Int = 0

        class ByValue: rtcConfiguration(), Structure.ByValue
        class ByReference: rtcConfiguration(), Structure.ByReference
    }

    fun rtcCreatePeerConnection(config: rtcConfiguration): Int
    fun rtcClosePeerConnection(pc: Int): Int
    fun rtcDeletePeerConnection(pc: Int): Int

    fun rtcSetLocalDescriptionCallback(pc: Int, cb: rtcDescriptionCallbackFunc): Int
    fun rtcSetLocalCandidateCallback(pc: Int, cb: rtcCandidateCallbackFunc): Int
    fun rtcSetStateChangeCallback(pc: Int, cb: rtcStateChangeCallbackFunc): Int
    fun rtcSetIceStateChangeCallback(pc: Int, cb: rtcIceStateChangeCallbackFunc): Int
    fun rtcSetGatheringStateChangeCallback(pc: Int, cb: rtcGatheringStateCallbackFunc): Int
    fun rtcSetSignalingStateChangeCallback(pc: Int, cb: rtcSignalingStateCallbackFunc): Int

    fun rtcSetLocalDescription(pc: Int, type: String?): Int
    fun rtcSetRemoteDescription(pc: Int, sdp: String, type: String): Int
    fun rtcAddRemoteCandidate(pc: Int, cand: String, mid: String): Int

    fun rtcGetLocalDescription(pc: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetRemoteDescription(pc: Int, buffer: ByteArray, size: Int): Int

    fun rtcGetLocalDescriptionType(pc: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetRemoteDescriptionType(pc: Int, buffer: ByteArray, size: Int): Int

    fun rtcGetLocalAddress(pc: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetRemoteAddress(pc: Int, buffer: ByteArray, size: Int): Int

    fun rtcGetSelectedCandidatePair(pc: Int, local: ByteArray, localSize: Int, remote: ByteArray, remoteSize: Int): Int

    fun rtcGetMaxDataChannelStream(pc: Int): Int
    fun rtcGetRemoteMaxMessageSize(pc: Int): Int

    // DataChannel, Track, and WebSocket common API
    fun rtcSetOpenCallback(id: Int, cb: rtcOpenCallbackFunc): Int
    fun rtcSetClosedCallback(id: Int, cb: rtcClosedCallbackFunc): Int
    fun rtcSetErrorCallback(id: Int, cb: rtcErrorCallbackFunc): Int
    fun rtcSetMessageCallback(id: Int, cb: rtcMessageCallbackFunc): Int
    fun rtcSendMessage(id: Int, data: Pointer, size: Int): Int
    fun rtcClose(id: Int): Int
    fun rtcDelete(id: Int): Int
    fun rtcIsOpen(id: Int): Boolean
    fun rtcIsClosed(id: Int): Boolean

    fun rtcMaxMessageSize(id: Int): Int
    fun rtcGetBufferedAmount(id: Int): Int // total size buffered to send
    fun rtcSetBufferedAmountLowThreshold(id: Int, amount: Int): Int
    fun rtcSetBufferedAmountLowCallback(id: Int, cb: rtcBufferedAmountLowCallbackFunc): Int

    // DataChannel, Track, and WebSocket common extended API
    fun rtcGetAvailableAmount(id: Int): Int // total size available to receive
    fun rtcSetAvailableCallback(id: Int, cb: rtcAvailableCallbackFunc): Int
    fun rtcReceiveMessage(id: Int, buffer: Pointer, size: Int): Int

    // DataChannel
    @FieldOrder("unordered", "unreliable", "maxPacketLifeTime", "maxRetransmits")
    open class rtcReliability: Structure() {
        @JvmField var unordered: Boolean = false
        @JvmField var unreliable: Boolean = false
        @JvmField var maxPacketLifeTime: Int = 0
        @JvmField var maxRetransmits: Int = 0

        class ByValue: rtcReliability(), Structure.ByValue
        class ByReference: rtcReliability(), Structure.ByReference
    }
    @FieldOrder("reliability", "protocol", "negotiated", "manualStream", "stream")
    open class rtcDataChannelInit: Structure() {
        @JvmField var reliability: rtcReliability = rtcReliability()
        @JvmField var protocol: String? = null // empty string if NULL
        @JvmField var negotiated: Boolean = false
        @JvmField var manualStream: Boolean = false
        @JvmField var stream: Short = 0  // numeric ID 0-65534, ignored if manualStream is false

        class ByValue: rtcDataChannelInit(), Structure.ByValue
        class ByReference: rtcDataChannelInit(), Structure.ByReference
    }

    fun rtcSetDataChannelCallback(pc: Int, cb: rtcDataChannelCallbackFunc): Int
    fun rtcCreateDataChannel(pc: Int, label: String): Int
    fun rtcCreateDataChannelEx(pc: Int, label: String, init: rtcDataChannelInit): Int // returns dc id
    fun rtcDeleteDataChannel(dc: Int): Int

    fun rtcGetDataChannelStream(dc: Int): Int
    fun rtcGetDataChannelLabel(dc: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetDataChannelProtocol(dc: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetDataChannelReliability(dc: Int, reliability: rtcReliability): Int

    // Track
    @FieldOrder("direction", "codec", "payloadType", "ssrc", "mid", "name", "msid", "trackId", "profile")
    open class rtcTrackInit: Structure() {
        @JvmField var direction: Int = 0
        @JvmField var codec: Int = 0
        @JvmField var payloadType: Int = 0
        @JvmField var ssrc: Int = 0
        @JvmField var mid: String = ""
        @JvmField var name: String? = null
        @JvmField var msid: String? = null
        @JvmField var trackId: String? = null
        @JvmField var profile: String? = null

        class ByValue: rtcTrackInit(), Structure.ByValue
        class ByReference: rtcTrackInit(), Structure.ByReference
    }
    fun rtcSetTrackCallback(pc: Int, cb: rtcTrackCallbackFunc): Int
    fun rtcAddTrack(pc: Int, mediaDescriptionSdp: String): Int
    fun rtcAddTrackEx(pc: Int, init: rtcTrackInit): Int
    fun rtcDeleteTrack(tr: Int): Int

    fun rtcGetTrackDescription(tr: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetTrackMid(tr: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetTrackDirection(tr: Int, direction: IntArray): Int // [0] = direction

    fun rtcRequestKeyframe(tr: Int): Int
    fun rtcRequestBitrate(tr: Int, bitrate: Int): Int

    // Media
    @FieldOrder("ssrc", "cname", "payloadType", "clockRate", "sequenceNumber", "timestamp", "maxFragmentSize", "nalSeparator", "obuPacketization")
    open class rtcPacketizerInit: Structure() {
        @JvmField var ssrc: Int = 0
        @JvmField var cname: String? = null
        @JvmField var payloadType: Byte = 0
        @JvmField var clockRate: Int = 0
        @JvmField var sequenceNumber: Short = 0
        @JvmField var timestamp: Int = 0
        @JvmField var maxFragmentSize: Short = 0
        @JvmField var nalSeparator: Int = 0
        @JvmField var obuPacketization: Int = 0

        class ByValue: rtcPacketizerInit(), Structure.ByValue
        class ByReference: rtcPacketizerInit(), Structure.ByReference
    }

    @FieldOrder("ssrc", "name", "msid", "trackId")
    open class rtcSsrcForTypeInit: Structure() {
        @JvmField var ssrc: Int = 0
        @JvmField var name: String? = null
        @JvmField var msid: String? = null
        @JvmField var trackId: String? = null

        class ByValue: rtcSsrcForTypeInit(), Structure.ByValue
        class ByReference: rtcSsrcForTypeInit(), Structure.ByReference
    }

    // Allocate a new opaque message.
    // Must be explicitly freed by rtcDeleteOpaqueMessage() unless
    // explicitly returned by a media interceptor callback;
    fun rtcCreateOpaqueMessage(data: Pointer, size: Int): Pointer
    fun rtcDeleteOpaqueMessage(msg: Pointer)

    // Set MediaInterceptor on peer connection
    fun rtcSetMediaInterceptorCallback(id: Int, cb: rtcInterceptorCallbackFunc): Int

    // Set a packetizer on track
    fun rtcSetH264Packetizer(tr: Int, init: rtcPacketizerInit): Int
    fun rtcSetH265Packetizer(tr: Int, init: rtcPacketizerInit): Int
    fun rtcSetAV1Packetizer(tr: Int, init: rtcPacketizerInit): Int
    fun rtcSetOpusPacketizer(tr: Int, init: rtcPacketizerInit): Int
    fun rtcSetAACPacketizer(tr: Int, init: rtcPacketizerInit): Int

//    // Chain RtcpReceivingSession on track
    fun rtcChainRtcpReceivingSession(tr: Int): Int

//// Chain RtcpSrReporter on track
    fun rtcChainRtcpSrReporter(tr: Int): Int

//// Chain RtcpNackResponder on track
    fun rtcChainRtcpNackResponder(tr: Int, maxStoredPacketsCount: Int): Int

//// Chain PliHandler on track
    fun rtcChainPliHandler(tr: Int, cb: rtcPliHandlerCallbackFunc): Int

//// Transform seconds to timestamp using track's clock rate, result is written to timestamp
    fun rtcTransformSecondsToTimestamp(id: Int, seconds: Double, timestamp: IntArray): Int

//// Transform timestamp to seconds using track's clock rate, result is written to seconds
    fun rtcTransformTimestampToSeconds(id: Int, timestamp: Int, seconds: DoubleArray): Int

//// Get current timestamp, result is written to timestamp
    fun rtcGetCurrentTrackTimestamp(id: Int, timestamp: IntArray): Int

//// Set RTP timestamp for track identified by given id
    fun rtcSetTrackRtpTimestamp(id: Int, timestamp: Int): Int

//// Get timestamp of last RTCP SR, result is written to timestamp
    fun rtcGetLastTrackSenderReportTimestamp(id: Int, timestamp: IntArray): Int

//// Set NeedsToReport flag in RtcpSrReporter handler identified by given track id
    fun rtcSetNeedsToSendRtcpSr(id: Int): Int

//// Get all available payload types for given codec and stores them in buffer, does nothing if
//// buffer is NULL
    fun rtcGetTrackPayloadTypesForCodec(tr: Int, ccodec: String, buffer: IntArray, size: Int): Int

//// Get all SSRCs for given track
    fun rtcGetSsrcsForTrack(tr: Int, buffer: IntArray, count: Int): Int

//// Get CName for SSRC
    fun rtcGetCNameForSsrc(tr: Int, ssrc: Int, cname: ByteArray, size: Int): Int

//// Get all SSRCs for given media type in given SDP
    fun rtcGetSsrcsForType(mediaType: String, sdp: String, buffer: IntArray, bufferSize: Int): Int

//// Set SSRC for given media type in given SDP
    fun rtcSetSsrcForType(mediaType: String, sdp: String, buffer: ByteArray, bufferSize: Int, init: rtcSsrcForTypeInit): Int

    // WebSocket
    @FieldOrder("disableTlsVerification", "proxyServer", "protocols", "protocolsCount", "connectionTimeoutMs", "pingIntervalMs", "maxOutstandingPings", "maxMessageSize")
    open class rtcWsConfiguration: Structure() {
        @JvmField var disableTlsVerification: Boolean = false
        @JvmField var proxyServer: String? = null
        @JvmField var protocols: Array<String>? = null
        @JvmField var protocolsCount: Int = 0
        @JvmField var connectionTimeoutMs: Int = 0
        @JvmField var pingIntervalMs: Int = 0
        @JvmField var maxOutstandingPings: Int = 0
        @JvmField var maxMessageSize: Int = 0

        class ByValue: rtcWsConfiguration(), Structure.ByValue
        class ByReference: rtcWsConfiguration(), Structure.ByReference
    }

    fun rtcCreateWebSocket(url: String): Int // returns ws id
    fun rtcCreateWebSocketEx(url: String, config: rtcWsConfiguration): Int
    fun rtcDeleteWebSocket(ws: Int): Int

    fun rtcGetWebSocketRemoteAddress(ws: Int, buffer: ByteArray, size: Int): Int
    fun rtcGetWebSocketPath(ws: Int, buffer: ByteArray, size: Int): Int

    // WebSocketServer
    @FieldOrder("port", "enableTls", "certificatePemFile", "keyPemFile", "keyPemPass", "bindAddress", "connectionTimeoutMs", "maxMessageSize")
    open class rtcWsServer: Structure() {
        @JvmField var port: Short = 0
        @JvmField var enableTls: Boolean = false
        @JvmField var certificatePemFile: String? = null
        @JvmField var keyPemFile: String? = null
        @JvmField var keyPemPass: String? = null
        @JvmField var bindAddress: String? = null
        @JvmField var connectionTimeoutMs: Int = 0
        @JvmField var maxMessageSize: Int = 0

        class ByValue: rtcWsServer(), Structure.ByValue
        class ByReference: rtcWsServer(), Structure.ByReference
    }

    fun rtcCreateWebSocketServer(config: rtcWsServer, cb: rtcWebSocketClientCallbackFunc): Int
    fun rtcDeleteWebSocketServer(wsserver: Int): Int
    fun rtcGetWebSocketServerPort(wsserver: Int): Int

    // Optional global preload and cleanup
    fun rtcPreload()
    fun rtcCleanup()

    // SCTP global settings
    @FieldOrder("recvBufferSize", "sendBufferSize", "maxChunksOnQueue", "initialCongestionWindow", "maxBurst", "congestionControlModule", "delayedSackTimeMs", "minRetransmitTimeoutMs", "maxRetransmitTimeoutMs", "initialRetransmitTimeoutMs", "maxRetransmitAttempts", "heartbeatIntervalMs")
    open class rtcSctpSettings: Structure() {
        @JvmField var recvBufferSize: Int = 0
        @JvmField var sendBufferSize: Int = 0
        @JvmField var maxChunksOnQueue: Int = 0
        @JvmField var maxBurst: Int = 0
        @JvmField var congestionControlModule: Int = 0
        @JvmField var delayedSackTimeMs: Int = 0
        @JvmField var minRetransmitTimeoutMs: Int = 0
        @JvmField var maxRetransmitTimeoutMs: Int = 0
        @JvmField var initialRetransmitTimeoutMs: Int = 0
        @JvmField var maxRetransmitAttempts: Int = 0
        @JvmField var heartbeatIntervalMs: Int = 0

        class ByValue: rtcSctpSettings(), Structure.ByValue
        class ByReference: rtcSctpSettings(), Structure.ByReference
    }

    // Note: SCTP settings apply to newly-created PeerConnections only
    fun rtcSetSctpSettings(settings: rtcSctpSettings): Int


    //// C FILE IO Utilities
    fun fopen(filename: String, mode: String): Pointer?
    fun fclose(file: Pointer): Int
    fun fread(ptr: Pointer, size: Int, numberOfElements: Int, file: Pointer): Int
    fun fwrite(ptr: Pointer, size: Int, numberOfElements: Int, file: Pointer): Int

    fun malloc(size: Int): Pointer?
    fun free(ptr: Pointer)
}