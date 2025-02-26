package example.com.web.components.topbar

import example.com.data.db.user.UserProfile
import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import kotlinx.html.*


fun HtmlBlockTag.profileMenu(user: UserProfile) {
    img(classes = "object-cover w-full h-full", src = "/resources/uploads/images/${user.profileImagePath}", alt = "Active Hive Logo")

    // toggle menu
    div(classes = "flex flex-col gap-1 w-72 absolute -right-2 top-full hidden rounded-lg bg-white mt-3 shadow-lg text-gray-700") {
        id = "profile-menu-dropdown"

        // icon name and email div
        div(classes = "flex flex-row items-center hover:bg-gray-200 cursor-pointer px-2 py-1") {
            // user profile image/icon
            img(classes = "object-cover w-12 h-12 rounded-full", src = "/resources/images/default-user-image.webp", alt = "Active Hive Logo")

            // username and email
            div (classes = "flex flex-col ml-4") {
                span(classes = "text-gray-700 font-bold") {
                    +user.firstName
                }
                // user email
                span(classes = "text-gray-700") {
                    +user.email
                }
            }
        }

        // divider
        div(classes = "border-b border-gray-300") {}

        // buttons
        div(classes = "flex flex-col gap-2") {
            // profile button
            div(classes = "hover:bg-gray-200 cursor-pointer px-2 py-1 rounded-lg") {
                attributes["hx-get"] = Routes.Ui.Profile.ROOT
                attributes["hx-target"] = "#main-content"
                span(classes = "text-gray-700") {
                    +"Profile"
                }
            }

            // settings button
            div(classes = "hover:bg-gray-200 cursor-pointer px-2 py-1 rounded-lg") {
                span(classes = "text-gray-700") {
                    +"Settings"
                }
            }

            // logout button
            div(classes = "hover:bg-gray-200 cursor-pointer px-2 py-1 rounded-lg") {
                onClick = "logout()"
                span(classes = "text-gray-700") {
                    +"Logout"
                }
            }
        }
    }
}

