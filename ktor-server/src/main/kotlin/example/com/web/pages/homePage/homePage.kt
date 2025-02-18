package example.com.web.pages.homePage

import example.com.web.components.layout.layout
import example.com.web.pages.homePage.navbar.navbar
import example.com.web.components.topbar.topbar
import example.com.web.loadJs
import example.com.web.pages.homePage.homeTab.homeTab
import kotlinx.html.*

fun HTML.homePage() {
    layout {
        topbar()
        div (classes = "px-4 ") {
            div(classes = "flex flex-col justify-center items-center w-full bg-stone-100 rounded-2xl shadow-lg") {
                // Navigation Bar
                navbar()

                // Alert Box
                div(classes = "fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 hidden p-4 rounded-lg shadow-lg") {
                    id = "alert-box"
                    span {
                        id = "alert-message"
                    }
                    span(classes = "ml-4 cursor-pointer") {
                        onClick = "closeAlert()"
                        +"×"
                    }
                }

                // content
                div( classes = "flex flex-col justify-center items-center w-[60%] mt-1 py-2 px-4") {
                    id = "main-content"
                    homeTab()
                }
            }
        }
        loadJs("home")
    }
}