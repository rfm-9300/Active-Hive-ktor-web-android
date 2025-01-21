import io.ktor.utils.io.core.*
import java.io.File
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageFileHandler {
    private const val UPLOAD_DIR = "files/uploads/images"

    init {
        // Create upload directory if it doesn't exist
        File(UPLOAD_DIR).mkdirs()
    }

    suspend fun saveImage(fileBytes: ByteArray, originalFileName: String): String {
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
}