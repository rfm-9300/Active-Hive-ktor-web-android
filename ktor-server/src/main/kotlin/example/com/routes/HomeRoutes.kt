package example.com.routes

import example.com.views.home.homePage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.h1
import java.io.File

fun Route.homeRoute(){
    get("/") {
        call.respondHtml(HttpStatusCode.OK){
            homePage()
        }
    }
    get("/feed") {
        call.respondHtml{
            body {
                h1 { +"Feed" }
            }
        }
    }
    get("/home") {
        call.respondHtml{
            homePage()
        }
    }
    staticFiles("/resources", File("files")){
        default("htmx.js")
    }
}
