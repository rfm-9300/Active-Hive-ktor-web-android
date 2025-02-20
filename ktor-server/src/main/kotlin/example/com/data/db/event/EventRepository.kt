package example.com.data.db.event

import example.com.data.db.user.UserProfile

interface EventRepository {
    suspend fun addEvent(event: Event) : Int?
    suspend fun getAllEvents(): List<Event>
    suspend fun getEvent(eventId: Int): Event?
    suspend fun deleteEvent(eventId: Int): Boolean
    suspend fun updateEvent(event: Event): Boolean
    suspend fun joinEvent(eventId: Int, userId:Int): Boolean
    suspend fun getEventAttendees(eventId: Int): List<UserProfile>
    suspend fun deleteEventAttendees(eventId: Int): Int
}