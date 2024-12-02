package example.com.routes

import example.com.views.home.homePage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title

fun Route.landingRoute(){
    get("/") {
        val name = "Hive"
        call.respondHtml(HttpStatusCode.OK){
            homePage()
        }
    }
}