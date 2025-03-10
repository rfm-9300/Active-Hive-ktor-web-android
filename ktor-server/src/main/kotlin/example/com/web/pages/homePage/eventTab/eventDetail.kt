package example.com.web.pages.homePage.eventTab

import example.com.data.db.event.Event
import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.projectButton
import example.com.web.components.svgIcon
import example.com.web.loadJs
import kotlinx.html.*
import java.net.URLEncoder
import java.time.*
import java.time.format.*

fun HTML.eventDetail(event: Event) {
    val now = LocalDateTime.now()
    val eventDate = LocalDateTime.parse(event.date.toString())
    val isToday = eventDate.toLocalDate() == now.toLocalDate()
    val isTomorrow = eventDate.toLocalDate() == now.toLocalDate().plusDays(1)
    val formattedDate = when {
        isToday -> "Today at " + eventDate.format(TimeFormatter)
        isTomorrow -> "Tomorrow at " + eventDate.format(TimeFormatter)
        else -> eventDate.format(DateAndTimeFormatter)
    }
    val isAdmin = true

    val url = Routes.Ui.Event.DETAILS.replace("{eventId}", event.id.toString())
    val encodedUrl = URLEncoder.encode(url, "utf-8")
    val encodedTitle = URLEncoder.encode(event.title, "utf-8")

    body {
        div(classes = "w-[90%] mx-auto py-6") { // Increased padding, centered with mx-auto
            div(classes = "bg-white bg-opacity-80 shadow-lg p-6 rounded-xl border border-blue-200 transition-all duration-300") { // Softer border, larger padding, higher opacity
                // Header Image
                img(src = "/resources/uploads/images/${event.headerImagePath}", classes = "w-full h-56 object-cover rounded-t-xl mb-6", alt = event.title) // Increased height, margin

                // Title and Date
                div(classes = "mb-6") { // Grouped for spacing
                    h1(classes = "text-3xl font-bold text-gray-900 mb-2") { +event.title } // Larger, bolder title
                    p(classes = "text-gray-600 text-base") { +formattedDate } // Larger text for date
                }

                // Description
                p(classes = "text-gray-700 text-lg leading-relaxed mb-6") { +event.description } // Larger text, more spacing

                // Event Details (Location, Organizer, Countdown)
                div(classes = "space-y-3 mb-6") { // Vertical spacing for details
                    p(classes = "text-gray-700 text-base") {
                        +"Location: "
                        a(href = "https://www.google.com/maps/place/${event.location}", target = "_blank", classes = "text-blue-500 hover:underline") {
                            +event.location
                        }
                    }
                    p(classes = "text-gray-700 text-base") {
                        +"Organized by: ${event.organizerName}" // Fixed syntax
                    }
                    if (eventDate > now) {
                        p(classes = "text-blue-500 text-base") {
                            +"Starts in "
                            span { id = "countdown" }
                        }
                    } else {
                        p(classes = "text-green-500 text-base") {
                            +"Event has started!"
                        }
                    }
                }

                // Participants and Join Button
                div(classes = "flex items-center justify-between mb-6") {
                    span(classes = "text-gray-700 font-semibold text-base") {
                        +"Participants: ${event.attendees.size}/${event.maxAttendees}"
                        if (event.attendees.size >= event.maxAttendees) {
                            span(classes = "text-red-500 ml-2") { +" (Sold out)" }
                        }
                    }
                    if (eventDate <= now) {
                        span(classes = "text-gray-500 text-base") {
                            +"Event has started."
                        }
                    } else if (event.attendees.size >= event.maxAttendees) {
                        span(classes = "bg-red-500 text-white px-3 py-1 rounded-full text-sm shadow-md") {
                            +"Event Full"
                        }
                    } else {
                        projectButton(text = "Join Event", onClick = "joinEvent(${event.id})")
                    }
                }

                // Action Buttons (Social Sharing, Calendar, Back)
                div(classes = "flex flex-wrap items-center gap-4") { // Flex wrap for responsiveness
                    a(href = "https://www.facebook.com/sharer/sharer.php?u=$encodedUrl", target = "_blank", classes = "text-blue-500 hover:text-blue-700 transition-colors duration-200") {
                        svgIcon(SvgIcon.FACEBOOK, classes = "w-6 h-6")
                    }
                    a(href = "https://x.com/intent/tweet?text=$encodedTitle&url=$encodedUrl", target = "_blank", classes = "text-blue-500 hover:text-blue-700 transition-colors duration-200") {
                        svgIcon(SvgIcon.X, classes = "w-6 h-6")
                    }
                    a(classes = "text-blue-500 hover:underline text-base", href = "webcal://yourdomain.com/events/${event.id}.ics") {
                        +"Add to Calendar"
                    }
                    p(classes = "text-blue-500 hover:underline text-base cursor-pointer") {
                        attributes["hx-get"] = Routes.Ui.Event.LIST_UPCOMING
                        attributes["hx-target"] = "#main-content"
                        +"Back to Events"
                    }
                }
            }

            // Admin Waiting List Section (Below Main Card)
            if (isAdmin) {
                div(classes = "mt-6 bg-white bg-opacity-80 shadow-lg p-6 rounded-xl border border-gray-200") {
                    id = "waiting-list-container"
                    // Toggle Header
                    div(classes = "flex justify-between items-center mb-2 cursor-pointer") {
                        attributes["onclick"] = "toggleWaitingList()"
                        h2(classes = "text-2xl font-semibold text-gray-800") { +"Waiting List" }
                        button(classes = "text-blue-500 hover:text-blue-700 focus:outline-none") {
                            svgIcon(SvgIcon.CHEVRON_DOWN, classes = "w-6 h-6")
                        }
                    }
                    // Collapsible Content
                    div(classes = "hidden") {
                        id = "waiting-list"
                        if (event.waitingList.isEmpty()) {
                            p(classes = "text-gray-600 text-base") { +"No users on the waiting list." }
                        } else {
                            div {
                                div(classes = "flex flex-row justify-between border-b border-gray-200") {
                                    p(classes = "text-gray-700 font-semibold") { +"User" }
                                    p(classes = "text-gray-700 font-semibold") { +"Joined At" }
                                    p(classes = "text-gray-700 font-semibold") { +"Actions" }
                                }
                                event.waitingList.forEach { waitingList ->
                                    div(classes = "flex flex-row justify-between py-2 border-b border-gray-200 text-sm items-center") {
                                        p(classes = "text-gray-700") { +"${waitingList.user.firstName} ${waitingList.user.lastName}" }
                                        p(classes = "text-gray-700") { +waitingList.joinedAt.format(DateAndTimeFormatter) }
                                        button(classes = "bg-blue-500 text-white px-4 py-1 rounded hover:bg-blue-700") {
                                            attributes["hx-post"] = "Routes.Api.Event.REMOVE_WAITING_LIST"
                                            attributes["hx-swap"] = "outerHTML"
                                            attributes["hx-target"] = "#waiting-list"
                                            attributes["hx-params"] = "eventId=${event.id}&userId=${waitingList.user.id}"
                                            +"Check in"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Define eventDate before loading the script
            script {
                +"const eventDate = '${event.date}';"
            }
            loadJs("event/event-detail")
        }
    }
}

val DateAndTimeFormatter = DateTimeFormatter.ofPattern("dd MMM, HH:mm")
val TimeFormatter = DateTimeFormatter.ofPattern("HH:mm")