package example.com.web.pages.profilePage

import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import kotlinx.html.*

fun FlowContent.profileEditBox() {
    div(classes = "absolute bg-white rounded-xl shadow-lg p-6 z-10 top-32 transform hidden transition-transform duration-300 border border-yellow-200") {
        id = "profile-edit-box"
        
        // Close button
        div(classes = "absolute top-2 right-2") {
            button(classes = "text-yellow-600 hover:text-yellow-800") {
                onClick = "hideEditProfile()"
                attributes["aria-label"] = "Close"
                svgIcon(SvgIcon.CLOSE, "w-5 h-5")
            }
        }
        
        // Title
        h3(classes = "text-lg font-medium text-black mb-4") {
            +"Change Profile Picture"
        }
        
        // Form
        form(classes = "space-y-4") {
            encType = FormEncType.multipartFormData
            method = FormMethod.post
            action = "#"

            
            // File input
            div {
                label(classes = "block text-sm font-medium text-yellow-800 mb-1") {
                    attributes["for"] = "profile-picture"
                    +"Select Image"
                }
                input(classes = "w-full text-sm text-yellow-800 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-yellow-50 file:text-yellow-700 hover:file:bg-yellow-100") {
                    type = InputType.file
                    id = "profile-picture"
                    name = "profile-picture"
                    accept = "image/*"
                }
            }
            
            // Preview (hidden initially)
            div(classes = "hidden") {
                id = "image-preview-container"
                label(classes = "block text-sm font-medium text-yellow-800 mb-1") {
                    +"Preview"
                }
                div(classes = "w-32 h-32 rounded-full overflow-hidden border border-yellow-200") {
                    img(classes = "w-full h-full object-cover") {
                        id = "image-preview"
                        src = ""
                        alt = "Preview"
                    }
                }
            }
            
            // Submit button
            div(classes = "pt-2") {
                button(classes = "w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-gradient-to-r from-yellow-500 to-black hover:from-yellow-600 hover:to-gray-900 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500") {
                    attributes["type"] = "button"
                    onClick = "submitProfileEdit()"
                    +"Upload Picture"
                }
            }
        }
    }
}