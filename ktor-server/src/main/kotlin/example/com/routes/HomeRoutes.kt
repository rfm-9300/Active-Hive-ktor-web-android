package example.com.routes

import example.com.data.db.event.EventRepository
import example.com.data.utils.SseManager
import example.com.plugins.Logger
import example.com.web.pages.homePage.homePage
import example.com.web.components.topbar.profileMenu
import example.com.web.pages.homePage.eventTab.allEventsTab
import example.com.web.pages.homePage.homeTab.homeTab
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
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
import java.io.File

fun Route.homeRoutes(
    sseManager: SseManager,
    eventRepository: EventRepository
){
    get("/") {
        call.respondHtml(HttpStatusCode.OK){
            homePage()
        }
    }
    get(Routes.Ui.Event.LIST) {
        call.respondHtml(HttpStatusCode.OK){
            allEventsTab(
                eventRepository = eventRepository
            )
        }
    }
    get(Routes.Ui.Home.HOME) {
        call.respondHtml(HttpStatusCode.OK){
            body {
                homeTab()
            }
        }
    }

    sse(Routes.Sse.SSE_CONNECTION) {
        try {
            // Send initial connection message
            send(ServerSentEvent(data = "sse connected"))

            // Main collection job
            sseManager.sseAction
                .onEach { action ->
                    send(ServerSentEvent(
                        event = "sse-action",
                        data = action
                    ))
                }
                .catch { e ->
                    send(ServerSentEvent(data = "Error: ${e.message}", event = "error"))
                }
                .collect {
                    // Keep the connection alive while collecting the flow
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
