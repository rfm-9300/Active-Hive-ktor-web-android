package example.com.data.db.event

interface EventRepository {
    suspend fun addEvent(event: Event) : Int?
    suspend fun getAllEvents(): List<Event>
}