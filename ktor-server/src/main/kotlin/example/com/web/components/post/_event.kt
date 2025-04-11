package example.com.web.components.post

import example.com.data.db.event.Event
import example.com.data.db.user.UserProfile
import example.com.data.db.user.UserRepository
import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import example.com.web.components.userProfileImage
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import org.koin.java.KoinJavaComponent.getKoin
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun HtmlBlockTag.event(event: Event, isAdminRequest: Boolean = false) {
    val userRepository = getKoin().get<UserRepository>()
    val user = runBlocking {
        userRepository.getUserProfile(event.organizerId)
    }

    val date = LocalDateTime.parse(event.date.toString()).format(DateTimeFormatter.ofPattern("dd MMM"))
    val dayOfWeek = LocalDateTime.parse(event.date.toString()).dayOfWeek.toString()
    val time = LocalDateTime.parse(event.date.toString()).format(DateTimeFormatter.ofPattern("HH:mm"))
    val url = Routes.Ui.Event.DETAILS.replace("{eventId}", event.id.toString())

    // Calculate if event is today
    val isToday = LocalDateTime.parse(event.date.toString()).toLocalDate() == LocalDateTime.now().toLocalDate()
    val todayClass = if (isToday) "border-yellow-500 bg-yellow-50/80" else "border-yellow-300 bg-yellow-50/80"

    // Determine occupancy status
    val occupancyPercentage = (event.attendees.size.toFloat() / event.maxAttendees) * 100
    val occupancyColor = when {
        occupancyPercentage >= 90 -> "text-red-600"
        occupancyPercentage >= 75 -> "text-yellow-600"
        occupancyPercentage >= 50 -> "text-yellow-800"
        else -> "text-black"
    }

    // padding if no icons
    val padding = if (isAdminRequest) "" else "py-6"

    div(classes = "flex flex-col sm:flex-row items-center w-full sm:w-[80%] space-y-3 sm:space-y-0 sm:space-x-4 group mb-4") {
        // Date container
        div(classes = "flex flex-col items-center justify-center w-full sm:w-24 min-w-[6rem] p-3 text-center shadow-md backdrop-blur-sm rounded-xl border transition-all duration-300 " + 
             (if (isToday) "border-yellow-500 bg-yellow-50/80" else "border-yellow-300 bg-yellow-50/80") +
             (if (isAdminRequest) "" else " py-6")) {
            p(classes = "text-lg font-semibold text-black") { +date }
            p(classes = "text-sm text-yellow-800 capitalize") { +dayOfWeek.take(3).lowercase() }

            // Time badge
            div(classes = "mt-2 px-2 py-1 bg-white/80 rounded-full text-xs font-medium text-black backdrop-blur-sm") {
                +time
            }

            // Admin controls
            if (isAdminRequest) {
                div(classes = "w-full flex flex-row justify-center gap-1 mt-2") {
                    span(classes = "p-1 rounded-full bg-yellow-100/50 hover:bg-red-100 transition-colors cursor-pointer") {
                        attributes["data-event-id"] = event.id.toString()
                        attributes["onclick"] = "deleteEvent(${event.id})"
                        svgIcon(SvgIcon.DELETE, classes = "w-4 h-4 text-red-600")
                    }
                    span(classes = "p-1 rounded-full bg-yellow-100/50 hover:bg-yellow-200/80 transition-colors cursor-pointer") {
                        attributes["hx-get"] = Routes.Ui.Event.UPDATE.replace("{eventId}", event.id.toString())
                        attributes["hx-target"] = "#main-content"
                        svgIcon(SvgIcon.EDIT, classes = "w-4 h-4 text-black")
                    }
                }
            }
        }

        // Event card
        div(classes = "flex-1 p-4 bg-white backdrop-blur-sm rounded-xl border border-yellow-200 shadow-md hover:shadow-lg hover:border-yellow-400 hover:bg-yellow-50 transition-all duration-300 cursor-pointer group-hover:translate-x-1 w-full") {
            attributes["hx-get"] = url
            attributes["hx-target"] = "#main-content"

            div(classes = "flex flex-col items-center justify-between gap-3") {

                div(classes = "w-full flex flex-row justify-start") {
                    // Text content
                    div(classes = "flex flex-col w-full align-start") {
                        p(classes = "text-xl font-bold text-black mb-1 tracking-tight") { +event.title }

                        // Location with icon
                        div(classes = "flex items-center text-sm text-gray-700 mb-2") {
                            svgIcon(SvgIcon.LOCATION, classes = "w-4 h-4 text-gray-600 mr-1")
                            +event.location
                        }

                        div(classes = "flex items-center") {
                            // Updated to use userProfileImage component
                            div(classes = "w-4 h-4 rounded-full overflow-hidden mr-2") {
                                user?.profileImagePath?.let { 
                                    userProfileImage(
                                        it,
                                        "Profile picture of ${event.organizerName}",
                                        "w-full h-full object-cover"
                                    )
                                }
                            }
                            p(classes = "text-sm text-yellow-700") {
                                +"Hosted by "
                                span(classes = "font-medium text-black") { +event.organizerName }
                            }
                        }
                        // Capacity indicator
                        div(classes = "mt-2 flex flex-col w-full") {
                            div(classes = "flex justify-between items-center text-xs mb-1") {
                                span { +"Capacity" }
                                span(classes = occupancyColor) { +"${event.attendees.size}/${event.maxAttendees}" }
                            }
                            // Progress bar
                            div(classes = "w-full bg-gray-200 rounded-full h-1.5") {
                                div(classes = "h-1.5 rounded-full bg-yellow-500") {
                                    style = "width: ${(event.attendees.size.toFloat() / event.maxAttendees * 100).coerceAtMost(
                                        100F
                                    )}%"
                                }
                            }
                        }
                    }
                    // Image with status indicator
                    div(classes = "relative flex-shrink-0 ml-4") {
                        if (isToday) {
                            div(classes = "absolute -top-1 -right-1 z-10 px-2 py-0.5 bg-yellow-500 text-black rounded-full text-xs font-bold") {
                                +"TODAY"
                            }
                        }
                        img(classes = "w-32 h-32 rounded-xl border-2 border-yellow-100 object-cover shadow-inner") {
                            src = "/resources/uploads/images/${event.headerImagePath}"
                            alt = event.title
                        }
                    }
                }
            }
        }
    }
}