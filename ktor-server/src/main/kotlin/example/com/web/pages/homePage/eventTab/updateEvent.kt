package example.com.web.pages.homePage.eventTab

import example.com.data.db.event.Event
import example.com.plugins.Logger
import example.com.web.loadJs
import kotlinx.html.HTML
import kotlinx.html.*
import java.time.format.DateTimeFormatter

fun HTML.updateEvent(event: Event) {
    // Assuming event.date is a LocalDateTime
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val formattedDate = event.date.format(formatter)
    body {
        div(classes = "w-full py-4") {
            div(classes = "bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4") {
                // Form for editing event details
                form(
                    action = "#",
                    method = FormMethod.post,
                    classes = "w-full bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4",
                    encType = FormEncType.multipartFormData
                ) {
                    // Hidden input for event id
                    input(type = InputType.hidden, name = "eventId") {
                        id = "eventId"
                        value = event.id.toString()
                    }

                    // Editable field for event title
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                            htmlFor = "title"
                            +"Event Title"
                        }
                        input(type = InputType.text, name = "title", classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline") {
                            id = "title"
                            value = event.title
                        }
                    }

                    // Editable field for event date
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                            htmlFor = "date"
                            +"Event Date"
                        }
                        input(classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700") {
                            attributes["type"] = "datetime-local"
                            attributes["name"] = "date"
                            attributes["id"] = "date"
                            attributes["required"] = "true"
                            value = formattedDate
                        }
                        Logger.d("Event date: ${event.date}")
                    }

                    // Location
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                            attributes["for"] = "location"
                            +"Location"
                        }
                        input(classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700") {
                            attributes["type"] = "text"
                            attributes["name"] = "location"
                            attributes["id"] = "location"
                            value = event.location
                        }
                    }

                    // Editable field for event description
                    div(classes = "mb-4") {
                        label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                            htmlFor = "description"
                            +"Event Description"
                        }
                        textArea(classes = "shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline") {
                            id = "description"
                            +event.description
                        }
                    }

                    // Save event button
                    div(classes = "flex justify-end") {
                        button(type = ButtonType.submit, classes = "bg-green-500 text-white px-4 py-2 rounded hover:bg-green-700") {
                            attributes["id"] = "submit-btn"
                            +"Save Event"
                        }
                    }
                }
            }
        }
        loadJs("event/update-event")
    }
}