package example.com.web.components.post

import example.com.data.db.event.Event
import example.com.data.utils.LocalDateTimeSerializer
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import kotlinx.html.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun HtmlBlockTag.event(event: Event){
    val date = LocalDateTime.parse(event.date.toString()).format(DateTimeFormatter.ofPattern("dd MMM"))
    val dayOfWeek = LocalDateTime.parse(event.date.toString()).dayOfWeek.toString()
    val time = LocalDateTime.parse(event.date.toString()).format(DateTimeFormatter.ofPattern("hh:mm a"))

    div(classes = "flex flex-row items-center justify-center w-full p-4 rounded-xl") {
        // date div
        div(classes = "flex flex-col flex-1 items-center justify-center text-center text-sm") {
            p(classes = "text-lg font-bold") {
                +date
            }
            p(classes = "text-sm text-gray-500") {
                +dayOfWeek
            }
        }

        div(classes = "flex w-[80%] p-4 bg-white rounded-xl shadow-lg") {
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
                    img(classes = "w-[200px] h-[100px] rounded-2xl") {
                        src = "/resources/uploads/images/0e396674-6989-473f-a61c-ae4c748006a2.jpeg"
                    }
                }
            }
        }

    }
}