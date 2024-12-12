package example.com.views.login

import example.com.views.layout.layout
import kotlinx.html.*

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
                        attributes["for"] = "username"
                        +"Username"
                    }
                    input(classes = "mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50") {
                        attributes["type"] = "text"
                        attributes["name"] = "username"
                        attributes["id"] = "username"
                        attributes["required"] = "true"
                        attributes["placeholder"] = "Enter your username"
                    }
                }

                div {
                    label(classes = "block text-sm font-medium text-gray-700") {
                        attributes["for"] = "password"
                        +"Password"
                    }
                    input(classes = "mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50") {
                        attributes["type"] = "password"
                        attributes["name"] = "password"
                        attributes["id"] = "password"
                        attributes["required"] = "true"
                        attributes["placeholder"] = "Enter your password"
                    }
                }

                div {
                    button(classes = "w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500") {
                        attributes["type"] = "submit"
                        +"Sign In"
                    }
                }

                div(classes = "text-center") {
                    p(classes = "mt-2 text-sm text-gray-600") {
                        +"Don't have an account? "
                        a(href = "/register", classes = "font-medium text-indigo-600 hover:text-indigo-500") {
                            +"Register"
                        }
                    }
                }
            }
        }
        script(src = "/resources/js/login.js" ) {}
    }
}
