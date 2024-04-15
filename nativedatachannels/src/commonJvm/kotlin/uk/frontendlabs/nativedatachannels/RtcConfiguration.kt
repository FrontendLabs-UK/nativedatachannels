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

class RtcConfiguration: LibDatachannels.rtcConfiguration()