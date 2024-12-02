package example.com.data.event

interface EventRepository {
    suspend fun addEvent(event: Event) : Boolean
    suspend fun getAllEvents(): List<Event>
}