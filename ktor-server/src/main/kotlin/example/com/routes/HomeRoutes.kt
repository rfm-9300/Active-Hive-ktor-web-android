package example.com.routes

import example.com.data.db.event.EventRepository
import example.com.data.db.post.PostRepository
import example.com.data.db.user.UserProfile
import example.com.data.db.user.UserRepository
import example.com.data.requests.EventRequest
import example.com.data.requests.PostRequest
import example.com.data.utils.SseAction
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
import java.io.File

fun Route.homeRoutes(
    sseManager: SseManager,
    eventRepository: EventRepository,
    userRepository: UserRepository,
    postRepository: PostRepository
){
    /**
     * Api Routes
     */

    authenticate {
        post(Routes.Api.Post.DELETE) {
            try{
                val request = kotlin.runCatching { call.receiveNullable<PostRequest>() }.getOrNull() ?: return@post respondHelper(success = false, message = "Invalid request", call = call)
                val postId = request.postId

                Logger.d("Delete post request")
                val userId = getIdFromRequest(call) ?: return@post
                if (!authorizeUser(userId)) {
                    return@post respondHelper(success = false, message = "Unauthorized", call = call, statusCode = HttpStatusCode.Unauthorized)
                }
                Logger.d("User $userId is authorized to delete post")

                val isDeleted = postRepository.deletePost(postId)
                if (isDeleted) {
                    sseManager.emitEvent(SseAction.RefreshPosts)
                }
                respondHelper(success = isDeleted, message = if (isDeleted) "Post deleted" else "Post not found", call = call, statusCode = if (isDeleted) HttpStatusCode.OK else HttpStatusCode.NotFound)
            } catch (e: Exception){
                respondHelper(success = false, message = e.message ?: "Error deleting post", call = call, statusCode = HttpStatusCode.InternalServerError)
            }
        }
    }



    /**
     * Ui Routes
     */

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


    authenticate {
        get(Routes.Ui.Home.PROFILE_MENU) {
            val principal = call.principal<JWTPrincipal>() ?: return@get respondHelper(success = false, message = "User not found", call = call)
            val userId = principal.getClaim("userId", String::class) ?: return@get respondHelper(success = false, message = "User not found", call = call)

            val userProfile = userRepository.getUserProfile(userId.toInt())

            call.respondHtml(HttpStatusCode.OK) {
                body {
                    profileMenu(userProfile)
                }
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

    staticFiles("/resources", File("files")){
        default("htmx.js")
    }
}
