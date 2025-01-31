import io.ktor.utils.io.core.*
import java.io.File
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

object ImageFileHandler {
    private const val UPLOAD_DIR = "files/uploads/images"

    init {
        // Create upload directory if it doesn't exist
        File(UPLOAD_DIR).mkdirs()
    }

    suspend fun saveImage(fileBytes: ByteArray, originalFileName: String): String {
        val existingImages = File(UPLOAD_DIR).listFiles()?.toList() ?: emptyList()
        return withContext(Dispatchers.IO) {
            try {
                // Generate unique filename while preserving original extension
                val extension = originalFileName.substringAfterLast('.', "")
                val fileName = "${UUID.randomUUID()}.$extension"
                val file = File("$UPLOAD_DIR/$fileName")

                // Write the file
                file.writeBytes(fileBytes)

                fileName // Return the generated filename
            } catch (e: Exception) {
                throw ImageSaveException("Failed to save image: ${e.message}")
            }
        }
    }

    fun getImageFile(fileName: String): File {
        return File("$UPLOAD_DIR/$fileName")
    }

    class ImageSaveException(message: String) : Exception(message)

    fun hashFile(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        return file.inputStream().use { inputStream ->
            val byteArray = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(byteArray).also { bytesRead = it } != -1) {
                digest.update(byteArray, 0, bytesRead)
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        }
    }

    fun isDuplicateImage(newFile: File, existingFiles: List<File>): Boolean {
        val newFileHash = hashFile(newFile)
        return existingFiles.any { hashFile(it) == newFileHash }
    }
}