package uk.frontendlabs.nativedatachannels

import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.io.path.absolutePathString
import kotlin.io.path.outputStream

expect fun isAndroid(): Boolean

object Rtc {
    external fun preload()
    external fun cleanup()

    external fun setOpenCallback(handle: Int, callback: () -> Unit)
    external fun setClosedCallback(handle: Int, callback: () -> Unit)
    external fun setErrorCallback(handle: Int, callback: (String) -> Unit)
    external fun setMessageCallback(handle: Int, callback: (ByteArray) -> Unit)
    external fun setBufferedAmountLowCallback(handle: Int, callback: () -> Unit)
    external fun setAvailableCallback(handle: Int, callback: () -> Unit)
    external fun bufferedAmount(handle: Int): Long
    external fun availableAmount(handle: Int): Long

    external fun sendMessage(handle: Int, message: ByteArray): Int
    external fun close(handle: Int): Int
    external fun isOpen(handle: Int): Boolean
    external fun isClosed(handle: Int): Boolean
    external fun getMaxMessageSize(handle: Int): Int
    external fun getBufferedAmount(handle: Int): Int
    external fun setBufferedAmountLowThreshold(handle: Int, amount: Int): Int
    external fun free(handle: Int)

    init {
        runBlocking {
            val os = System.getProperty("os.name")

            val libNames = when {
                os.contains("Mac OS X") -> listOf("macos/libnativedatachannels.dylib" to ".dylib")
                os.contains("Linux") && !isAndroid() -> listOf("linux/libnativedatachannels.so" to ".so")
                os.contains("Windows") -> listOf(
                    "windows/libcrypto-3-x64.dll" to ".dll",
                    "windows/libssl-3-x64.dll" to ".dll",
                    "windows/juice.dll" to ".dll",
                    "windows/datachannel.dll" to ".dll",
                    "windows/libnativedatachannels.dll" to ".dll",
                )
                else -> emptyList()
            }

            libNames.forEach { libName ->
                if (libName.first.isNotEmpty()) {
                    val resource = loadResource(libName.first)
                    val path = kotlin.io.path.createTempFile("tmp", libName.second)
                    path.toFile().deleteOnExit()

                    path.outputStream().use { it.write(resource) }
                    System.load(path.absolutePathString())
                }
            }

            if (libNames.isEmpty()) {
                System.loadLibrary("nativedatachannels")
            }
        }
    }

    private fun loadResource(path: String): ByteArray {
        val classLoader = Thread.currentThread().contextClassLoader ?: (::RtcDataChannel.javaClass.classLoader)
        val resource = classLoader.getResourceAsStream(path)
        if (resource != null) {
            return resource.readBytes()
        } else {
            throw IOException("Cannot find resource at: $path")
        }
    }
}