package example.com.web.pages.homePage.eventTab

import example.com.data.db.event.Event
import kotlinx.html.*

fun HTML.eventDetail(event: Event){
    body {
        div(classes = "w-full py-4") {
            div(classes = "flex justify-end mb-4") {
                button(classes = "bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700") {
                    attributes["hx-get"] = "/home/create-event"
                    attributes["hx-target"] = "#main-content"
                    +"Create Event"
                }
            }
            div(classes = "bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4") {
                div(classes = "mb-4") {
                    h1(classes = "text-2xl font-bold") {
                        +event.title
                    }
                    p(classes = "text-gray-700") {
                        +event.date.toString()
                    }
                }
                p(classes = "text-gray-700") {
                    +event.description
                }
            }
            // join event button
            div(classes = "flex justify-end") {
                button(classes = "bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-700") {
                    attributes["hx-post"] = "/home/join-event"
                    attributes["hx-target"] = "#main-content"
                    attributes["hx-swap"] = "outerHTML"
                    attributes["hx-params"] = "eventId=${event.id}"
                    +"Join Event"
                }
            }
        }
    }
}