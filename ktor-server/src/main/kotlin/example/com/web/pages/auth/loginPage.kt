package example.com.web.pages.auth

import example.com.routes.Routes
import example.com.web.components.layout.layout
import example.com.web.components.svgIcon
import example.com.web.components.SvgIcon
import example.com.web.loadJs
import kotlinx.html.*
import kotlinx.html.InputType.*

fun HTML.loginPage() {
    layout {
        div(classes = "bg-white p-8 rounded-lg shadow-md w-96") {
            form(
                action = "#",
                method = FormMethod.post,
                classes = "space-y-6"
            ) {
                id = "login-form"
                h2(classes = "text-center text-2xl font-bold text-gray-800") {
                    +"Login to Your Account"
                }

                div {
                    label(classes = "block text-sm font-medium text-gray-700") {
                        attributes["for"] = "email"
                        +"Email"
                    }
                    input(classes = "mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50") {
                        id = "email"
                        type = text
                        name = "email"
                        required = true
                        placeholder = "Enter your email"
                    }
                }

                div {
                    label(classes = "block text-sm font-medium text-gray-700") {
                        attributes["for"] = "password"
                        +"Password"
                    }
                    input(classes = "mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50") {
                        id = "password"
                        type = password
                        name = "password"
                        required = true
                        placeholder = "Enter your password"
                    }
                }

                div {
                    button(classes = "w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500") {
                        type = ButtonType.submit
                        +"Sign In"
                    }
                }
                
                // Social login separator
                /*div(classes = "relative flex items-center justify-center mt-6") {
                    div(classes = "border-t border-gray-300 flex-grow") {}
                    div(classes = "text-sm text-gray-500 px-3") {
                        +"Or continue with"
                    }
                    div(classes = "border-t border-gray-300 flex-grow") {}
                }
                
                // Google Sign-In button
                div(classes = "mt-6") {
                    button(classes = "w-full flex justify-center items-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500") {
                        id = "google-signin-btn"
                        type = ButtonType.button
                        
                        // Google logo
                        unsafe {
                            +"""
                            <svg class="w-5 h-5 mr-2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <g transform="matrix(1, 0, 0, 1, 27.009001, -39.238998)">
                                    <path fill="#4285F4" d="M -3.264 51.509 C -3.264 50.719 -3.334 49.969 -3.454 49.239 L -14.754 49.239 L -14.754 53.749 L -8.284 53.749 C -8.574 55.229 -9.424 56.479 -10.684 57.329 L -10.684 60.329 L -6.824 60.329 C -4.564 58.239 -3.264 55.159 -3.264 51.509 Z"/>
                                    <path fill="#34A853" d="M -14.754 63.239 C -11.514 63.239 -8.804 62.159 -6.824 60.329 L -10.684 57.329 C -11.764 58.049 -13.134 58.489 -14.754 58.489 C -17.884 58.489 -20.534 56.379 -21.484 53.529 L -25.464 53.529 L -25.464 56.619 C -23.494 60.539 -19.444 63.239 -14.754 63.239 Z"/>
                                    <path fill="#FBBC05" d="M -21.484 53.529 C -21.734 52.809 -21.864 52.039 -21.864 51.239 C -21.864 50.439 -21.724 49.669 -21.484 48.949 L -21.484 45.859 L -25.464 45.859 C -26.284 47.479 -26.754 49.299 -26.754 51.239 C -26.754 53.179 -26.284 54.999 -25.464 56.619 L -21.484 53.529 Z"/>
                                    <path fill="#EA4335" d="M -14.754 43.989 C -12.984 43.989 -11.404 44.599 -10.154 45.789 L -6.734 42.369 C -8.804 40.429 -11.514 39.239 -14.754 39.239 C -19.444 39.239 -23.494 41.939 -25.464 45.859 L -21.484 48.949 C -20.534 46.099 -17.884 43.989 -14.754 43.989 Z"/>
                                </g>
                            </svg>
                            """
                        }
                        +"Sign in with Google"
                    }
                }*/

                div(classes = "text-center mt-6") {
                    p(classes = "text-sm text-gray-600") {
                        +"Don't have an account? "
                        span(classes = "font-medium text-indigo-600 hover:text-indigo-500 cursor-pointer") {
                            attributes["hx-get"] = Routes.Ui.Auth.SIGNUP
                            attributes["hx-target"] = "#main-content"
                            +"Register"
                        }
                    }
                }
            }
        }
        loadJs("auth/login")
    }
}
