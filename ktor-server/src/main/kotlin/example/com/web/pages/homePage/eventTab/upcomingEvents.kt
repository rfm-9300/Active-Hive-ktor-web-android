package example.com.web.pages.homePage.eventTab

import example.com.data.db.event.EventRepository
import example.com.plugins.Logger
import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.eventFilterTag
import example.com.web.components.post.event
import example.com.web.components.projectButton
import example.com.web.components.svgIcon
import example.com.web.loadJs
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import java.time.LocalDateTime

fun HtmlBlockTag.upcomingEvents(
    eventRepository: EventRepository,
    isAdminRequest: Boolean
) {
    // Fetch both upcoming and past events
    val (upcomingEvents, pastEvents) = runBlocking {
        try {
            val allEvents = eventRepository.getAllEvents()
            val now = LocalDateTime.now()
            val upcoming = allEvents.filter { it.date > now }.sortedBy { it.date }
            val past = allEvents.filter { it.date <= now }.sortedByDescending { it.date }
            Pair(upcoming, past)
        } catch (e: Exception) {
            Logger.d("Error fetching events: $e")
            Pair(emptyList(), emptyList())
        }
    }
    
    div(classes = "w-full py-4 flex flex-col items-center") {
        // Header section with title and Create Event button
        div(classes = "w-[90%] mx-auto mb-8") {
            // Header with title and Create Event button
            div(classes = "flex justify-between items-center mb-6") {
                h1(classes = "text-2xl font-bold text-blue-700") {
                    +"Events"
                }
                button(classes = "flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-5 py-2.5 rounded-lg shadow-md transition-all duration-300") {
                    attributes["hx-get"] = Routes.Ui.Event.CREATE
                    attributes["hx-target"] = "#main-content"
                    svgIcon(SvgIcon.CALENDAR, "w-5 h-5 mr-1")
                    +"Create Event"
                }
            }
            
            // Filter tabs in a more prominent tab-like design
            div(classes = "flex border-b border-blue-200") {
                div(classes = "flex space-x-1") {
                    eventFilterTag("Upcoming", "upcoming", active = true)
                    eventFilterTag("Past", "past", active = false)
                }
            }
        }

        // Events list container
        div(classes = "w-[90%] mx-auto") {
            // Upcoming Events Section
            div(classes = "events-content") {
                id = "upcoming-content"
                if (upcomingEvents.isEmpty()) {
                    emptyState("upcoming")
                } else {
                    upcomingEvents.forEach { event ->
                        event(event, isAdminRequest)
                    }
                }
            }

            // Past Events Section
            div(classes = "events-content hidden") {
                id = "past-content"
                if (pastEvents.isEmpty()) {
                    emptyState("past")
                } else {
                    pastEvents.forEach { event ->
                        event(event, isAdminRequest)
                    }
                }
            }
        }
        
        // Load both the events page JavaScript and the all-events script
        loadJs("events-page")
        loadJs("event/all-events")
    }
}

private fun HtmlBlockTag.emptyState(type: String) {
    div(classes = "flex flex-col items-center justify-center py-12 text-center bg-white/50 rounded-xl border border-blue-100 shadow-sm") {
        svgIcon(SvgIcon.CALENDAR, "w-16 h-16 text-blue-200 mb-4")
        h3(classes = "text-xl font-semibold text-blue-800 mb-2") { 
            +if (type == "upcoming") "No upcoming events" else "No past events" 
        }
        p(classes = "text-blue-600 mb-6") { 
            +if (type == "upcoming") "Be the first to create an event!" else "Check back later for past events" 
        }
        if (type == "upcoming") {
            projectButton(
                text = "Create New Event", 
                hxGet = Routes.Ui.Event.CREATE, 
                hxTarget = "#main-content",
                extraClasses = "bg-blue-600 hover:bg-blue-700 text-white"
            )
        }
    }
}