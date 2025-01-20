package example.com.web.components.post

import example.com.data.db.event.Event
import kotlinx.html.HtmlBlockTag
import kotlinx.html.div
import kotlinx.html.img
import kotlinx.html.p

fun HtmlBlockTag.event(event: Event){
    div(classes = "flex flex-col items-center justify-center w-full p-4") {
        div(classes = "w-full p-4 bg-white rounded-lg shadow-lg") {
            div(classes = "flex flex-col items-center justify-center w-full") {
                div(classes = "flex flex-row items-center justify-between w-full") {
                    div(classes = "flex flex-row items-center") {
                        img(classes = "w-12 h-12 rounded-full") {
                            src = "/resources/images/profile.png"
                        }
                        div(classes = "flex flex-col items-start justify-center ml-4") {
                            p(classes = "text-lg font-bold") {
                                +event.title
                            }
                            p(classes = "text-sm text-gray-500") {
                                +event.date.toString()
                            }
                        }
                    }
                }
                p(classes = "text-lg mt-4") {
                    +event.description
                }
            }
        }
    }
}