package example.com.views.layout


import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.html.*
import java.io.File

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        // Add Tailwind CSS CDN
        script(src = "https://cdn.tailwindcss.com") {}

        link(rel = "stylesheet", href = "/styles.css", type = "text/css")
        //script(src = "https://unpkg.com/htmx.org@2.0.3/dist/htmx.js") {}

        // Add Htmx path
        script(src = "/file/htmx.js") {}

    }

    body {
        e()
    }
}

fun Application.configureHtmx() = routing {
    staticFiles("/file", File("files")){
        default("htmx.js")
    }
}