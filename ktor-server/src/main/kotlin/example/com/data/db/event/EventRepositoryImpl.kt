package example.com.data.db.event

import example.com.data.db.user.UserDao
import example.com.data.db.user.UserProfileDao
import example.com.data.db.user.suspendTransaction

class EventRepositoryImpl: EventRepository {
    override suspend fun addEvent(event: Event): Boolean = suspendTransaction {
        val eventId = EventDao.new {
            title = event.title
            description = event.description
            date = event.date
            location = event.location
            organizer = UserProfileDao[event.organizerId]
            headerImagePath = event.headerImagePath
        }.id.value

        // If you want to add attendees
        /*event.attendees.forEach { attendee ->
            EventAttendeeTable.insert {
                it[this.event] = eventId
                it[user] = attendee.id
            }
        }*/

        true
    }

    override suspend fun getAllEvents(): List<Event> = suspendTransaction {
        EventDao.all().map { it.toEvent() }
    }
}