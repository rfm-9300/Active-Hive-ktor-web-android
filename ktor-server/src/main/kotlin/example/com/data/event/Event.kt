package example.com.data.event

import example.com.data.ImageEntity
import example.com.data.user.User
import example.com.data.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.util.*
import java.time.LocalDateTime

@Serializable
data class Event(
    val id: Int? = null,
    val title: String,
    val headerImage: ImageEntity? = null,
    val description: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val location: String,
    val attendees: List<User> = emptyList(),
    val organizer: User,
    val isPublic: Boolean = true,
    val isCancelled: Boolean = false,
    val isArchived: Boolean = false,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
