package example.com.routes

import example.com.data.db.event.Event
import example.com.data.db.event.EventRepository
import example.com.data.db.user.UserRepository
import example.com.data.requests.EventRequest
import example.com.data.responses.CreateEventResponse
import example.com.data.utils.SseAction
import example.com.data.utils.SseManager
import example.com.plugins.Logger
import example.com.web.pages.homePage.eventTab.createEvent
import example.com.web.pages.homePage.eventTab.eventDetail
import example.com.web.pages.homePage.eventTab.updateEvent
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import java.time.LocalDateTime

fun Route.eventRoutes(
    eventRepository: EventRepository,
    sseManager: SseManager,
    userRepository: UserRepository
) {
    /**
     * API Routes
     */

    //api update event
    authenticate {
        post(Routes.Api.Event.UPDATE) {
            try {
                val multiPart = call.receiveMultipart()
                var eventId = 0
                var title = ""
                var description = ""
                var date = ""
                var location = ""
                var image = ""

                multiPart.forEachPart {
                    when(it) {
                        is PartData.FormItem -> {
                            when(it.name) {
                                "eventId" -> eventId = it.value.toInt()
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
                    id = eventId,
                    title = title,
                    description = description,
                    date = LocalDateTime.now(),
                    location = location,
                    organizerId = 1,
                    headerImagePath = image
                )
                Logger.d("Updating event: $event")

                if (!eventRepository.updateEvent(event)) throw Exception("Error updating event")
                Logger.d("Event updated successfully")

                call.respond(
                    HttpStatusCode.OK,
                    CreateEventResponse(
                        success = true,
                        message = "Event updated successfully"
                    )
                )
            } catch (e: Exception) {
                Logger.d("Error updating event: ${e.message}")
                call.respond(
                    HttpStatusCode.BadRequest, CreateEventResponse(
                        success = false,
                        message = "Error updating event: ${e.message}"
                    )
                )
            }
        }
    }

    //api delete event
    authenticate {
        post(Routes.Api.Event.DELETE) {
            val request = kotlin.runCatching { call.receiveNullable<EventRequest>() }.getOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
            val deletedEvent = eventRepository.deleteEvent(request.eventId)

            if (deletedEvent) {
                sseManager.emitEvent(SseAction.RefreshEvents)
            }
            call.respond (HttpStatusCode.OK,
                if (deletedEvent) CreateEventResponse.success() else CreateEventResponse.failure()
            )
        }
    }

    //api create event
    authenticate {
        post(Routes.Api.Event.CREATE) {
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
                call.respond(
                    HttpStatusCode.BadRequest, CreateEventResponse(
                        success = false,
                        message = "Error creating event: ${e.message}"
                    )
                )
            }
        }
    }

    // api join event
    authenticate {
        post(Routes.Api.Event.JOIN_EVENT) {
            val principal = call.principal<JWTPrincipal>() ?: return@post respondHelper(success = false, message = "User not found", call = call)
            val userId = principal.getClaim("userId", String::class)
            Logger.d("User ID: $userId")

            // Add explicit null check and empty string check
            if (userId.isNullOrEmpty() || userId == "null") {
                return@post respondHelper(success = false, message = "User not found", call = call)
            }

            val eventId = call.receive<EventRequest>().eventId
            val attendees = eventRepository.getEventAttendees(eventId)
            Logger.d("Attendees: $attendees")

            val event = eventRepository.getEvent(eventId) ?: return@post respondHelper(success = false, message = "Event not found", call = call)

            // check if user is already in the event
            event.attendees.forEach {
                if (it.id == userId.toInt()) {
                    return@post respondHelper(success = false, message = "User already in event", call = call)
                }
            }
            Logger.d("user $userId joining event $eventId")

            val joined = eventRepository.joinEvent(eventId, userId.toInt())
            if (joined) {
                sseManager.emitEvent(SseAction.RefreshEvents)
            }
            call.respond(HttpStatusCode.OK, CreateEventResponse(
                success = joined,
                message = if (joined) "Joined event" else "Failed to join event"
            ))
        }
    }


    /**
     * UI Routes
     */

    //ui update event
    get(Routes.Ui.Event.UPDATE) {
        val eventId = call.parameters["eventId"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val event = eventRepository.getEvent(eventId) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respondHtml(HttpStatusCode.OK){
            updateEvent(event)
        }
    }

    // event details
    get(Routes.Ui.Event.DETAILS) {
        val eventId = call.parameters["eventId"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
        val event = eventRepository.getEvent(eventId) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respondHtml(HttpStatusCode.OK){
            eventDetail(event)
        }
    }

    // create event
    get(Routes.Ui.Event.CREATE) {
        call.respondHtml(HttpStatusCode.OK){
            createEvent()
        }
    }


}