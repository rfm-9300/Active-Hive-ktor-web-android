package example.com.views.navbar

import kotlinx.html.*

fun HtmlBlockTag.navbar()  {
    nav(classes = "bg-white shadow px-4 py-2 fixed top-0 left-0 right-0 z-10") {
        div(classes = "max-w-6xl mx-auto flex justify-between items-center") {
            // Logo Section
            div(classes = "flex items-center space-x-2") {
                img(classes = "h-8 w-8", src = "/assets/logo.png", alt = "Logo")
                span(classes = "text-xl font-bold text-gray-800") { +"Event Explorer" }
            }

            // Navigation Links
            ul(classes = "hidden md:flex space-x-8 text-gray-600 font-medium") {
                li {
                    a(href = "/", classes = "hover:text-blue-600 transition") { +"Home" }
                }
                li {
                    a(href = "/events", classes = "hover:text-blue-600 transition") { +"Events" }
                }
                li {
                    a(href = "/about", classes = "hover:text-blue-600 transition") { +"About Us" }
                }
                li {
                    a(href = "/contact", classes = "hover:text-blue-600 transition") { +"Contact" }
                }
            }

            // Profile and CTA Button
            div(classes = "flex items-center space-x-4") {
                // Profile Icon
                button(classes = "text-gray-600 hover:text-blue-600 transition") {
                    span(classes = "material-icons") { +"account_circle" }
                }

                // CTA Button
                a(href = "/create-event", classes = "bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition") {
                    +"Create Event"
                }
            }

            // Mobile Menu Icon
            button(classes = "block md:hidden text-gray-600 hover:text-blue-600 transition") {
                span(classes = "material-icons") { +"menu" }
            }
        }
    }
}