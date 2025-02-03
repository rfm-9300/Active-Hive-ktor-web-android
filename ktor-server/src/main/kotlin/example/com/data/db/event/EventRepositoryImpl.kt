package example.com.data.db.event

import example.com.data.db.user.UserDao
import example.com.data.db.user.UserProfileDao
import example.com.data.db.user.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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

    override suspend fun getEvent(eventId: Int): Event? = suspendTransaction {
        EventDao.findById(eventId)?.toEvent()
    }

    override suspend fun deleteEvent(eventId: Int): Boolean = suspendTransaction {
        EventTable.deleteWhere { EventTable.id eq eventId } > 0
    }

    override suspend fun updateEvent(event: Event): Boolean = suspendTransaction {
        try {
            EventTable.update({ EventTable.id eq event.id }) {
                it[title] = event.title
                it[description] = event.description
                it[date] = event.date
                it[location] = event.location
                it[headerImagePath] = event.headerImagePath
            } > 0
        } catch (e: Exception) {
            false
        }
    }
}