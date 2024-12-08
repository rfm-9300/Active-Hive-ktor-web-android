package example.com.views.topbar

import kotlinx.html.*
import java.io.File

fun HtmlBlockTag.logoMenu() {
    div (classes = "w-20 h-20 bg-orange-500 absolute top-full left-0 z-10 hidden") {
        id = "dropdown"
        +"test"
    }
}