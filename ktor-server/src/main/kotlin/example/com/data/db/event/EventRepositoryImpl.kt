package example.com.data.db.event

import example.com.data.db.user.UserDao
import example.com.data.db.user.UserProfileDao
import example.com.data.db.user.suspendTransaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

class EventRepositoryImpl: EventRepository {
    override suspend fun addEvent(event: Event): Int? = suspendTransaction {
        val organizer = UserProfileDao.findById(event.organizerId) ?: return@suspendTransaction null

        try {
            val eventId = EventTable.insertAndGetId {
                it[title] = event.title
                it[description] = event.description
                it[date] = event.date
                it[location] = event.location
                it[organizerId] = organizer.id.value
                it[headerImagePath] = event.headerImagePath
            }.value
            eventId // Return the generated event ID
        } catch (e: Exception) {
            null // Return null if the transaction fails
        }
    }

    override suspend fun getAllEvents(): List<Event> = suspendTransaction {
        EventDao.all().map { it.toEvent() }
    }
}