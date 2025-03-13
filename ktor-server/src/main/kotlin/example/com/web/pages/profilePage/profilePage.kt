package example.com.web.pages.profilePage

import example.com.data.db.event.Event
import example.com.data.db.user.UserProfile
import example.com.data.utils.monthYear
import example.com.web.components.layout.layout
import example.com.web.components.svgIcon
import example.com.web.components.SvgIcon
import example.com.web.loadJs
import kotlinx.html.*
import java.time.format.DateTimeFormatter

fun HTML.profilePage(user: UserProfile) {
    val userName = "${user.firstName} ${user.lastName}"
    val joinString = "Joined on ${user.joinedAt?.monthYear()}"
    val hostedEvents = user.hostedEvents.size
    val attendedEvents = user.attendedEvents.size
    val waitingEvents = user.waitingListEvents.size
    val attendingEvents = user.attendingEvents.size
    
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm")

    layout {
        div(classes = "flex flex-col w-full items-center justify-center relative") {
            // hidden floating box change profile picture
            div(classes = "relative w-full flex justify-center") {
                profileEditBox()
            }

            // profile info div
            div(classes = "flex flex-row items-center w-full p-4 rounded-xl") {
                div(classes = "relative group") {
                    img(
                        src = "/resources/uploads/images/${user.profileImagePath}",
                        classes = "w-20 h-20 rounded-full object-cover border-2 border-blue-100"
                    )

                    // Edit button (hidden by default, shown on hover)
                    button(classes = "absolute inset-0 flex items-center justify-center w-20 h-20 " +
                            "rounded-full bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 " +
                            "transition-opacity duration-200 text-white") {
                        onClick = "showEditProfile()"
                        +"Edit"
                    }
                }

                // user info
                div(classes = "flex flex-col ml-4") {
                    p(classes = "font-bold text-xl") { +userName }
                    p(classes = "text-sm text-gray-600") { +joinString }
                    div(classes = "flex gap-4 mt-2") {
                        div(classes = "flex items-center text-sm text-gray-500") {
                            svgIcon(SvgIcon.CALENDAR, "w-4 h-4 mr-1")
                            +"$hostedEvents hosted"
                        }
                        div(classes = "flex items-center text-sm text-gray-500") {
                            svgIcon(SvgIcon.PROFILE, "w-4 h-4 mr-1")
                            +"$attendedEvents attended"
                        }
                    }
                }
            }

            // Events Tabs
            div(classes = "w-full mt-8") {
                // Tabs
                div(classes = "flex border-b border-gray-200") {
                    button(classes = "px-4 py-2 text-sm font-medium text-blue-600 border-b-2 border-blue-600") {
                        attributes["onclick"] = "switchTab('hosted')"
                        id = "hosted-tab"
                        +"Hosted Events ($hostedEvents)"
                    }
                    button(classes = "px-4 py-2 text-sm font-medium text-gray-500 hover:text-gray-700") {
                        attributes["onclick"] = "switchTab('attended')"
                        id = "attended-tab"
                        +"Attended Events ($attendedEvents)"
                    }
                    button(classes = "px-4 py-2 text-sm font-medium text-gray-500 hover:text-gray-700") {
                        attributes["onclick"] = "switchTab('waiting')"
                        id = "waiting-tab"
                        +"Waiting List ($waitingEvents)"
                    }
                    button(classes = "px-4 py-2 text-sm font-medium text-gray-500 hover:text-gray-700") {
                        attributes["onclick"] = "switchTab('attending')"
                        id = "attending-tab"
                        +"Attending Events ($attendingEvents)"
                    }
                }

                // Tab Content
                div(classes = "mt-4") {
                    // Hosted Events
                    div(classes = "space-y-4") {
                        id = "hosted-content"
                        if (user.hostedEvents.isEmpty()) {
                            p(classes = "text-gray-500 text-center py-4") {
                                +"No hosted events yet"
                            }
                        } else {
                            user.hostedEvents.forEach { event ->
                                eventCard(event, dateFormatter)
                            }
                        }
                    }

                    // Attended Events
                    div(classes = "hidden space-y-4") {
                        id = "attended-content"
                        if (user.attendedEvents.isEmpty()) {
                            p(classes = "text-gray-500 text-center py-4") {
                                +"No attended events yet"
                            }
                        } else {
                            user.attendedEvents.forEach { event ->
                                eventCard(event, dateFormatter)
                            }
                        }
                    }

                    // Waiting List Events
                    div(classes = "hidden space-y-4") {
                        id = "waiting-content"
                        if (user.waitingListEvents.isEmpty()) {
                            p(classes = "text-gray-500 text-center py-4") {
                                +"No events in waiting list"
                            }
                        } else {
                            user.waitingListEvents.forEach { event ->
                                eventCard(event, dateFormatter)
                            }
                        }
                    }

                    // Attending Events
                    div(classes = "hidden space-y-4") {
                        id = "attending-content"
                        if (user.attendingEvents.isEmpty()) {
                            p(classes = "text-gray-500 text-center py-4") {
                                +"No attending events yet"
                            }
                        } else {
                            user.attendingEvents.forEach { event ->
                                eventCard(event, dateFormatter)
                            }
                        }
                    }
                }
            }
            loadJs("profile-page")
        }
    }
}

private fun FlowContent.eventCard(event: Event, dateFormatter: DateTimeFormatter) {
    div(classes = "bg-white rounded-lg shadow p-4 hover:shadow-md transition-shadow") {
        div(classes = "flex justify-between items-start") {
            div(classes = "flex-1") {
                h3(classes = "font-semibold text-lg text-gray-900") {
                    +event.title
                }
                p(classes = "text-sm text-gray-600 mt-1") {
                    +event.description
                }
                div(classes = "flex items-center gap-4 mt-2") {
                    div(classes = "flex items-center text-sm text-gray-500") {
                        svgIcon(SvgIcon.CALENDAR, "w-4 h-4 mr-1")
                        +event.date.format(dateFormatter)
                    }
                    div(classes = "flex items-center text-sm text-gray-500") {
                        svgIcon(SvgIcon.PROFILE, "w-4 h-4 mr-1")
                        +"${event.attendees.size} attendees"
                    }
                    
                    // Location
                    div(classes = "flex items-center text-sm text-gray-500 mt-1") {
                        svgIcon(SvgIcon.HOME, "w-4 h-4 mr-1")
                        +event.location
                    }
                }

                // View button
                div(classes = "ml-4") {
                    a(classes = "px-3 py-1 text-sm bg-blue-50 hover:bg-blue-100 text-blue-700 rounded-lg transition-colors") {
                        href = "/events/${event.id}"
                        +"View"
                    }
                }
            }
        }
    }
}