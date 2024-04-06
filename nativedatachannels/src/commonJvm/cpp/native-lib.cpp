#include <jni.h>
#include <string>
#include <string.h>
#include <map>
#include "rtc.h"

struct Callbacks {
    jobject open_cb = nullptr;
    jobject closed_cb = nullptr;
    jobject error_cb = nullptr;
    jobject message_cb = nullptr;
    jobject buffer_amounts_low_cb = nullptr;
    jobject available_cb = nullptr;
    jobject wss_cb = nullptr;

    // PeerConnection
    jobject local_description_cb = nullptr;
    jobject local_candidate_cb = nullptr;
    jobject state_change_cb = nullptr;
    jobject gathering_state_change_cb = nullptr;
    jobject data_channel_cb = nullptr;
    jobject track_cb = nullptr;
};

static std::map<int, Callbacks> dataChannelCallbacks;
static JavaVM *vm;

static JNIEnv *getEnv() {
    JNIEnv *env;
    int result = vm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (result == JNI_EDETACHED) {
#if ANDROID
        result = vm->AttachCurrentThread(&env, nullptr);
#else
        result = vm->AttachCurrentThread(&env, nullptr);
#endif

        if (result != JNI_OK) {
            return nullptr;
        }
    }
    return env;
}

// JNI_OnLoad
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *_vm, void *reserved) {
    vm = _vm;
    return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_nativeCreate(JNIEnv *env, jobject thiz) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(malloc(sizeof(rtcConfiguration)));
    if (!config) {
        return -1;
    }
    return reinterpret_cast<jlong>(config);
}


extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_nativeDelete(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (config) {
        for (int i = 0; i < config->iceServersCount; i++) {
            free((void *) config->iceServers[i]);
        }
        if (config->iceServers) free(config->iceServers);
        if (config->proxyServer) free((void *) config->proxyServer);
        if (config->bindAddress) free((void *) config->bindAddress);
        free(config);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_iceServers(JNIEnv *env, jobject thiz,
                                                                 jlong handle,
                                                                 jobjectArray servers) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }

    if (!servers) {
        config->iceServers = nullptr;
        config->iceServersCount = 0;
        return;
    }

    jsize length = env->GetArrayLength(servers);
    if (length == 0) {
        return;
    }

    char **iceServers = reinterpret_cast<char **>(malloc(sizeof(char *) * length));
    for (int i = 0; i < length; i++) {
        jstring server = static_cast<jstring>(env->GetObjectArrayElement(servers, i));
        const char *iceServer = env->GetStringUTFChars(server, nullptr);
        iceServers[i] = strdup(iceServer);
        env->ReleaseStringUTFChars(server, iceServer);
    }
    config->iceServers = (const char **)iceServers;
    config->iceServersCount = length;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_proxyServer(JNIEnv *env, jobject thiz,
                                                                  jlong handle, jstring server) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    if (server) {
        const char *proxyServer = env->GetStringUTFChars(server, nullptr);
        config->proxyServer = strdup(proxyServer);
    } else {
        config->proxyServer = nullptr;
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_bindAddress(JNIEnv *env, jobject thiz,
                                                                  jlong handle, jstring address) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    if (!address) {
        config->bindAddress = nullptr;
        return;
    }

    const char *bindAddress = env->GetStringUTFChars(address, nullptr);
    config->bindAddress = strdup(bindAddress);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_certificateType(JNIEnv *env, jobject thiz,
                                                                      jlong handle, jint type) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->certificateType = (rtcCertificateType)type;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_iceTransportPolicy(JNIEnv *env, jobject thiz,
                                                                         jlong handle,
                                                                         jint policy) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->iceTransportPolicy = (rtcTransportPolicy)policy;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_enableIceTcp(JNIEnv *env, jobject thiz,
                                                                   jlong handle, jboolean enable) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->enableIceTcp = enable;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_enableIceUdpMux(JNIEnv *env, jobject thiz,
                                                                      jlong handle,
                                                                      jboolean enable) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->enableIceUdpMux = enable;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_disableAutoNegotiation(JNIEnv *env,
                                                                             jobject thiz,
                                                                             jlong handle,
                                                                             jboolean disable) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->disableAutoNegotiation = disable;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_forceMediaTransport(JNIEnv *env, jobject thiz,
                                                                          jlong handle,
                                                                          jboolean force) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->forceMediaTransport = force;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_portRange(JNIEnv *env, jobject thiz,
                                                                jlong handle, jint begin,
                                                                jint end) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->portRangeBegin = begin;
    config->portRangeEnd = end;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_mtu(JNIEnv *env, jobject thiz, jlong handle,
                                                          jint mtu) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->mtu = mtu;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcConfiguration_maxMessageSize(JNIEnv *env, jobject thiz,
                                                                     jlong handle, jint size) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(handle);
    if (!config) {
        return;
    }
    config->maxMessageSize = size;
}
extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_nativeCreate(JNIEnv *env, jobject thiz) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(malloc(sizeof(rtcReliability)));
    if (!reliability) {
        return -1;
    }
    return reinterpret_cast<jlong>(reliability);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_nativeDelete(JNIEnv *env, jobject thiz,
                                                              jlong handle) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (reliability) {
        free(reliability);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_unordered(JNIEnv *env, jobject thiz, jlong handle,
                                                           jboolean value) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return;
    }
    reliability->unordered = value;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_getUnordered(JNIEnv *env, jobject thiz,
                                                              jlong handle) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return false;
    }
    return reliability->unordered;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_unreliable(JNIEnv *env, jobject thiz, jlong handle,
                                                            jboolean value) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return;
    }
    reliability->unreliable = value;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_getUnreliable(JNIEnv *env, jobject thiz,
                                                               jlong handle) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return false;
    }
    return reliability->unreliable;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_maxPacketLifeTime(JNIEnv *env, jobject thiz,
                                                                   jlong handle, jint value) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return;
    }
    reliability->maxPacketLifeTime = value;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_getMaxPacketLifeTime(JNIEnv *env, jobject thiz,
                                                                      jlong handle) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return 0;
    }
    return reliability->maxPacketLifeTime;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_maxRetransmits(JNIEnv *env, jobject thiz,
                                                                jlong handle, jint value) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return;
    }
    reliability->maxRetransmits = value;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_Reliability_getMaxRetransmits(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(handle);
    if (!reliability) {
        return 0;
    }
    return reliability->maxRetransmits;
}
extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_nativeCreate(JNIEnv *env, jobject thiz) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(malloc(sizeof(rtcDataChannelInit)));
    if (!init) {
        return -1;
    }
    return reinterpret_cast<jlong>(init);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_nativeDelete(JNIEnv *env, jobject thiz,
                                                                  jlong handle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (init) {
        free(init);
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_setReliability(JNIEnv *env, jobject thiz,
                                                                    jlong handle, jlong reliabilityHandle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return;
    }


    init->reliability = *reinterpret_cast<rtcReliability *>(reliabilityHandle);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_getReliability(JNIEnv *env, jobject thiz,
                                                                    jlong handle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return -1;
    }

    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(malloc(sizeof(rtcReliability)));
    if (!reliability) {
        return -1;
    }
    memcpy(reliability, &init->reliability, sizeof(rtcReliability));
    return reinterpret_cast<jlong>(reliability);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_setProtocol(JNIEnv *env, jobject thiz,
                                                                 jlong handle, jstring value) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return;
    }
    const char *protocol = env->GetStringUTFChars(value, nullptr);
    init->protocol = strdup(protocol);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_getProtocol(JNIEnv *env, jobject thiz,
                                                                 jlong handle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return nullptr;
    }
    return env->NewStringUTF(init->protocol);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_setNegotiated(JNIEnv *env, jobject thiz,
                                                                   jlong handle, jboolean value) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return;
    }
    init->negotiated = value;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_getNegotiated(JNIEnv *env, jobject thiz,
                                                                   jlong handle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return false;
    }
    return init->negotiated;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_setManualStream(JNIEnv *env, jobject thiz,
                                                                     jlong handle, jboolean value) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return;
    }
    init->manualStream = value;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_getManualStream(JNIEnv *env, jobject thiz,
                                                                     jlong handle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return false;
    }
    return init->manualStream;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_setStream(JNIEnv *env, jobject thiz,
                                                               jlong handle, jshort value) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return;
    }
    init->stream = value;
}
extern "C"
JNIEXPORT jshort JNICALL
Java_uk_frontendlabs_nativedatachannels_DataChannelInit_getStream(JNIEnv *env, jobject thiz,
                                                               jlong handle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(handle);
    if (!init) {
        return 0;
    }
    return init->stream;
}

