package uk.frontendlabs.nativedatachannels

import kotlinx.coroutines.runBlocking
import java.io.IOException
import kotlin.io.path.absolutePathString
import kotlin.io.path.outputStream

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

            val libName = when (os) {
                "Mac OS X" -> "libnativedatachannels.dylib" to ".dylib"
                "Linux" -> "libnativedatachannels.so" to ".so"
                "Windows" -> "nativedatachannels.dll" to ".dll"
                else -> "" to ""
            }

            if (libName.first.isNotEmpty()) {
                val resource = loadResource(libName.first)
                val path = kotlin.io.path.createTempFile("libnativedatachannels", libName.second)
                path.toFile().deleteOnExit()

                path.outputStream().use { it.write(resource) }
                System.load(path.absolutePathString())
            } else {
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