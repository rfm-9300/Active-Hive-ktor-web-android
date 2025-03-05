package example.com.web.components.topbar

import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import example.com.web.utils.Strings
import kotlinx.html.*

fun HtmlBlockTag.topbar() {
    val title = Strings.Home.COMMUNITY_NAME

    div(classes = "w-full flex items-center justify-between relative px-4") {
        // logo container
        div (classes = "h-auto, flex items-center cursor-pointer rounded-xl hover:bg-gray-200 hover:text-gray-900 transition-all duration-300") {
            attributes["hx-get"] = Routes.Ui.Event.LIST
            attributes["hx-target"] = "#main-content"
            id = "logo-container"
            div(classes = "relative overflow-hidden w-auto h-[65px] rounded-lg"){
                span {
                    img(classes = "object-cover w-full h-full", src = "/resources/logo.webp", alt = "Active Hive Logo")
                }
            }
        }
        // profile container
        div(classes = "relative flex flex-row items-center gap-2") {
            id = "profile-container"

            // login button
            div(classes = "bg-blue-200 text-gray px-2 py-1 rounded-md cursor-pointer opacity-75 hover:opacity-100 text-sm") {
                attributes["hx-get"] = "/login"
                attributes["hx-target"] = "#main-content"
                id = "login-button"
                span {
                    +"Sing in"
                }
            }

            // div that will contain the user profile image and toggle the menu
            div(classes = "w-8 h-8 rounded-full overflow-hidden cursor-pointer z-20") {
                id = "user-profile-icon"
            }
            div(classes = "w-4 h-4 rounded-full overflow-hidden cursor-pointer") {
                svgIcon(SvgIcon.MENU)
            }

        }
        // hidden menu
        logoMenu()
    }
}