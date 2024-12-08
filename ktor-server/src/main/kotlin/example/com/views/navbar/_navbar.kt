package example.com.views.navbar

import kotlinx.html.*

fun HtmlBlockTag.navbar()  {
    nav(classes = "bg-red w-full shadow px-4 py-2 top-0 left-0 right-0 z-10 ") {
        div(classes = "mx-auto max-w-6xl w-full flex justify-between items-center") {
            // Navigation Links
            ul(classes = "hidden md:flex space-x-8 text-gray-600 font-medium mx-auto") {
                val tabs = mapOf(
                    "Home" to "/home",
                    "Feed" to "/feed",
                    "Events" to "/events",
                    "People" to "/people",
                    "About" to "/about",
                )
                tabs.forEach { (label, url) ->
                    li(classes = "cursor-pointer hover:text-blue-600 transition"){
                        attributes["hx-get"] = url
                        attributes["hx-target"] = "#main-content"
                        +label
                    }
                }

            }
        }
    }
}