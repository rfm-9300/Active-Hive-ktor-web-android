package example.com.web.pages.homePage.eventTab

import kotlinx.html.*

fun HTML.createEvent() {
    body {
        div(classes = "flex flex-col justify-center items-center mx-auto max-w-3xl") {
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
                classes = "w-full bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4",
                encType = FormEncType.multipartFormData
            ) {
                id = "event-form"
                attributes["name"] = "eventForm"

                // Title
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

                // Description
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

                // Event date
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
                    }
                }

                // Image upload
                div(classes = "mb-4") {
                    label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                        attributes["for"] = "image"
                        +"Event Image"
                    }
                    div(classes = "mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md") {
                        div(classes = "flex flex-col space-y-1 text-center") {
                            div(classes = "flex text-sm text-gray-600 justify-center items-center") {
                                label(classes = "relative cursor-pointer bg-white rounded-md font-medium text-blue-600 hover:text-blue-500 items-center") {
                                    input(classes = "sr-only") {
                                        attributes["type"] = "file"
                                        attributes["name"] = "image"
                                        attributes["id"] = "image"
                                        attributes["accept"] = "image/*"
                                    }
                                    div(classes = "items-center") {
                                        span { +"Upload a file"}
                                        span(classes = "pl-1") { +" or drag and drop" }
                                    }
                                }

                            }
                            p(classes = "text-xs text-gray-500") {
                                id = "image-upload-text"
                                +"PNG, JPG, GIF up to 10MB"
                            }
                            p(classes = "text-sm text-green-600 mt-2") {
                                id = "upload-status"
                                +""
                            }
                        }
                    }
                }

                // Submit button
                div(classes = "flex items-center justify-center") {
                    button(classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                        attributes["type"] = "button"
                        attributes["id"] = "submit-btn"
                        +"Create Event"
                    }
                }
            }
        }

        // Import a JavaScript file for form submission or validation logic
        script(src = "/resources/js/create-event/createEvent.js") {}
        script(src = "/resources/js/create-event/main.js") {}
    }
}