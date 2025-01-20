package example.com.web.pages.homePage.eventTab

import kotlinx.html.*

fun HTML.createEvent() {
    body{
        div(classes = "flex flex-col justify-center items-center mx-auto max-w-4xl") {
            id = "event-content"
            div(classes = "flex justify-between items-center mb-8") {
                div {
                    h1(classes = "text-3xl font-bold") { +"Create New Event" }
                    p(classes = "text-gray-600 mt-2") { +"Fill out the details for your upcoming event" }
                }
                a(href = "/events/create", classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                    +"+ New Event"
                }
            }

            form(
                action = "#",
                method = FormMethod.post,
                classes = "w-full bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"
            ) {
                id = "event-form"
                attributes["name"] =  "eventForm"
                div(classes = "mb-4") {
                    label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                        attributes["for"] = "title"
                        +"Event Title"
                    }
                    input(classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700") {
                        attributes["type"] = "text"
                        attributes["name"] = "title"
                        attributes["id"] = "title"
                        attributes["required"] = "true"
                    }
                }

                div(classes = "mb-4") {
                    label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                        attributes["for"] = "description"
                        +"Description"
                    }
                    textArea(classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700") {
                        attributes["name"] = "description"
                        attributes["id"] = "description"
                        attributes["rows"] = "4"
                    }
                }

                div(classes = "mb-4") {
                    label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                        attributes["for"] = "date"
                        +"Event Date"
                    }
                    input(classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700") {
                        attributes["type"] = "datetime-local"
                        attributes["name"] = "date"
                        attributes["id"] = "date"
                        attributes["required"] = "true"
                    }
                }

                div(classes = "mb-4") {
                    label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                        attributes["for"] = "location"
                        +"Location"
                    }
                    input(classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700") {
                        attributes["type"] = "text"
                        attributes["name"] = "location"
                        attributes["id"] = "location"
                    }
                }

                div(classes = "flex items-center justify-center") {
                    button(classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                        attributes["type"] = "button"
                        attributes["id"] = "submit-btn"
                        +"Create Event"
                    }
                }
            }
        }
        script(src = "/resources/js/eventPage.js" ) {}
    }
}