package kz.seasky.tms.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kz.seasky.tms.utils.FILE_DEFAULT_SIZE
import java.io.InputStream
import java.io.OutputStream

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}

/**
 * @return true if file size is less than [FILE_DEFAULT_SIZE], false otherwise
 */
fun InputStream.fileSize(): Boolean {
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var bytesCopied = 0L
    while (true) {
        if (bytesCopied > FILE_DEFAULT_SIZE) {
            return false
        }
        val bytes = read(buffer).takeIf { it >= 0 } ?: break
        bytesCopied += bytes
    }
    return true
}

fun Long.asKiB() = this / 1024

fun Long.asMiB() = this / 1048576