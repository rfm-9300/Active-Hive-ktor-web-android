package example.com.web.pages.homePage

import example.com.data.db.event.EventRepository
import example.com.data.db.user.UserRepository
import example.com.web.components.layout.layout
import example.com.web.pages.homePage.navbar.navbar
import example.com.web.components.topbar.topbar
import example.com.web.loadJs
import example.com.web.pages.homePage.eventTab.allEventsTab
import example.com.web.pages.homePage.homeTab.homeTab
import kotlinx.html.*

fun HTML.homePage(eventRepository: EventRepository) {
    layout {
        div(classes = "flex flex-col w-full bg-gradient-to-br from-blue-50 via-blue-100 to-indigo-50 min-h-screen") {
            id = "root-container"
            // Sticky topbar container
            div(classes = "sticky top-0 z-20 w-full") {
                topbar()
            }
            // Navigation Bar
            //navbar()

            // Alert Box
            div(classes = "fixed z-20 top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 hidden p-4 rounded-lg shadow-lg") {
                id = "alert-box"
                span {
                    id = "alert-message"
                }
                span(classes = "ml-4 cursor-pointer") {
                    onClick = "closeAlert()"
                    +"×"
                }
            }

            div (classes = "w-full flex flex-col justify-center items-center") {
                id = "main-content-bg"
                // content
                div( classes = "flex flex-col justify-center items-center w-[70%] mt-1 py-2 px-4") {
                    id = "main-content"
                    allEventsTab(eventRepository, true)
                }
            }


        }
        loadJs("home")
    }
}