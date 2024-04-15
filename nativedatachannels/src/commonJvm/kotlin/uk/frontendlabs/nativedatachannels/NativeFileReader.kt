package uk.frontendlabs.nativedatachannels

import com.sun.jna.Pointer

/**
 * This class is NOT thread-safe!!!
 */
class NativeFileReader(path: String) {
    private val native = LibDatachannels.INSTANCE
    private val pointer: Pointer = native.fopen(path, "rb") ?: throw RuntimeException("Failed to open file")
    private val buffer: Pointer? = native.malloc(DEFAULT_BUFFER_SIZE)

    /**
     * Don't forget to free the returned native memory!!!
     */
    fun read(): Pair<Pointer, Int>? {
        if (buffer == null) throw RuntimeException("Buffer is not valid!")
        val read = native.fread(buffer, 1, DEFAULT_BUFFER_SIZE, pointer)
        if (read <= 0) {
            native.fclose(pointer)
            native.free(buffer)
            return null
        }
        return buffer.share(0, read.toLong()) to read
    }

    fun close() {
        native.fclose(pointer)
        buffer?.let { native.free(buffer) }
    }
}