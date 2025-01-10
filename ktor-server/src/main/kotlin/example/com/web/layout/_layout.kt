package example.com.web.layout


import kotlinx.html.*

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        // Add Tailwind CSS CDN
        script(src = "https://cdn.tailwindcss.com") {}

        // Add Htmx path
        script(src = "/resources/htmx.js") {}

    }

    body {
        e()
    }
}