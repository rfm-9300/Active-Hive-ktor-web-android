package example.com.data.event

class PostgresEventRepository: EventRepository {
    override suspend fun addEvent(event: Event): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAllEvents(): List<Event> {
        TODO("Not yet implemented")
    }
}