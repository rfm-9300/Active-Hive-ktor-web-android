package example.com.web.components

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.id

fun FlowContent.eventFilterTag(text: String, targetId: String, active: Boolean = false) {
    val tabId = text.lowercase() + "-tab"
    val activeClasses = if (active) {
        "text-blue-600 border-b-2 border-blue-600 font-medium"
    } else {
        "text-gray-500 hover:text-blue-500 hover:border-blue-300 border-b-2 border-transparent"
    }
    
    a(href = "#", classes = "px-6 py-3 inline-block transition-colors duration-300 $activeClasses") {
        id = tabId
        attributes["onclick"] = "switchTab('${text.lowercase()}')"
        +text
    }
}