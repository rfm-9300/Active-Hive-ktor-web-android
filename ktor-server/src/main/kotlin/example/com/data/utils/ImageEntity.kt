package example.com.data.utils

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ImageEntity(
    val id: Int? = null,
    val name: String,
    val imageData: ByteArray?, // For BYTEA
    val contentType: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val uploadedAt: LocalDateTime = LocalDateTime.now()
) {
    // Implement equals and hashCode for ByteArray
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}