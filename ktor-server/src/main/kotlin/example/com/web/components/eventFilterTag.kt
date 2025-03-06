package example.com.web.components

import example.com.routes.Routes
import kotlinx.html.HtmlBlockTag
import kotlinx.html.span
import kotlinx.html.style

fun HtmlBlockTag.eventFilterTag(text: String, url: String, active: Boolean = false) {
    val baseClasses = "filter-event-tag px-3 py-2 rounded-lg text-sm font-medium cursor-pointer transition-all duration-300 flex items-center gap-1"
    val activeClasses = "bg-indigo-100 border border-indigo-300 text-indigo-700 shadow-sm"
    val inactiveClasses = "bg-blue-50/50 backdrop-blur-sm border border-blue-200/60 text-indigo-500 hover:border-indigo-300 hover:bg-indigo-50 hover:text-indigo-700"

    span(classes = "$baseClasses ${if (active) activeClasses else inactiveClasses}") {
        attributes["hx-get"] = url
        attributes["hx-target"] = "#main-content"

        +text
    }
}