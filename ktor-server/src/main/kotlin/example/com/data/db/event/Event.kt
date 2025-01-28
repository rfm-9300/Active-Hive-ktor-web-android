package example.com.data.db.event

import example.com.data.utils.ImageEntity
import example.com.data.db.user.User
import example.com.data.db.user.UserDao
import example.com.data.db.user.UserTable
import example.com.data.db.user.toUser
import example.com.data.utils.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

@Serializable
data class Event(
    val id: Int? = null,
    val title: String,
    val headerImagePath: String,
    val description: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val location: String,
    val attendees: List<User> = emptyList(),
    val organizerId: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val organizerName: String = ""
)

object EventTable : IntIdTable("event")  {
    val title = varchar("title", 255)
    val description = text("description")
    val date = datetime("date")
    val location = varchar("location", 255)
    val organizerId = reference("organizer_id", UserTable)
    val headerImagePath = varchar("header_image_path", 255)
}

object EventAttendeeTable : Table("event_attendee") {
    val event = reference("event_id", EventTable)
    val user = reference("user_id", UserTable)
    override val primaryKey = PrimaryKey(event, user)
}

class EventDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EventDao>(EventTable)

    var title by EventTable.title
    var description by EventTable.description
    var date by EventTable.date
    var location by EventTable.location
    var organizer by UserDao referencedOn EventTable.organizerId
    var headerImagePath by EventTable.headerImagePath

    val attendees by UserDao via EventAttendeeTable
}

fun EventDao.toEvent() = Event(
    id = id.value,
    title = title,
    description = description,
    date = date,
    location = location,
    organizerId = organizer.id.value,
    attendees = attendees.map { it.toUser() },
    headerImagePath = headerImagePath,
    organizerName = organizer.name
)