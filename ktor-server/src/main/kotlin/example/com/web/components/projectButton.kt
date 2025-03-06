package example.com.web.components

import kotlinx.html.HtmlBlockTag
import kotlinx.html.button
import kotlinx.html.classes

fun HtmlBlockTag.projectButton(
    text: String = "Join Event",
    onClick: String? = null,
    hxGet: String? = null, // Optional htmx hx-get attribute
    hxTarget: String? = null, // Optional htmx hx-target attribute
    extraClasses: String = "",
    disabled: Boolean = false
) {
    button(classes = "bg-gradient-to-r from-blue-500/70  to-indigo-600/60 hover:from-blue-600 hover:to-indigo-700 text-white font-semibold py-3 px-6 rounded-lg shadow-md focus:outline-none focus:ring-2 focus:ring-blue-300 transition-all duration-200 $extraClasses") {
        if (onClick != null) {
            attributes["onclick"] = onClick
        }
        if (hxGet != null) {
            attributes["hx-get"] = hxGet
        }
        if (hxTarget != null) {
            attributes["hx-target"] = hxTarget
        }
        if (disabled) {
            attributes["disabled"] = "true"
            classes = classes + " opacity-50 cursor-not-allowed"
        }
        +text
    }
}