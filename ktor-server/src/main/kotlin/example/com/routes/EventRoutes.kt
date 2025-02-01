package example.com.routes

import example.com.data.db.event.Event
import example.com.data.db.event.EventRepository
import example.com.data.requests.DeleteEventRequest
import example.com.data.responses.CreateEventResponse
import example.com.data.utils.SseAction
import example.com.data.utils.SseManager
import example.com.plugins.Logger
import example.com.web.pages.homePage.eventTab.createEvent
import example.com.web.pages.homePage.eventTab.eventDetail
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.auth.*
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
) {

    get("/home/create-event") {
        call.respondHtml(HttpStatusCode.OK){
            createEvent()
        }
    }

    authenticate {
        post(Routes.Api.Event.DELETE) {
            val request = kotlin.runCatching { call.receiveNullable<DeleteEventRequest>() }.getOrNull() ?: return@post call.respond(HttpStatusCode.BadRequest)
            val deletedEvent = true // eventRepository.deleteEvent(request.eventId)

            if (deletedEvent) {
                sseManager.emitEvent(SseAction.RefreshEvents)
            }
            call.respond (HttpStatusCode.OK,
                if (deletedEvent) CreateEventResponse.success() else CreateEventResponse.failure()
            )
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

}