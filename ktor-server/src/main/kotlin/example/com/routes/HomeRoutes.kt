package example.com.routes

import ImageFileHandler
import example.com.data.db.event.Event
import example.com.data.db.event.EventRepository
import example.com.data.requests.CreateEventRequest
import example.com.data.responses.CreateEventResponse
import example.com.data.utils.LikeEventManager
import example.com.web.pages.homePage.homePage
import example.com.web.components.topbar.profileMenu
import example.com.web.pages.homePage.eventTab.createEvent
import example.com.web.pages.homePage.eventTab.eventTab
import example.com.web.pages.homePage.homeTab.homeTab
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.html.body
import kotlinx.io.readByteArray
import java.io.File
import java.time.LocalDateTime
import kotlin.text.toByteArray

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
        val cookies = call.request.cookies.rawCookies
        val cookieNames = call.request.cookies["authToken"]
        println("Cookies: $cookieNames")
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
            //val request = kotlin.runCatching { call.receiveNullable<CreateEventRequest>() }.getOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
            //println("RequestAAAAAA: $request")
            try {
                val multiPart = call.receiveMultipart()
                var source = ""
                var title = ""
                var description = ""
                var date = ""
                var location = ""
                var image = ""

                multiPart.forEachPart {
                    when(it) {
                        is PartData.FormItem -> {
                            when(it.name) {
                                "source" -> source = it.value
                                "title" -> title = it.value
                                "description" -> description = it.value
                                "date" -> date = it.value
                                "location" -> location = it.value
                            }
                        }
                        is PartData.FileItem -> {
                            if (it.name == "image") {
                                val fileName = it.originalFileName ?: "unnamed.jpg"
                                val fileBytes = it.provider().readRemaining().readByteArray()
                                image = ImageFileHandler.saveImage(fileBytes, fileName)
                            }
                        }
                        else -> {}
                    }
                    it.dispose
                }

                val event = Event(
                    title = title,
                    description = description,
                    date = LocalDateTime.now(),
                    location = location,
                    organizerId = 1,
                    headerImagePath = image
                )

                eventRepository.addEvent(event)

                call.respond(
                    HttpStatusCode.Created,
                    CreateEventResponse(
                        success = true,
                        message = "Event created successfully"
                    )
                )
            } catch (e: Exception) {
                println("Error creating event: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, CreateEventResponse(
                    success = false,
                    message = "Error creating event: ${e.message}"
                ))
            }
        }
    }
    get("/home/create-event") {
        call.respondHtml(HttpStatusCode.OK){
            createEvent()
        }
    }


    staticFiles("/resources", File("files")){
        default("htmx.js")
    }
}
