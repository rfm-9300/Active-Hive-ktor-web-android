package example.com.web.components.layout


import kotlinx.html.*

fun HTML.layout(e: BODY.() -> Unit) {
    head {
        // Add Tailwind CSS CDN
        script(src = "https://cdn.tailwindcss.com") {}

        // Add Htmx path
        script(src = "/resources/htmx.js") {}

        script {
            unsafe {
                +"""
            document.addEventListener('DOMContentLoaded', function() {
                try {
                    const token = localStorage.getItem('authToken');
                    if (token) {
                        htmx.config.headers = {
                            'Authorization': 'Bearer ' + token,
                            'X-User': 'rfm-9300'
                        };
                        console.log('HTMX headers configured successfully');
                    } else {
                        console.warn('No auth token found');
                    }
                } catch (error) {
                    console.error('Error configuring HTMX headers:', error);
                }
            });
        """.trimIndent()
            }
        }

    }

    body {
        e()
    }
}