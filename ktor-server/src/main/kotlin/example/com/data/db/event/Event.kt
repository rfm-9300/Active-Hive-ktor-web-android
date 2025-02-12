package example.com.data.db.event

import example.com.data.db.user.*
import example.com.data.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

@Serializable
data class Event(
    val id: Int? = null,
    val title: String,
    val headerImagePath: String = "",
    val description: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val location: String,
    var attendees: List<UserProfile> = emptyList(),
    val organizerId: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val organizerName: String = "",
    val maxAttendees: Int
)

@Serializable
data class EventAttendee(
    val event: Int,
    val user: UserProfile,
    @Serializable(with = LocalDateTimeSerializer::class)
    val joinedAt: LocalDateTime
)

object EventTable : IntIdTable("event")  {
    val title = varchar("title", 255)
    val description = text("description")
    val date = datetime("date")
    val location = varchar("location", 255)
    val organizerId = reference("organizer_id", UserProfilesTable)
    val headerImagePath = varchar("header_image_path", 255)
    val maxAttendees = integer("max_attendees")
}

object EventAttendeeTable : Table("event_attendee") {
    val eventId = reference("event_id", EventTable)
    val userId = reference("user_id", UserTable)
    val joinedAt = datetime("joined_at").default(LocalDateTime.now())
    override val primaryKey = PrimaryKey(eventId, userId)
}
