package example.com.web.components.layout


import example.com.web.loadHeaderScripts
import kotlinx.html.*

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        // Add Tailwind CSS CDN
        script(src = "https://cdn.tailwindcss.com") {}
        script (src = "/test/resources/js/ApiClient.js"){}
        script (src = "/resources/js/main/htmx.js"){}
        //loadHeaderScripts()
    }

    body {
        e()
    }
}