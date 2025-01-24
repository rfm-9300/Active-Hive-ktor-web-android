package example.com.web.components.post

import example.com.data.db.event.Event
import example.com.data.utils.LocalDateTimeSerializer
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.p
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun HtmlBlockTag.event(event: Event){
    val date = LocalDateTime.parse(event.date.toString()).format(DateTimeFormatter.ofPattern("dd MMM"))
    val dayOfWeek = LocalDateTime.parse(event.date.toString()).dayOfWeek.toString()

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
                    p(classes = "text-lg font-bold") {
                        +event.title
                    }
                    p(classes = "text-sm text-gray-500") {
                        +event.date.toString()
                    }
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