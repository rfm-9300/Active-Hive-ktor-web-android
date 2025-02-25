package example.com.web.pages.profilePage

import example.com.data.db.user.UserProfile
import example.com.data.utils.monthYear
import example.com.web.components.layout.layout
import example.com.web.loadJs
import kotlinx.html.*

fun HTML.profilePage(user: UserProfile){
    val userName = "${user.firstName} ${user.lastName}"
    val joinString = "Joined on ${user.joinedAt?.monthYear()}"
    val hostedEvents = user.hostedEvents.size
    val attendedEvents = user.attendedEvents.size
    layout {
        div (classes = "flex flex-col w-full items-center justify-center relative"){
            // hidden floating box change profile picture
            profileEditBox()


            // profile info div
            div(classes = "flex flex-row items-center w-full p-4 rounded-xl"){
                div(classes = "relative group") {
                    img(
                        src = "/resources/uploads/images/${user.profileImagePath}",
                        classes = "w-20 h-20 rounded-full"
                    )

                    // Edit button (hidden by default, shown on hover)
                    button(classes = "absolute inset-0 flex items-center justify-center w-20 h-20 " +
                                "rounded-full bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 " +
                                "transition-opacity duration-200 text-white") {
                        onClick = "showEditProfile()"
                        +"Edit"
                    }
                }


                // user info
                div(classes = "flex flex-col ml-4"){
                    p(classes = "font-bold text-xl") { +userName }
                    p(classes = "text-sm") { +joinString }
                    p (classes = "text-sm text-gray-500 mt-2"){
                        +"$hostedEvents hosted  $attendedEvents attended"
                    }
                }
            }
            loadJs("profile-page")
        }
    }
}