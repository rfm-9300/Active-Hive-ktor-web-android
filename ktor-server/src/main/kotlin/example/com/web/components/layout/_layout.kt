package example.com.web.components.layout


import example.com.routes.Routes
import example.com.web.loadHeaderScripts
import kotlinx.html.*

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        // Add Tailwind CSS CDN
        script(src = "https://cdn.tailwindcss.com") {}
        script (src = Routes.DynamicJs.API_CLIENT){}
        // cropper.js
        script(src = "https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.js") {}
        link (rel = "stylesheet", href = "https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.6.1/cropper.min.css")
        loadHeaderScripts()
    }

    body {
        e()
    }
}