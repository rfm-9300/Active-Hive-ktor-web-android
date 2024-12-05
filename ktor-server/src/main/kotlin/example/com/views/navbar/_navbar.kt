package example.com.views.navbar

import kotlinx.html.*

fun HtmlBlockTag.navbar()  {
    nav(classes = "bg-red w-full shadow px-4 py-2 top-0 left-0 right-0 z-10 ") {
        div(classes = "mx-auto max-w-6xl w-full flex justify-between items-center") {
            // Navigation Links
            ul(classes = "hidden md:flex space-x-8 text-gray-600 font-medium mx-auto") {
                li {
                    a(href = "/", classes = "hover:text-blue-600 transition", ) {

                        attributes["hx-target"] = "closest li"
                            +"Home"
                    }
                }
                li {
                    a(classes = "hover:text-blue-600 transition") {
                        attributes["hx-get"] = "/feed"
                        attributes["hx-target"] = "#text"
                        +"Feed"
                    }
                }
                li {
                    a(href = "/about", classes = "hover:text-blue-600 transition") { +"Challenges" }
                }
                li {
                    a(href = "/contact", classes = "hover:text-blue-600 transition") { +"Events" }
                }
                li {
                    a(href = "/logout", classes = "hover:text-blue-600 transition") { +"Chat" }
                }
                li {
                    a(href = "/profile", classes = "hover:text-blue-600 transition") { +"People" }
                }
                li {
                    a(href = "/profile", classes = "hover:text-blue-600 transition") { +"About" }
                }
            }
        }
    }
}