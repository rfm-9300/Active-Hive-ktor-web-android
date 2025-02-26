package example.com.web.pages.homePage.eventTab

import example.com.data.db.event.Event
import example.com.data.utils.dayMonthTime
import example.com.web.loadJs
import kotlinx.html.*

fun HTML.eventDetail(event: Event){
    val dates = event.date.dayMonthTime()
    body {
        div(classes = "w-full py-4") {
            div(classes = "bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4") {
                div(classes = "mb-4") {
                    h1(classes = "text-2xl font-bold") {
                        +event.title
                    }
                    p(classes = "text-gray-700") {
                        +"${dates.day} ${dates.month}, ${dates.time}"
                    }
                }
                p(classes = "text-gray-700") {
                    +event.description
                }
                div(classes = "mt-4") {
                    span(classes = "text-gray-700 font-bold") {
                        +"participants: ${event.attendees.size}/${event.maxAttendees}"
                    }
                    span(classes = " text-gray-700 ml-4") {
                        +"names: ${event.attendees.joinToString { it.firstName }}"
                    }

                }

            }
            // join event button
            div(classes = "flex justify-end") {
                button(classes = "bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700") {
                    attributes["onclick"] = "joinEvent(${event.id})"
                    +"Join Event"
                }
            }
            loadJs("event/event-detail")
        }
    }
}