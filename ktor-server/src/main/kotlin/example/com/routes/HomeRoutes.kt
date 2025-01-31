package example.com.routes

import ImageFileHandler
import example.com.data.db.event.Event
import example.com.data.db.event.EventRepository
import example.com.data.responses.CreateEventResponse
import example.com.data.utils.LikeEventManager
import example.com.plugins.Logger
import example.com.web.pages.homePage.homePage
import example.com.web.components.topbar.profileMenu
import example.com.web.pages.homePage.eventTab.createEvent
import example.com.web.pages.homePage.eventTab.allEventsTab
import example.com.web.pages.homePage.homeTab.homeTab
import io.ktor.http.*
import io.ktor.http.content.*
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
import io.ktor.utils.io.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.html.body
import kotlinx.io.readByteArray
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
            allEventsTab(
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
        likeEventManager.emitLike(postId, likesCount)
        call.respond(HttpStatusCode.OK)
    }

    sse("/home/sse") {
        try {
            // Send initial connection message
            send(ServerSentEvent(data = "sse connected"))

            // Keep connection alive with a ping every 30 seconds
            val keepAliveJob = launch {
                while (true) {
                    delay(30000) // 30 seconds
                    send(ServerSentEvent(data = "ping", event = "keepalive"))
                }
            }

            // Main collection job
            try {
                likeEventManager.eventDeleted
                    .onEach { event ->
                        send(ServerSentEvent(
                            data = "Post  likes",
                            event = "event-deleted",
                            id = 2.toString()
                        ))
                    }
                    .catch { e ->
                        send(ServerSentEvent(data = "Error: ${e.message}", event = "error"))
                    }
                    .collect {
                        // No action needed, events are sent in onEach
                    }
            } finally {
                keepAliveJob.cancel() // Clean up keep-alive job
            }
        } catch (e: CancellationException) {
            // Normal disconnection, no need to send error
            Logger.d("Client disconnected from SSE")
        } catch (e: Exception) {
            Logger.d("SSE error ${e.message}")
            try {
                send(ServerSentEvent(data = "Error: ${e.message}", event = "error"))
            } catch (_: Exception) {
                // Ignore send errors on connection close
            }
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



    staticFiles("/resources", File("files")){
        default("htmx.js")
    }
}
