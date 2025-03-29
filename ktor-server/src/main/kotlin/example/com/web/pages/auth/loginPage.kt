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
        div(classes = "flex items-center justify-center min-h-[80vh]") {
            div(classes = "bg-white bg-opacity-90 p-8 rounded-xl shadow-lg border border-slate-200 w-[28rem] max-w-full transform transition-all duration-300") {
                form(
                    action = "#",
                    method = FormMethod.post,
                    classes = "space-y-6"
                ) {
                    id = "login-form"
                    
                    // Title with icon
                    div(classes = "text-center mb-6") {
                        div(classes = "inline-flex items-center justify-center w-16 h-16 rounded-full bg-purple-100 mb-4") {
                            svgIcon(SvgIcon.PROFILE, "w-8 h-8 text-purple-600")
                        }
                        h2(classes = "text-2xl font-bold text-slate-800") {
                            +"Welcome Back"
                        }
                        p(classes = "text-slate-500 mt-1") {
                            +"Sign in to your account to continue"
                        }
                    }

                    // Email field
                    div {
                        label(classes = "block text-sm font-medium text-slate-700 mb-1") {
                            attributes["for"] = "email"
                            +"Email"
                        }
                        div(classes = "relative") {
                            // Email icon
                            div(classes = "absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none") {
                                svgIcon(SvgIcon.EMAIL, "w-5 h-5 text-slate-400")
                            }
                            
                            input(classes = "pl-10 block w-full rounded-lg border-slate-300 bg-slate-50 py-3 shadow-sm focus:border-purple-500 focus:ring-purple-500 focus:outline-none") {
                                id = "email"
                                type = text
                                name = "email"
                                required = true
                                placeholder = "Enter your email"
                            }
                        }
                    }

                    // Password field
                    div {
                        label(classes = "block text-sm font-medium text-slate-700 mb-1") {
                            attributes["for"] = "password"
                            +"Password"
                        }
                        div(classes = "relative") {
                            // Lock icon
                            div(classes = "absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none") {
                                svgIcon(SvgIcon.LOCK, "w-5 h-5 text-slate-400")
                            }
                            
                            input(classes = "pl-10 block w-full rounded-lg border-slate-300 bg-slate-50 py-3 shadow-sm focus:border-purple-500 focus:ring-purple-500 focus:outline-none") {
                                id = "password"
                                type = password
                                name = "password"
                                required = true
                                placeholder = "Enter your password"
                            }
                        }
                    }

                    div {
                        button(classes = "w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-lg shadow-sm text-base font-medium text-white bg-purple-600 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500 transition-colors duration-300") {
                            type = ButtonType.submit
                            
                            // Login icon
                            svgIcon(SvgIcon.LOGIN, "w-5 h-5 mr-2")
                            +"Sign In"
                        }
                    }

                    div(classes = "text-center mt-6") {
                        p(classes = "text-sm text-slate-600") {
                            +"Don't have an account? "
                            span(classes = "font-medium text-purple-600 hover:text-purple-500 cursor-pointer transition-colors duration-300") {
                                attributes["hx-get"] = Routes.Ui.Auth.SIGNUP
                                attributes["hx-target"] = "#main-content"
                                +"Register"
                            }
                        }
                    }
                }
            }
        }
        loadJs("auth/login")
    }
}
