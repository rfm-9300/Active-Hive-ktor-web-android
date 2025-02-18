package example.com.web.pages.profilePage

import example.com.data.db.user.UserProfile
import example.com.data.utils.monthYear
import example.com.web.components.layout.layout
import example.com.web.pages.homePage.navbar.navbar
import example.com.web.components.topbar.topbar
import example.com.web.loadJs
import example.com.web.pages.homePage.homeTab.homeTab
import kotlinx.html.*

fun HTML.profilePage(user: UserProfile){
    val userName = "${user.firstName} ${user.lastName}"
    val joinString = "Joined on ${user.joinedAt?.monthYear()}"
    val hostedEvents = user.hostedEvents.size
    val attendedEvents = user.attendedEvents.size
    layout {
        div (classes = "flex flex-col"){
            // profile info div
            div(classes = "flex flex-row items-center"){
                img(src = "/resources/images/profile.jpg", classes = "w-20 h-20 rounded-full"){}

                // user info
                div(classes = "flex flex-col ml-4"){
                    h1 { +userName }
                    p { +joinString }
                    span { +"Hosted events: $hostedEvents" }
                    span { +"Attended events: $attendedEvents" }
                }
            }
        }
    }
}