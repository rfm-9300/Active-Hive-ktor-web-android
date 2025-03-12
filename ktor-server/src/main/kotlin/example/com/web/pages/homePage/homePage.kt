package example.com.web.pages.homePage

import example.com.data.db.event.EventRepository
import example.com.routes.Routes
import example.com.web.components.layout.layout
import example.com.web.components.topbar.topbar
import example.com.web.loadJs
import example.com.web.pages.homePage.eventTab.upcomingEvents
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
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
            div(classes = "fixed z-20 top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 hidden p-4 rounded-lg shadow-lg bg-white bg-opacity-90 border border-blue-200") {
                id = "alert-box"
                span(classes = "text-gray-800 font-semibold") {
                    id = "alert-message"
                }
                span(classes = "ml-4 cursor-pointer text-red-500 hover:text-red-700 transition-colors duration-300") {
                    onClick = "closeAlert()"
                    svgIcon(SvgIcon.CLOSE, classes = "w-5 h-5")
                }
            }

            div (classes = "w-full flex flex-col justify-center items-center relative") {
                id = "main-content-bg"
                // Back to Home Icon
                div(classes = "fixed top-[15%] left-[15%] transform -translate-y-1/2 text-blue-500 hover:text-blue-700 hover:bg-blue-300 transition-colors duration-300 mt-8 mr-3 bg-blue-100 rounded-full p-2 shadow-md cursor-pointer") {
                    attributes["onclick"] = "navigate()"
                    svgIcon(SvgIcon.ARROW_LEFT, classes = "w-6 h-6")
                }
                // content
                div( classes = "flex flex-col justify-center items-center w-[70%] mt-1 py-2 px-4") {
                    id = "main-content"
                    upcomingEvents(eventRepository, true)
                }
            }


        }
        loadJs("home")
    }
}