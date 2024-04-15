package uk.frontendlabs.nativedatachannels

import com.sun.jna.Platform.isAndroid
import com.sun.jna.Platform.isLinux
import com.sun.jna.Platform.isMac
import com.sun.jna.Platform.isWindows
import kotlinx.coroutines.runBlocking

object Rtc {
    fun load() {
        println("OS is Linux: ${isLinux()}")
        println("OS is Windows: ${isWindows()}")
        println("OS is MacOS: ${isMac()}")
        println("OS is Android: ${isAndroid()}")
        runBlocking {
            if (isMac()) {
                LibDatachannels.path = "nativedatachannels"
            } else if (isLinux() || isAndroid()) {
                LibDatachannels.path = "datachannel"
            } else if (isWindows()) {
                LibDatachannels.path = "datachannel.dll"
            }
        }

        LibDatachannels.INSTANCE.rtcPreload()
    }
}