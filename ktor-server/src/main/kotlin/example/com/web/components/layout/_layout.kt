package example.com.web.components.layout


import example.com.routes.Routes
import example.com.web.loadHeaderScripts
import kotlinx.html.*

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        // Site title
        title("Active Hive | Event Management Platform")
        
        // Favicon
        link(rel = "icon", href = "/resources/images/favicon.ico", type = "image/x-icon")
        link(rel = "shortcut icon", href = "/resources/images/favicon.ico", type = "image/x-icon")
        // Additional favicon formats for better browser support
        link(rel = "apple-touch-icon", href = "/resources/images/apple-touch-icon.png")
        link(rel = "icon", type = "image/png", href = "/resources/images/default-user-image.webp") {
            attributes["sizes"] = "32x32"
        }
        link(rel = "icon", type = "image/png", href = "/resources/images/default-user-image.webp") {
            attributes["sizes"] = "16x16"
        }
        
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