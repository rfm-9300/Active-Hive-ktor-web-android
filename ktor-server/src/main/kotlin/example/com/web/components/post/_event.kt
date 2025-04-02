package example.com.web.components.post

import example.com.data.db.event.Event
import example.com.data.db.user.UserProfile
import example.com.data.db.user.UserRepository
import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
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
    val todayClass = if (isToday) "border-indigo-400 bg-indigo-50/80" else "border-blue-200 bg-blue-50/80"

    // Determine occupancy status
    val occupancyPercentage = (event.attendees.size.toFloat() / event.maxAttendees) * 100
    val occupancyColor = when {
        occupancyPercentage >= 90 -> "text-red-600"
        occupancyPercentage >= 75 -> "text-orange-500"
        occupancyPercentage >= 50 -> "text-blue-600"
        else -> "text-green-600"
    }

    // padding if no icons
    val padding = if (isAdminRequest) "" else "py-6"

    val profileImagePath = if (user?.profileImagePath?.isEmpty() == true) "/resources/images/default-user-image.webp" else "/resources/uploads/images/${user?.profileImagePath}"


    div(classes = "flex flex-col sm:flex-row items-center w-full sm:w-[80%] space-y-3 sm:space-y-0 sm:space-x-4 group mb-4") {
        // Date container
        div(classes = "flex flex-col items-center justify-center w-full sm:w-24 min-w-[6rem] p-3 text-center shadow-md backdrop-blur-sm rounded-xl border transition-all duration-300 $todayClass $padding") {
            p(classes = "text-lg font-semibold text-blue-600") { +date }
            p(classes = "text-sm text-blue-400 capitalize") { +dayOfWeek.take(3).lowercase() }

            // Time badge
            div(classes = "mt-2 px-2 py-1 bg-white/80 rounded-full text-xs font-medium text-blue-600 backdrop-blur-sm") {
                +time
            }

            // Admin controls
            if (isAdminRequest) {
                div(classes = "w-full flex flex-row justify-center gap-1 mt-2") {
                    span(classes = "p-1 rounded-full bg-blue-100/50 hover:bg-red-100 transition-colors cursor-pointer") {
                        attributes["data-event-id"] = event.id.toString()
                        attributes["onclick"] = "deleteEvent(${event.id})"
                        svgIcon(SvgIcon.DELETE, classes = "w-4 h-4 text-red-600")
                    }
                    span(classes = "p-1 rounded-full bg-blue-100/50 hover:bg-blue-200/80 transition-colors cursor-pointer") {
                        attributes["hx-get"] = Routes.Ui.Event.UPDATE.replace("{eventId}", event.id.toString())
                        attributes["hx-target"] = "#main-content"
                        svgIcon(SvgIcon.EDIT, classes = "w-4 h-4 text-blue-600")
                    }
                }
            }
        }

        // Event card
        div(classes = "flex-1 p-4 bg-white backdrop-blur-sm rounded-xl border border-blue-100 shadow-md hover:shadow-lg hover:border-blue-400 hover:bg-blue-50 transition-all duration-300 cursor-pointer group-hover:translate-x-1 w-full") {
            attributes["hx-get"] = url
            attributes["hx-target"] = "#main-content"

            div(classes = "flex flex-col items-center justify-between gap-3") {

                div(classes = "w-full flex flex-row justify-start") {
                    // Text content
                    div(classes = "flex flex-col w-full align-start") {
                        p(classes = "text-xl font-bold text-gray-800 mb-1 tracking-tight") { +event.title }

                        // Location with icon
                        div(classes = "flex items-center text-sm text-gray-500 mb-2") {
                            svgIcon(SvgIcon.LOCATION, classes = "w-4 h-4 text-gray-400 mr-1")
                            +event.location
                        }

                        div(classes = "flex items-center") {
                            span {
                                img(classes = "object-cover w-4 h-4 rounded-full mr-2", src = profileImagePath, alt = "Active Hive Logo")
                            }
                            p(classes = "text-sm text-blue-500") {

                                +"Hosted by "
                                span(classes = "font-medium text-blue-600") { +event.organizerName }
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
                                div(classes = "h-1.5 rounded-full bg-blue-600") {
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
                            div(classes = "absolute -top-1 -right-1 z-10 px-2 py-0.5 bg-indigo-500 text-white rounded-full text-xs font-bold") {
                                +"TODAY"
                            }
                        }
                        img(classes = "w-32 h-32 rounded-xl border-2 border-blue-50 object-cover shadow-inner") {
                            src = "/resources/uploads/images/${event.headerImagePath}"
                            alt = event.title
                        }
                    }
                }



            }
        }
    }
}