void rtcOpenCallback(int id, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) {
        return;
    }

    if (dataChannelCallbacks.find(id) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[id];
    jclass clazz = env->GetObjectClass(cbs.open_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "()V");
    env->CallVoidMethod(cbs.open_cb, method);
}

void rtcClosedCallback(int id, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(id) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[id];
    jclass clazz = env->GetObjectClass(cbs.closed_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "()V");
    env->CallVoidMethod(cbs.closed_cb, method);
}

void rtcErrorCallback(int id, const char *msg, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(id) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[id];
    jclass clazz = env->GetObjectClass(cbs.error_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(Ljava/lang/String;)V");
    jstring message = env->NewStringUTF(msg);
    env->CallVoidMethod(cbs.error_cb, method, message);
    env->DeleteLocalRef(message);
}

void rtcMessageCallback(int id, const char *msg, int size, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(id) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[id];
    jclass clazz = env->GetObjectClass(cbs.message_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "([B)V");
    jbyteArray bytes = env->NewByteArray(size);
    env->SetByteArrayRegion(bytes, 0, size, reinterpret_cast<const jbyte *>(msg));
    env->CallVoidMethod(cbs.message_cb, method, bytes);
    env->DeleteLocalRef(bytes);
}

void rtcBufferedAmountLowCallback(int id, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(id) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[id];
    jclass clazz = env->GetObjectClass(cbs.buffer_amounts_low_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "()V");
    env->CallVoidMethod(cbs.buffer_amounts_low_cb, method);
}

void rtcAvailableCallback(int id, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(id) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[id];
    jclass clazz = env->GetObjectClass(cbs.available_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "()V");
    env->CallVoidMethod(cbs.available_cb, method);
}

extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeCreate(JNIEnv *env, jobject thiz,
                                                                    jlong config_handle) {
    rtcConfiguration *config = reinterpret_cast<rtcConfiguration *>(config_handle);
    if (!config) {
        return -1;
    }
    return rtcCreatePeerConnection(config);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeDelete(JNIEnv *env, jobject thiz,
                                                                    jint handle) {
    rtcDeletePeerConnection(handle);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeClose(JNIEnv *env, jobject thiz,
                                                                   jint handle) {
    return rtcClosePeerConnection(handle);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcDataChannel_getReliability(JNIEnv *env, jobject thiz,
                                                                   jint handle)  {
    rtcReliability *reliability = reinterpret_cast<rtcReliability *>(malloc(sizeof(rtcReliability)));
    if (!reliability) {
        return -1;
    }
    int result = rtcGetDataChannelReliability(handle, reliability);
    if (result < 0) {
        free(reliability);
        return -1;
    }
    return reinterpret_cast<jlong>(reliability);
}

// Globals
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_setOpenCallback(JNIEnv *env, jobject thiz,
                                                                    jint handle,
                                                                    jobject callback) {

    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }
    if (dataChannelCallbacks[handle].open_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].open_cb);
    }
    dataChannelCallbacks[handle].open_cb = env->NewGlobalRef(callback);
    rtcSetOpenCallback(handle, rtcOpenCallback);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_setClosedCallback(JNIEnv *env, jobject thiz,
                                                                      jint handle,
                                                                      jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].closed_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].closed_cb);
    }
    dataChannelCallbacks[handle].closed_cb = env->NewGlobalRef(callback);
    rtcSetClosedCallback(handle, rtcClosedCallback);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_setErrorCallback(JNIEnv *env, jobject thiz,
                                                                     jint handle,
                                                                     jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].error_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].error_cb);
    }

    dataChannelCallbacks[handle].error_cb = env->NewGlobalRef(callback);
    rtcSetErrorCallback(handle, rtcErrorCallback);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_setMessageCallback(JNIEnv *env, jobject thiz,
                                                                       jint handle,
                                                                       jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].message_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].message_cb);
    }

    dataChannelCallbacks[handle].message_cb = env->NewGlobalRef(callback);
    rtcSetMessageCallback(handle, rtcMessageCallback);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_setBufferedAmountLowCallback(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jint handle,
                                                                                 jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].buffer_amounts_low_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].buffer_amounts_low_cb);
    }

    dataChannelCallbacks[handle].buffer_amounts_low_cb = env->NewGlobalRef(callback);
    rtcSetBufferedAmountLowCallback(handle, rtcBufferedAmountLowCallback);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_setAvailableCallback(JNIEnv *env, jobject thiz,
                                                                         jint handle,
                                                                         jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].available_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].available_cb);
    }

    dataChannelCallbacks[handle].available_cb = env->NewGlobalRef(callback);
    rtcSetAvailableCallback(handle, rtcAvailableCallback);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_bufferedAmount(JNIEnv *env, jobject thiz,
                                                                   jint handle) {
    if (handle == -1) {
        return -1;
    }
    return rtcGetBufferedAmount(handle);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_availableAmount(JNIEnv *env, jobject thiz,
                                                                    jint handle) {
    if (handle == -1) {
        return -1;
    }
    return rtcGetAvailableAmount(handle);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_setBufferedAmountLowThreshold(JNIEnv *env,
                                                                                  jobject thiz,
                                                                                  jint handle,
                                                                                  jint amount) {
    if (handle == -1) {
        return;
    }
    rtcSetBufferedAmountLowThreshold(handle, amount);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcDataChannel_getStream(JNIEnv *env, jobject thiz,
                                                              jint handle) {
    return rtcGetDataChannelStream(handle);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcDataChannel_getLabel(JNIEnv *env, jobject thiz,
                                                             jint handle) {
    char label[8192];
    int result = rtcGetDataChannelLabel(handle, label, sizeof(label));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(label);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcDataChannel_getProtocol(JNIEnv *env, jobject thiz,
                                                                jint handle) {
    char protocol[8192];
    int result = rtcGetDataChannelProtocol(handle, protocol, sizeof(protocol));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(protocol);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcTrack_getDescription(JNIEnv *env, jobject thiz,
                                                             jint handle) {
    char description[8192];
    int result = rtcGetTrackDescription(handle, description, sizeof(description));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(description);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcTrack_getMid(JNIEnv *env, jobject thiz, jint handle) {
    char mid[8192];
    int result = rtcGetTrackMid(handle, mid, sizeof(mid));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(mid);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcTrack_getDirection(JNIEnv *env, jobject thiz, jint handle) {
    rtcDirection direction;
    int result = rtcGetTrackDirection(handle, &direction);

    if (result < 0) {
        return result;
    }
    return direction;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcTrack_delete(JNIEnv *env, jobject thiz, jint handle) {
    rtcDeleteTrack(handle);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocket_nativeCreate(JNIEnv *env, jobject thiz,
                                                               jstring url) {
    const char *uri = env->GetStringUTFChars(url, nullptr);
    int handle = rtcCreateWebSocket(uri);
    env->ReleaseStringUTFChars(url, uri);
    return handle;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocket_nativeCreateEx(JNIEnv *env, jobject thiz,
                                                                 jstring url,
                                                                 jboolean disable_tls_verification,
                                                                 jobjectArray protocols,
                                                                 jint connection_timeout_ms,
                                                                 jint ping_interval_ms,
                                                                 jint max_outstanding_pings) {
    const char *uri = env->GetStringUTFChars(url, nullptr);
    int length = env->GetArrayLength(protocols);
    const char **protocolsArray = reinterpret_cast<const char **>(malloc(sizeof(char *) * length));
    for (int i = 0; i < length; i++) {
        jstring protocol = static_cast<jstring>(env->GetObjectArrayElement(protocols, i));
        const char *protocolStr = env->GetStringUTFChars(protocol, nullptr);
        protocolsArray[i] = strdup(protocolStr);
        env->ReleaseStringUTFChars(protocol, protocolStr);
    }
    rtcWsConfiguration config = {
        .disableTlsVerification = static_cast<bool>(disable_tls_verification),
        .protocols = protocolsArray,
        .protocolsCount = length,
        .connectionTimeoutMs = connection_timeout_ms,
        .pingIntervalMs = ping_interval_ms,
        .maxOutstandingPings = max_outstanding_pings
    };
    int handle = rtcCreateWebSocketEx(uri, &config);
    env->ReleaseStringUTFChars(url, uri);

    for (int i = 0; i < length; i++) {
        free((void *)protocolsArray[i]);
    }
    free(protocolsArray);

    return handle;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocket_nativeDelete(JNIEnv *env, jobject thiz,
                                                               jint handle) {
    rtcDeleteWebSocket(handle);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocket_nativeGetRemoteAddress(JNIEnv *env, jobject thiz,
                                                                         jint handle) {
    char address[8192];
    int result = rtcGetWebSocketRemoteAddress(handle, address, sizeof(address));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(address);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocket_nativeGetPath(JNIEnv *env, jobject thiz,
                                                                jint handle) {
    char path[8192];
    int result = rtcGetWebSocketPath(handle, path, sizeof(path));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(path);
}

void rtcWebSocketServerCallback(int id, int websocketId, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(id) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[id];
    jclass clazz = env->GetObjectClass(cbs.wss_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(I)V");
    env->CallVoidMethod(cbs.wss_cb, method, websocketId);
}

extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocketServer_nativeCreate(JNIEnv *env, jobject thiz,
                                                                     jshort port,
                                                                     jboolean enable_tls,
                                                                     jstring certificate_pem_file,
                                                                     jstring key_pem_file,
                                                                     jstring key_pem_pass,
                                                                     jint connection_timeout_ms,
                                                                     jobject cb) {
    const char *certificatePemFile = (certificate_pem_file != nullptr) ? env->GetStringUTFChars(certificate_pem_file, nullptr) : nullptr;
    const char *keyPemFile = (key_pem_file != nullptr) ? env->GetStringUTFChars(key_pem_file, nullptr) : nullptr;
    const char *keyPemPass = (key_pem_pass != nullptr) ? env->GetStringUTFChars(key_pem_pass, nullptr) : nullptr;

    rtcWsServerConfiguration config = {
        .port = static_cast<uint16_t>(port),
        .enableTls = static_cast<bool>(enable_tls),
        .certificatePemFile = certificatePemFile != nullptr ? strdup(certificatePemFile) : nullptr,
        .keyPemFile = keyPemFile != nullptr ? strdup(keyPemFile) : nullptr,
        .keyPemPass = keyPemPass != nullptr ? strdup(keyPemPass) : nullptr,
        .connectionTimeoutMs = connection_timeout_ms
    };

    int handle = rtcCreateWebSocketServer(&config, rtcWebSocketServerCallback);
    if (certificate_pem_file) env->ReleaseStringUTFChars(certificate_pem_file, certificatePemFile);
    if (key_pem_file) env->ReleaseStringUTFChars(key_pem_file, keyPemFile);
    if (key_pem_pass) env->ReleaseStringUTFChars(key_pem_pass, keyPemPass);

    free((void*)config.certificatePemFile);
    free((void*)config.keyPemFile);
    free((void*)config.keyPemPass);

    if (handle < 0) {
        return handle;
    }

    dataChannelCallbacks[handle] = Callbacks();
    dataChannelCallbacks[handle].wss_cb = env->NewGlobalRef(cb);
    return handle;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocketServer_nativeDelete(JNIEnv *env, jobject thiz,
                                                                     jint handle) {
    rtcDeleteWebSocketServer(handle);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcWebSocketServer_nativeGetPort(JNIEnv *env, jobject thiz,
                                                                      jint handle) {
    return rtcGetWebSocketServerPort(handle);
}

void localDescriptionCallback(int pc, const char *sdp, const char *type, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(pc) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[pc];
    jclass clazz = env->GetObjectClass(cbs.local_description_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(Ljava/lang/String;Ljava/lang/String;)V");
    jstring sdpStr = env->NewStringUTF(sdp);
    jstring typeStr = env->NewStringUTF(type);
    env->CallVoidMethod(cbs.local_description_cb, method, sdpStr, typeStr);
    env->DeleteLocalRef(sdpStr);
    env->DeleteLocalRef(typeStr);
}

void localCandidateCallback(int pc, const char *candidate, const char *mid, void *ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(pc) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[pc];
    jclass clazz = env->GetObjectClass(cbs.local_candidate_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(Ljava/lang/String;Ljava/lang/String;)V");
    jstring candidateStr = env->NewStringUTF(candidate);
    jstring midStr = env->NewStringUTF(mid);
    env->CallVoidMethod(cbs.local_candidate_cb, method, candidateStr, midStr);
    env->DeleteLocalRef(candidateStr);
    env->DeleteLocalRef(midStr);
}

void stateChangeCallback(int pc, rtcState state, void *user_ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(pc) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[pc];
    jclass clazz = env->GetObjectClass(cbs.state_change_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(I)V");
    env->CallVoidMethod(cbs.state_change_cb, method, state);
}

void gatheringStateChangeCallback(int pc, rtcGatheringState state, void *user_ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(pc) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[pc];
    jclass clazz = env->GetObjectClass(cbs.gathering_state_change_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(I)V");
    env->CallVoidMethod(cbs.gathering_state_change_cb, method, state);
}

void dataChannelCallback(int pc, int dc, void *user_ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(pc) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[pc];
    jclass clazz = env->GetObjectClass(cbs.data_channel_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(I)V");
    env->CallVoidMethod(cbs.data_channel_cb, method, dc);
}

void trackCallback(int pc, int tr, void *user_ptr) {
    JNIEnv *env = getEnv();
    if (!env) return;

    if (dataChannelCallbacks.find(pc) == dataChannelCallbacks.end()) {
        return;
    }

    Callbacks cbs = dataChannelCallbacks[pc];
    jclass clazz = env->GetObjectClass(cbs.track_cb);
    jmethodID method = env->GetMethodID(clazz, "invoke", "(I)V");
    env->CallVoidMethod(cbs.track_cb, method, tr);
}

extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetLocalDescriptionCallback(
        JNIEnv *env, jobject thiz, jint handle, jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].local_description_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].local_description_cb);
    }

    dataChannelCallbacks[handle].local_description_cb = env->NewGlobalRef(callback);
    return rtcSetLocalDescriptionCallback(handle, localDescriptionCallback);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetLocalCandidateCallback(JNIEnv *env,
                                                                                       jobject thiz,
                                                                                       jint handle,
                                                                                       jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].local_candidate_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].local_candidate_cb);
    }

    dataChannelCallbacks[handle].local_candidate_cb = env->NewGlobalRef(callback);
    return rtcSetLocalCandidateCallback(handle, localCandidateCallback);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetStateChangeCallback(JNIEnv *env,
                                                                                    jobject thiz,
                                                                                    jint handle,
                                                                                    jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].state_change_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].state_change_cb);
    }

    dataChannelCallbacks[handle].state_change_cb = env->NewGlobalRef(callback);
    return rtcSetStateChangeCallback(handle, stateChangeCallback);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetGatheringStateChangeCallback(
        JNIEnv *env, jobject thiz, jint handle, jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].gathering_state_change_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].gathering_state_change_cb);
    }

    dataChannelCallbacks[handle].gathering_state_change_cb = env->NewGlobalRef(callback);
    return rtcSetGatheringStateChangeCallback(handle, gatheringStateChangeCallback);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetDataChannelCallback(JNIEnv *env,
                                                                                    jobject thiz,
                                                                                    jint handle,
                                                                                    jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].data_channel_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].data_channel_cb);
    }

    dataChannelCallbacks[handle].data_channel_cb = env->NewGlobalRef(callback);
    return rtcSetDataChannelCallback(handle, dataChannelCallback);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetTrackCallback(JNIEnv *env,
                                                                              jobject thiz,
                                                                              jint handle,
                                                                              jobject callback) {
    if (dataChannelCallbacks.find(handle) == dataChannelCallbacks.end()) {
        dataChannelCallbacks[handle] = Callbacks();
    }

    if (dataChannelCallbacks[handle].track_cb) {
        env->DeleteGlobalRef(dataChannelCallbacks[handle].track_cb);
    }

    dataChannelCallbacks[handle].track_cb = env->NewGlobalRef(callback);
    return rtcSetTrackCallback(handle, trackCallback);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetLocalDescription(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jint handle,
                                                                                 jstring type) {
    if (!type) {
        rtcSetLocalDescription(handle, nullptr);
        return 0;
    }
    const char *typeStr = env->GetStringUTFChars(type, nullptr);
    int result = rtcSetLocalDescription(handle, typeStr);
    env->ReleaseStringUTFChars(type, typeStr);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeSetRemoteDescription(JNIEnv *env,
                                                                                  jobject thiz,
                                                                                  jint handle,
                                                                                  jstring sdp,
                                                                                  jstring type) {
    const char *sdpStr = env->GetStringUTFChars(sdp, nullptr);
    const char *typeStr = env->GetStringUTFChars(type, nullptr);
    int result = rtcSetRemoteDescription(handle, sdpStr, typeStr);
    env->ReleaseStringUTFChars(sdp, sdpStr);
    env->ReleaseStringUTFChars(type, typeStr);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_addRemoteCandidate(JNIEnv *env, jobject thiz,
                                                                          jint handle,
                                                                          jstring candidate,
                                                                          jstring mid) {
    const char *candidateStr = env->GetStringUTFChars(candidate, nullptr);
    const char *midStr = env->GetStringUTFChars(mid, nullptr);
    int result = rtcAddRemoteCandidate(handle, candidateStr, midStr);
    env->ReleaseStringUTFChars(candidate, candidateStr);
    env->ReleaseStringUTFChars(mid, midStr);
    return result;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetLocalDescription(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jint handle) {
    char buffer[8192];
    int result = rtcGetLocalDescription(handle, buffer, sizeof(buffer));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(buffer);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetRemoteDescription(JNIEnv *env,
                                                                                  jobject thiz,
                                                                                  jint handle) {
    char buffer[8192];
    int result = rtcGetRemoteDescription(handle, buffer, sizeof(buffer));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(buffer);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetLocalDescriptionType(JNIEnv *env,
                                                                                     jobject thiz,
                                                                                     jint handle) {
    char buffer[8192];
    int result = rtcGetLocalDescriptionType(handle, buffer, sizeof(buffer));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(buffer);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetRemoteDescriptionType(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint handle) {
    char buffer[8192];
    int result = rtcGetRemoteDescriptionType(handle, buffer, sizeof(buffer));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(buffer);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetLocalAddress(JNIEnv *env,
                                                                             jobject thiz,
                                                                             jint handle) {
    char buffer[8192];
    int result = rtcGetLocalAddress(handle, buffer, sizeof(buffer));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(buffer);
}
extern "C"
JNIEXPORT jstring JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetRemoteAddress(JNIEnv *env,
                                                                              jobject thiz,
                                                                              jint handle) {
    char buffer[8192];
    int result = rtcGetRemoteAddress(handle, buffer, sizeof(buffer));
    if (result < 0) {
        return nullptr;
    }
    return env->NewStringUTF(buffer);
}
extern "C"
JNIEXPORT jobject JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetSelectedCandidatePair(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint handle) {
    char local[8192];
    char remote[8192];
    int result = rtcGetSelectedCandidatePair(handle, local, sizeof(local), remote, sizeof(remote));
    if (result < 0) {
        return nullptr;
    }

    jclass clazz = env->FindClass("kotlin/Pair");
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "(Ljava/lang/Object;Ljava/lang/Object;)V");
    jstring localStr = env->NewStringUTF(local);
    jstring remoteStr = env->NewStringUTF(remote);
    jobject pair = env->NewObject(clazz, constructor, localStr, remoteStr);
    env->DeleteLocalRef(localStr);
    env->DeleteLocalRef(remoteStr);
    return pair;
}
extern "C"
JNIEXPORT jlong JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetMaximumDataChannelStream(
        JNIEnv *env, jobject thiz, jint handle) {
    return rtcGetMaxDataChannelStream(handle);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeGetRemoteMaxMessageSize(JNIEnv *env,
                                                                                     jobject thiz,
                                                                                     jint handle) {
    return rtcGetRemoteMaxMessageSize(handle);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeCreateDataChannel(JNIEnv *env,
                                                                               jobject thiz,
                                                                               jint handle,
                                                                               jstring label) {
    const char *labelStr = env->GetStringUTFChars(label, nullptr);
    int result = rtcCreateDataChannel(handle, labelStr);
    env->ReleaseStringUTFChars(label, labelStr);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeCreateDataChannelEx(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jint handle,
                                                                                 jstring label,
                                                                                 jlong init_handle) {
    rtcDataChannelInit *init = reinterpret_cast<rtcDataChannelInit *>(init_handle);
    if (!init) {
        return -1;
    }
    const char *labelStr = env->GetStringUTFChars(label, nullptr);
    int result = rtcCreateDataChannelEx(handle, labelStr, init);
    env->ReleaseStringUTFChars(label, labelStr);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_RtcPeerConnection_nativeAddTrack(JNIEnv *env, jobject thiz,
                                                                      jint handle,
                                                                      jstring media_description_sdp) {
    const char *sdp = env->GetStringUTFChars(media_description_sdp, nullptr);
    int result = rtcAddTrack(handle, sdp);
    env->ReleaseStringUTFChars(media_description_sdp, sdp);
    return result;
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_preload(JNIEnv *env, jobject thiz) {
    rtcPreload();
//    rtcInitLogger(RTC_LOG_VERBOSE, [](rtcLogLevel level, const char *message) {
//        __android_log_print(ANDROID_LOG_INFO, "LibDatachannel", "%s", message);
//    });
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_cleanup(JNIEnv *env, jobject thiz) {
    rtcCleanup();
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_sendMessage(JNIEnv *env, jobject thiz, jint handle,
                                                     jbyteArray message) {
    jbyte *bytes = env->GetByteArrayElements(message, nullptr);
    int size = env->GetArrayLength(message);
    int result = rtcSendMessage(handle, reinterpret_cast<const char *>(bytes), size);
    env->ReleaseByteArrayElements(message, bytes, JNI_ABORT);
    return result;
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_close(JNIEnv *env, jobject thiz, jint handle) {
    return rtcClose(handle);
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_isOpen(JNIEnv *env, jobject thiz, jint handle) {
    return rtcIsOpen(handle);
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_isClosed(JNIEnv *env, jobject thiz, jint handle) {
    return rtcIsClosed(handle);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_getMaxMessageSize(JNIEnv *env, jobject thiz, jint handle) {
    return rtcMaxMessageSize(handle);
}
extern "C"
JNIEXPORT jint JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_getBufferedAmount(JNIEnv *env, jobject thiz, jint handle) {
    return rtcGetBufferedAmount(handle);
}
extern "C"
JNIEXPORT void JNICALL
Java_uk_frontendlabs_nativedatachannels_Rtc_free(JNIEnv *env, jobject thiz, jint handle) {
    rtcDelete(handle);
}