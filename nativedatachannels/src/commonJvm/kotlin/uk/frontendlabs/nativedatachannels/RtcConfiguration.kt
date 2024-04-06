package uk.frontendlabs.nativedatachannels

enum class RtcCertificateType(val value: Int) {
    Default(0),
    ECDSA(1),
    RSA(2),
}

enum class RtcIceTransportPolicy(val value: Int) {
    All(0),
    Relay(1),
}

class RtcConfiguration(
    iceServers: Array<String>? = null,
    proxyServer: String? = null,
    bindAddress: String? = null,
    certificateType: RtcCertificateType = RtcCertificateType.Default,
    iceTransportPolicy: RtcIceTransportPolicy = RtcIceTransportPolicy.All,
    enableIceTcp: Boolean = true,
    enableIceUdpMux: Boolean = false,
    disableAutoNegotiation: Boolean = false,
    forceMediaTransport: Boolean = false,
    portRangeBegin: Int = 0,
    portRangeEnd: Int = 0,
    mtu: Int = 0,
    maxMessageSize: Int = 0
) {
    internal val nativeHandle: Long

    init {
        nativeHandle = nativeCreate()
        if (nativeHandle == -1L) {
            throw RuntimeException("Failed to create native RtcConfiguration")
        }
        iceServers(nativeHandle, iceServers)
        proxyServer(nativeHandle, proxyServer)
        bindAddress(nativeHandle, bindAddress)
        certificateType(nativeHandle, certificateType.value)
        iceTransportPolicy(nativeHandle, iceTransportPolicy.value)
        enableIceTcp(nativeHandle, enableIceTcp)
        enableIceUdpMux(nativeHandle, enableIceUdpMux)
        disableAutoNegotiation(nativeHandle, disableAutoNegotiation)
        forceMediaTransport(nativeHandle, forceMediaTransport)
        portRange(nativeHandle, portRangeBegin, portRangeEnd)
        mtu(nativeHandle, mtu)
        maxMessageSize(nativeHandle, maxMessageSize)
    }

    private external fun nativeCreate(): Long
    private external fun nativeDelete(handle: Long)
    private external fun iceServers(handle: Long, servers: Array<String>?)
    private external fun proxyServer(handle: Long, server: String?)
    private external fun bindAddress(handle: Long, address: String?)
    private external fun certificateType(handle: Long, type: Int)
    private external fun iceTransportPolicy(handle: Long, policy: Int)
    private external fun enableIceTcp(handle: Long, enable: Boolean)
    private external fun enableIceUdpMux(handle: Long, enable: Boolean)
    private external fun disableAutoNegotiation(handle: Long, disable: Boolean)
    private external fun forceMediaTransport(handle: Long, force: Boolean)
    private external fun portRange(handle: Long, begin: Int, end: Int)
    private external fun mtu(handle: Long, mtu: Int)
    private external fun maxMessageSize(handle: Long, size: Int)
}