package example.com.views.topbar

import example.com.views.svgIcon
import example.com.views.utils.Strings
import kotlinx.html.*

fun HtmlBlockTag.topbar() {
    val title = Strings.Home.COMMUNITY_NAME

    div(classes = "flex items-center justify-between relative") {
        // logo container
        div (classes = "h-auto, flex items-center cursor-pointer rounded-xl hover:bg-gray-200 hover:text-gray-900 transition-all duration-300") {
            id = "logo-container"
            div(classes = "relative overflow-hidden w-12 h-12 rounded-lg"){
                span {
                    img(classes = "object-cover w-full h-full", src = "/resources/logo.webp", alt = "Active Hive Logo")
                }
            }
            div (classes = "mx-2") {
                span {
                    +title
                }
            }

        }
        // profile container
        div(classes = "relative") {
            id = "profile-container"
            div(classes = "w-8 h-8 rounded-full overflow-hidden cursor-pointer") {
                svgIcon("menu")
            }
            // profile menu
            profileMenu()
        }
        // hidden menu
        logoMenu()
    }
    script(src = "/resources/js/topBar.js" ) {}


}