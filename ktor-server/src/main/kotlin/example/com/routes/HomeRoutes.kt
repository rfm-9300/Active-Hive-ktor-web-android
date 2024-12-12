package example.com.routes

import example.com.data.db.event.Event
import example.com.data.db.event.EventService
import example.com.data.db.user.User
import example.com.data.requests.CreateEventRequest
import example.com.views.event.eventPage
import example.com.views.home.homePage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import java.io.File
import java.time.LocalDateTime

fun Route.homeRoutes(){
    get("/") {
        call.respondHtml(HttpStatusCode.OK){
            homePage()
        }
    }
    get("/feed") {
        call.respondHtml{
            body {
                h1 { +"Feed" }
            }
        }
    }
    get("/home") {
        call.respondHtml{
            homePage()
        }
    }
    get("/events") {
        call.respondHtml{
            eventPage()
        }
    }

    post("/events/create") {

        val request = kotlin.runCatching { call.receiveNullable<CreateEventRequest>() }.getOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)


        val eventRepository = EventService()
        val event = Event(
            title = request.title,
            description = request.description,
            date = LocalDateTime.now(),
            location = request.location,
            organizerId = 2
        )

        eventRepository.addEvent(event)
    }

    staticFiles("/resources", File("files")){
        default("htmx.js")
    }
}
