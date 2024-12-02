package example.com.data.event


import example.com.data.db.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object EventTable : IntIdTable("event")  {
    val title = varchar("title", 255)
    val description = text("description")
    val date = datetime("date")
    val location = varchar("location", 255)
    val attendees = integer("attendees").default(0)
    val organizer = reference("organizer_id", UserTable)
}