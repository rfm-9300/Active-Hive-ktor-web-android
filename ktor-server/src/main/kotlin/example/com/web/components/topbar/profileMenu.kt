package example.com.web.components.topbar

import example.com.data.db.user.UserProfile
import example.com.routes.Routes
import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import kotlinx.html.*


fun HtmlBlockTag.profileMenu(user: UserProfile) {
    img(classes = "object-cover w-full h-full", src = "/resources/uploads/images/${user.profileImagePath}", alt = "Active Hive Logo")

    // toggle menu
    div(classes = "flex flex-col gap-2 w-80 absolute -right-2 top-full hidden rounded-xl bg-white mt-3 shadow-lg text-gray-700 border border-blue-100 overflow-hidden transition-all duration-300") {
        id = "profile-menu-dropdown"

        // Header with user info
        div(classes = "bg-gradient-to-r from-blue-50 to-indigo-50 p-4") {
            // icon name and email div
            div(classes = "flex flex-row items-center") {
                // user profile image/icon
                div(classes = "w-14 h-14 rounded-full overflow-hidden border-2 border-white shadow-md") {
                    img(classes = "object-cover w-full h-full", src = "/resources/uploads/images/${user.profileImagePath}", alt = "${user.firstName}'s profile")
                }

                // username and email
                div(classes = "flex flex-col ml-4") {
                    span(classes = "text-gray-800 font-bold text-lg") {
                        +user.firstName
                    }
                    // user email
                    span(classes = "text-gray-600 text-sm") {
                        +user.email
                    }
                }
            }
        }

        // Menu items container
        div(classes = "flex flex-col p-2") {
            // Profile button
            div(classes = "flex items-center gap-3 hover:bg-blue-50 cursor-pointer px-4 py-3 rounded-lg transition-colors duration-200") {
                attributes["hx-get"] = Routes.Ui.Profile.ROOT
                attributes["hx-target"] = "#main-content"
                svgIcon(SvgIcon.PROFILE, classes = "w-5 h-5 text-blue-600")
                span(classes = "text-gray-700 font-medium") {
                    +"My Profile"
                }
            }

            // Settings button
            div(classes = "flex items-center gap-3 hover:bg-blue-50 cursor-pointer px-4 py-3 rounded-lg transition-colors duration-200") {
                svgIcon(SvgIcon.SETTINGS, classes = "w-5 h-5 text-blue-600")
                span(classes = "text-gray-700 font-medium") {
                    +"Settings"
                }
            }

            // Divider
            div(classes = "border-t border-gray-200 my-2") {}

            // Logout button
            div(classes = "flex items-center gap-3 hover:bg-red-50 cursor-pointer px-4 py-3 rounded-lg transition-colors duration-200") {
                onClick = "logout()"
                svgIcon(SvgIcon.LOGOUT, classes = "w-5 h-5 text-red-500")
                span(classes = "text-red-600 font-medium") {
                    +"Logout"
                }
            }
        }
    }
}

