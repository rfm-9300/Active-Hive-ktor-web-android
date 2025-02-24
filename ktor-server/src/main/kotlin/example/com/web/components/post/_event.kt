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

    div(classes = "flex flex-row items-center justify-center w-full p-4 rounded-xl") {

        // date div
        div(classes = "flex flex-col flex-1 items-center justify-center text-center text-sm shadow-lg bg-slate-50 rounded-xl py-5") {
            p(classes = "text-lg font-bold") {
                +date
            }
            p(classes = "text-sm text-gray-500") {
                +dayOfWeek
            }
            // icons div
            val hiddenTag = if (!isAdminRequest) "hidden" else ""
            div(classes = "w-full flex flex-row flex-start px-1 gap-2 mt-2 $hiddenTag") {
                span(classes = "delete-icon rounded-full overflow-hidden cursor-pointer"){
                    attributes["data-event-id"] = event.id.toString()
                    attributes["onclick"] = "deleteEvent(${event.id})"
                    svgIcon(SvgIcon.DELETE)
                }
                span(classes = "edit-icon rounded-full overflow-hidden cursor-pointer $hiddenTag"){
                    attributes["hx-get"] = Routes.Ui.Event.UPDATE.replace("{eventId}", event.id.toString())
                    attributes["hx-target"] = "#main-content"
                    svgIcon(SvgIcon.EDIT)
                }
            }
        }

        // event card div
        div(classes = "flex w-[80%] p-4 bg-white rounded-xl shadow-lg hover:bg-gray-100 cursor-pointer") {
            attributes["hx-get"] = url
            attributes["hx-target"] = "#main-content"
            div(classes = "flex flex-row items-start justify-between w-full") {
                div(classes = "flex flex-col items-start justify-center ml-4") {
                    p(classes = "text-lg font-bold mb-2") {
                        +event.title
                    }
                    div(classes = "text-sm text-gray-500 flex items-center") {
                        div(classes = "w-4 h-4 mr-1") { svgIcon(SvgIcon.TIME, size = 12) }
                        +time
                    }
                    p(classes = "text-sm text-gray-500") { +"With ${event.organizerName}" }
                }
                // image div
                div(classes = "flex flex-row items-center") {
                    img(classes = "w-[150px] h-[150px] rounded-2xl") {
                        src = "/resources/uploads/images/${event.headerImagePath}"
                    }
                }
            }
        }

    }
}