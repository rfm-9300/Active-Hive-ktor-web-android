package example.com.data.db.event

interface EventRepository {
    suspend fun addEvent(event: Event) : Int?
    suspend fun getAllEvents(): List<Event>
    suspend fun getEvent(eventId: Int): Event?
    suspend fun deleteEvent(eventId: Int): Boolean
}