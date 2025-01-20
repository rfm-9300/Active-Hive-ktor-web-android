package example.com.web.pages.homePage.eventTab

import example.com.data.db.event.EventRepository
import example.com.web.components.post.event
import kotlinx.coroutines.runBlocking
import kotlinx.html.*

fun HTML.eventTab(
    eventRepository: EventRepository
) {
    val events = runBlocking {
        try {
            eventRepository.getAllEvents()
        } catch (e: Exception) {
            println("Error fetching events: ${e.message}")
            emptyList()
        }
    }
    body{
        div(classes = "w-full py-4") {
            events.forEach { event ->
                event(event)
            }
        }
    }
}