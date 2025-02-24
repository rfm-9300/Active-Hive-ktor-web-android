package example.com.web.pages.profilePage

import example.com.data.db.user.UserProfile
import example.com.data.utils.monthYear
import example.com.web.components.layout.layout
import kotlinx.html.*

fun HTML.profilePage(user: UserProfile){
    val userName = "${user.firstName} ${user.lastName}"
    val joinString = "Joined on ${user.joinedAt?.monthYear()}"
    val hostedEvents = user.hostedEvents.size
    val attendedEvents = user.attendedEvents.size
    layout {
        div (classes = "flex flex-col w-full items-center justify-center"){
            // profile info div
            div(classes = "flex flex-row items-center w-full p-4 rounded-xl"){
                img(src = "/resources/images/profile.jpg", classes = "w-20 h-20 rounded-full"){}

                // user info
                div(classes = "flex flex-col ml-4"){
                    p(classes = "font-bold text-xl") { +userName }
                    p(classes = "text-sm") { +joinString }
                    p (classes = "text-sm text-gray-500 mt-2"){
                        +"$hostedEvents hosted  $attendedEvents attended"
                    }
                }
            }
        }
    }
}