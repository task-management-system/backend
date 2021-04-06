package kz.seasky.tms.utils

import kotlinx.uuid.UUID
import java.io.File

class FileHelper {
    companion object {
        const val KEY_SUCCESS = 's'
        const val KEY_ERROR = 'e'
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
}

fun Long.asMib() = this / (1024 * 1024)