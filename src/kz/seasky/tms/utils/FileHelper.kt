package kz.seasky.tms.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlinx.uuid.UUID
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class FileHelper {
    private val directory = File(FILE_ROOT_DIR)

    companion object {
        const val KEY_SUCCESS = "success"
        const val KEY_ERROR = "error"

        /** Equals to 4MiB */
        const val YIELD_SIZE = 4 * 1024 * 1024
    }

    /**
     * Creates directory, if not exists, by passed [taskId] and generates unique filename.
     *
     * @param taskId the name of directory
     * @param filename the original filename of file
     * @param bytes the byte array of file
     * @return the new [File]
     */
    fun prepareFile(taskId: UUID, filename: String, bytes: ByteArray): File {
        val dirName = "$FILE_ROOT_DIR/$taskId"
        val dir = File(dirName)
        if (!dir.exists()) dir.mkdir()

        val filenameHashCode = "fhc${filename.hashCode()}"
        val bytesHashCode = "bhc${bytes.size}"
        val extension = ".${filename.substringAfterLast('.')}"

        return File(dirName, filenameHashCode + bytesHashCode + extension)
    }

    /**
     * Read all bytes from [input] and validate it size so that it is not less than or equal to [fileSize]
     *
     * @param input the input stream to be read
     * @return empty byte array if file size is more than [fileSize], filled byte array otherwise
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun readBytesAndValidate(input: InputStream, fileSize: Long = FILE_DEFAULT_SIZE): ByteArray {
        return withContext(Dispatchers.IO) {
            val output = ByteArrayOutputStream(DEFAULT_BUFFER_SIZE)
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesCopied = 0L
            var bytesAfterYield = 0L
            while (true) {
                if (bytesCopied > fileSize) {
                    return@withContext ByteArray(0)
                }
                val bytes = input.read(buffer).takeIf { it >= 0 } ?: break
                output.write(buffer, 0, bytes)
                if (bytesAfterYield >= YIELD_SIZE) {
                    yield()
                    bytesAfterYield %= YIELD_SIZE
                }
                bytesCopied += bytes
                bytesAfterYield += bytes
            }
            return@withContext output.toByteArray()
        }
    }

    suspend fun usedSpace(): Long = withContext(Dispatchers.IO) {
        return@withContext directory.totalSpace - directory.usableSpace
    }

    suspend fun availableSpace(): Long = withContext(Dispatchers.IO) {
        return@withContext directory.usableSpace
    }
}

fun Long.asMiB() = this / (1024 * 1024)