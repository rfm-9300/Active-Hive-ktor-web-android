package example.com.web.components.topbar

import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.projectButton
import example.com.web.components.svgIcon
import example.com.web.utils.Strings
import kotlinx.html.*

fun HtmlBlockTag.topbar() {

    div(classes = "w-full flex items-center justify-between px-4 backdrop-blur-sm transition-all duration-300") {
        style = "border-bottom: 1px solid rgba(0, 0, 0, 0.05);"
        // logo container
        div (classes = "h-auto flex items-center cursor-pointer rounded-xl hover:bg-gray-200 hover:text-gray-900 transition-all duration-300") {
            attributes["hx-get"] = Routes.Ui.Event.LIST_UPCOMING
            attributes["hx-target"] = "#main-content"
            id = "logo-container"
            div(classes = "relative overflow-hidden w-auto h-[60px] rounded-lg"){
                span {
                    img(classes = "object-cover w-full h-full", src = "/resources/logo.webp", alt = "Active Hive Logo")
                }
            }
        }
        // profile container
        div(classes = "relative flex flex-row items-center gap-2") {
            id = "profile-container"

            // login button
            projectButton("Sign in", hxGet = "/login", hxTarget = "#main-content", extraClasses = "py-2", buttonId = "login-button")


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