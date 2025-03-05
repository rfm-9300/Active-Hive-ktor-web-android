package example.com.web.components.post

import example.com.data.db.event.Event
import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import kotlinx.html.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun HtmlBlockTag.event(event: Event, isAdminRequest: Boolean = false) {
    val date = LocalDateTime.parse(event.date.toString()).format(DateTimeFormatter.ofPattern("dd MMM"))
    val dayOfWeek = LocalDateTime.parse(event.date.toString()).dayOfWeek.toString()
    val time = LocalDateTime.parse(event.date.toString()).format(DateTimeFormatter.ofPattern("HH:mm"))
    val url = Routes.Ui.Event.DETAILS.replace("{eventId}", event.id.toString())

    div(classes = "flex flex-row items-center w-[80%] p-4 space-x-4 group") {
        // Date container
        div(classes = "flex flex-col items-center justify-center w-24 min-w-[6rem] p-3 text-center shadow-md bg-blue-50/80 backdrop-blur-sm rounded-xl border border-blue-200 transition-all duration-300") {
            p(classes = "text-lg font-semibold text-blue-600") { +date }
            p(classes = "text-sm text-blue-400") { +dayOfWeek.take(3) }

            // Admin controls
            val hiddenTag = if (!isAdminRequest) "hidden" else ""
            div(classes = "w-full flex flex-row justify-center gap-1 mt-2 $hiddenTag") {
                span(classes = "p-1 rounded-full bg-blue-100/50 hover:bg-blue-200/80 transition-colors") {
                    attributes["data-event-id"] = event.id.toString()
                    attributes["onclick"] = "deleteEvent(${event.id})"
                    svgIcon(SvgIcon.DELETE, classes = "w-4 h-4 text-blue-600")
                }
                span(classes = "p-1 rounded-full bg-blue-100/50 hover:bg-blue-200/80 transition-colors") {
                    attributes["hx-get"] = Routes.Ui.Event.UPDATE.replace("{eventId}", event.id.toString())
                    attributes["hx-target"] = "#main-content"
                    svgIcon(SvgIcon.EDIT, classes = "w-4 h-4 text-blue-600")
                }
            }
        }

        // Event card
        div(classes = "flex-1 p-4 bg-white-500 backdrop-blur-sm rounded-xl border border-blue-100 shadow-md hover:shadow-lg hover:border-blue-400 hover:bg-blue-100 bg-opacity-50 transition-all duration-300 cursor-pointer group-hover:translate-x-1") {
            attributes["hx-get"] = url
            attributes["hx-target"] = "#main-content"

            div(classes = "flex flex-row items-start justify-between") {
                // Text content
                div(classes = "flex flex-col pr-4") {
                    p(classes = "text-xl font-bold text-gray-800 mb-1 tracking-tight") { +event.title }

                    div(classes = "flex items-center text-sm text-blue-500/90 ") {
                        svgIcon(SvgIcon.TIME, classes = "w-4 h-4 text-blue-400")
                        +time
                    }

                    p(classes = "text-sm text-blue-400") {
                        +"Hosted by "
                        span(classes = "font-medium text-blue-500") { +event.organizerName }
                    }

                    p(classes = "text-sm text-blue-400") { +"${event.attendees.size} attending" }
                }

                // Image
                div(classes = "flex-shrink-0 ml-4") {
                    img(classes = "w-24 h-24 rounded-xl border-2 border-blue-50 object-cover shadow-inner") {
                        src = "/resources/uploads/images/${event.headerImagePath}"
                    }
                }
            }
        }
    }
}