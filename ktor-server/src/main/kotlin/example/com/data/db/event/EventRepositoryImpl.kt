package example.com.data.db.event

import example.com.data.db.user.*
import example.com.plugins.Logger
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
        val eventsList = mutableListOf<Event>()
        Logger.d("List: $eventsList")
        val events = EventTable.selectAll().map {
            Event(
                id = it[EventTable.id].value,
                title = it[EventTable.title],
                description = it[EventTable.description],
                date = it[EventTable.date],
                location = it[EventTable.location],
                headerImagePath = it[EventTable.headerImagePath],
                attendees = emptyList(),
                organizerId = it[EventTable.organizerId].value,
                organizerName = "Rodrigo"
            )
        }
        Logger.d("Events: $events")
        events.forEach { event ->
            // Fetch attendees for each event
            val attendeesQuery = EventAttendeeTable.select { EventAttendeeTable.event eq event.id }
            Logger.d("Attendees Query: $attendeesQuery")
            attendeesQuery.forEach { attendeeRow ->
                val userId = attendeeRow[EventAttendeeTable.user].value
                val userProfile = UserProfilesTable.select { UserProfilesTable.userId eq userId }
                    .map { row ->
                        UserProfile(
                            id = row[UserProfilesTable.id].value,
                            firstName = row[UserProfilesTable.firstName],
                            lastName = row[UserProfilesTable.lastName],
                            email = row[UserProfilesTable.email],
                            phone = row[UserProfilesTable.phone]
                        )
                    }
                    .firstOrNull()
                Logger.d("User: $userProfile")
                userProfile?.let { user ->
                    // Reassign the list with the new user added using the + operator
                    event.attendees = event.attendees + user
                }
            }
            eventsList.add(event)
            Logger.d("Event: $event")
        }

        eventsList
    }

    override suspend fun getEvent(eventId: Int): Event? = suspendTransaction {
        val event = EventTable.select { EventTable.id eq eventId }
            .map {
                Event(
                    id = it[EventTable.id].value,
                    title = it[EventTable.title],
                    description = it[EventTable.description],
                    date = it[EventTable.date],
                    location = it[EventTable.location],
                    headerImagePath = it[EventTable.headerImagePath],
                    attendees = emptyList(),
                    organizerId = it[EventTable.organizerId].value,
                    organizerName = "Rodrigo"
                )
            }
            .firstOrNull()
        val attendeesQuery = EventAttendeeTable.select { EventAttendeeTable.event eq eventId }
        attendeesQuery.forEach { attendeeRow ->
            val userId = attendeeRow[EventAttendeeTable.user].value
            val userProfile = UserProfilesTable.select { UserProfilesTable.userId eq userId }
                .map { row ->
                    UserProfile(
                        id = row[UserProfilesTable.id].value,
                        firstName = row[UserProfilesTable.firstName],
                        lastName = row[UserProfilesTable.lastName],
                        email = row[UserProfilesTable.email],
                        phone = row[UserProfilesTable.phone]
                    )
                }
                .firstOrNull()
            userProfile?.let { user ->
                if (event != null) {
                    event.attendees += user
                }
            }
        }
        event
    }

    override suspend fun deleteEvent(eventId: Int): Boolean = suspendTransaction {
        EventTable.deleteWhere { id eq eventId } > 0
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

    override suspend fun joinEvent(eventId: Int, userId: Int): Boolean = suspendTransaction {
        try {
            EventAttendeeTable.insert {
                it[event] = eventId
                it[user] = userId
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}