package example.com.routes

import example.com.data.db.event.Event
import example.com.data.db.event.EventRepository
import example.com.data.db.event.EventService
import example.com.data.db.user.User
import example.com.views.event.eventPage
import example.com.views.home.homePage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import java.io.File
import java.time.LocalDateTime

fun Route.homeRoute(){
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
        val params = call.receiveParameters()
        val title = params["title"]
        val description = params["description"]
        var dateTime = params["date"]
        val location = params["location"]

        val date = LocalDateTime.parse(dateTime)

        val eventRepository = EventService()
        eventRepository.addEvent(Event(title = title!!, description = description!!, date = date, location = location!!, organizer = User(
            id = 1,
            username = "admin",
            password = "admin",
            salt = "salt"
            )))
    }
    staticFiles("/resources", File("files")){
        default("htmx.js")
    }
}
