package example.com.web.components.topbar

import example.com.data.db.user.UserProfile
import kotlinx.html.*


fun HtmlBlockTag.profileMenu(user: UserProfile) {
    img(classes = "object-cover w-full h-full", src = "/resources/images/default-user-image.webp", alt = "Active Hive Logo")
    // toggle menu
    div(classes = "w-72 absolute -right-2 top-full hidden rounded-lg bg-white mt-3 shadow-lg") {  // Adjusted width and position
        id = "profile-menu-dropdown"

        // icon name and email div
        div(classes = "flex flex-row items-center hover:bg-gray-200 cursor-pointer px-2 py-1") {
            // user profile image/icon
            img(classes = "object-cover w-full h-full", src = "/resources/images/default-user-image.webp", alt = "Active Hive Logo")
            // username and email
            div (classes = "flex flex-col ml-2") {
                span(classes = "text-gray-700 font-bold") {
                    +user.firstName
                }
                // user email
                span(classes = "text-gray-700") {
                    +user.email
                }
            }
        }
    }
}

