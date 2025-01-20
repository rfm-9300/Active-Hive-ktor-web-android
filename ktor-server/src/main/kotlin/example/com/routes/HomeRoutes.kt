package example.com.routes

import example.com.data.db.event.Event
import example.com.data.db.event.EventRepository
import example.com.data.db.event.EventRepositoryImpl
import example.com.data.requests.CreateEventRequest
import example.com.data.responses.CreateEventResponse
import example.com.web.pages.homePage.homePage
import example.com.web.components.topbar.profileMenu
import example.com.web.pages.homePage.eventTab.eventTab
import example.com.web.pages.homePage.homeTab.homeTab
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.coroutines.delay
import kotlinx.html.body
import java.io.File
import java.time.LocalDateTime

fun Route.homeRoutes(
    likeEventManager: LikeEventManager,
    eventRepository: EventRepository
){
    get("/") {
        call.respondHtml(HttpStatusCode.OK){
            homePage()
        }
    }
    get("/home/events-tab") {
        call.respondHtml(HttpStatusCode.OK){
            eventTab(
                eventRepository = eventRepository
            )
        }
    }
    get("/home/home-tab") {
        val cookies = call.response.cookies
        println("Cookies: $cookies")
        call.respondHtml(HttpStatusCode.OK){
            body {
                homeTab()
            }
        }
    }
    get("/post/like") {
        val cookies = call.request.cookies
        println("Cookies: $cookies")
        val postId = call.parameters["postId"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        // Emit event
        val likesCount = 10
        //likeEventManager.emitLike(postId, likesCount)
        call.respond(HttpStatusCode.OK)
    }

    sse ("/home/sse") {
        send(ServerSentEvent(data = "sse connected"))
        try {
            likeEventManager.likeEvents.collect { event ->
                send(ServerSentEvent(
                    data = "Post ${event.postId} has ${event.likesCount} likes",
                    event = "like-update",
                    id = event.postId.toString()
                ))
            }
        } catch (e: Exception) {
            send(ServerSentEvent(data= "Error: ${e.message}", event = "error"))
        }
    }

    authenticate {
        get("/home/user-info") {
            val principal = call.principal<JWTPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
            val userId = principal.getClaim("userId", String::class) ?: return@get call.respond(HttpStatusCode.Unauthorized)
            call.respondHtml(HttpStatusCode.OK) {
                body {
                    profileMenu()
                }
            }
        }
    }

    authenticate {
        post("/events/create") {

            val request = kotlin.runCatching { call.receiveNullable<CreateEventRequest>() }.getOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)

            val event = Event(
                title = request.title,
                description = request.description,
                date = LocalDateTime.now(),
                location = request.location,
                organizerId = 1
            )

            eventRepository.addEvent(event)

            call.respond(
                HttpStatusCode.Created,
                CreateEventResponse(
                    message = "Event created successfully"
                )
            )

        }
    }

    staticFiles("/resources", File("files")){
        default("htmx.js")
    }
}
