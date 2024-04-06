import kotlin.native.CName

data class RtcConfig(
    var iceServers: List<String>,
    var proxyServer: String?,
    var bindAddress: String?,
    var certificateType: CertificateType,
    var iceTransportPolicy: TransportPolicy,
    var enableIceTcp: Boolean,
    var enableIceUdpMux: Boolean,
    var portRangeBegin: UShort,
    var portRangeEnd: UShort,
    var mtu: Int,
    var maxMessageSize: Int,
    var disableAutoNegotiation: Boolean,
    var forceMediaTransport: Boolean
) {
    constructor(iceServers: List<String>) : this(
        iceServers = iceServers,
        proxyServer = null,
        bindAddress = null,
        certificateType = CertificateType.Default,
        iceTransportPolicy = TransportPolicy.All,
        enableIceTcp = false,
        enableIceUdpMux = false,
        portRangeBegin = 0u,
        portRangeEnd = 0u,
        mtu = 0,
        maxMessageSize = 0,
        disableAutoNegotiation = false,
        forceMediaTransport = false
    )

    fun bindAddress(addr: String): RtcConfig {
        return this.apply {
            bindAddress = addr
        }
    }

    fun proxyServer(proxy: String): RtcConfig {
        return this.apply {
            proxyServer = proxy
        }
    }

    fun certificateType(type: CertificateType): RtcConfig {
        return this.apply {
            certificateType = type
        }
    }

    fun iceTransportPolicy(policy: TransportPolicy): RtcConfig {
        return this.apply {
            iceTransportPolicy = policy
        }
    }

    fun enableIceTcp(enable: Boolean): RtcConfig {
        return this.apply {
            enableIceTcp = enable
        }
    }

    fun enableIceUdpMux(enable: Boolean): RtcConfig {
        return this.apply {
            enableIceUdpMux = enable
        }
    }

    fun portRange(begin: UShort, end: UShort): RtcConfig {
        return this.apply {
            portRangeBegin = begin
            portRangeEnd = end
        }
    }

    fun mtu(mtu: Int): RtcConfig {
        return this.apply {
            this.mtu = mtu
        }
    }

    fun maxMessageSize(size: Int): RtcConfig {
        return this.apply {
            maxMessageSize = size
        }
    }

    fun disableAutoNegotiation(disable: Boolean): RtcConfig {
        return this.apply {
            disableAutoNegotiation = disable
        }
    }

    fun forceMediaTransport(force: Boolean): RtcConfig {
        return this.apply {
            forceMediaTransport = force
        }
    }
}

enum class CertificateType(value: Int) {
    Default(0),
    ECDSA(1),
    RSA(2),
}

enum class TransportPolicy(value: Int) {
    All(0),
    Relay(1),
}