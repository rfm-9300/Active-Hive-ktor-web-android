package example.com.web.pages.profilePage

import example.com.web.components.SvgIcon
import example.com.web.components.svgIcon
import kotlinx.html.*

fun FlowContent.profileEditBox() {
    div(classes = "absolute bg-white rounded-xl shadow-lg p-6 z-10 top-32 transform scale-0 transition-transform duration-300") {
        id = "profile-edit-box"
        
        // Close button
        div(classes = "absolute top-2 right-2") {
            button(classes = "text-gray-500 hover:text-gray-700") {
                onClick = "hideEditProfile()"
                attributes["aria-label"] = "Close"
                svgIcon(SvgIcon.CLOSE, "w-5 h-5")
            }
        }
        
        // Title
        h3(classes = "text-lg font-medium text-gray-900 mb-4") {
            +"Change Profile Picture"
        }
        
        // Form
        form(
            action = "/api/profile/update-picture",
            encType = FormEncType.multipartFormData,
            method = FormMethod.post,
            classes = "space-y-4"
        ) {
            id = "profile-picture-form"
            attributes["onsubmit"] = "return handleProfilePictureSubmit(event)"
            
            // File input
            div {
                label(classes = "block text-sm font-medium text-gray-700 mb-1") {
                    attributes["for"] = "profile-picture"
                    +"Select Image"
                }
                input(classes = "w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100") {
                    type = InputType.file
                    id = "profile-picture"
                    name = "profile-picture"
                    accept = "image/*"
                    required = true
                }
            }
            
            // Preview (hidden initially)
            div(classes = "hidden") {
                id = "image-preview-container"
                label(classes = "block text-sm font-medium text-gray-700 mb-1") {
                    +"Preview"
                }
                div(classes = "w-32 h-32 rounded-full overflow-hidden border border-gray-200") {
                    img(classes = "w-full h-full object-cover") {
                        id = "image-preview"
                        src = ""
                        alt = "Preview"
                    }
                }
            }
            
            // Submit button
            div(classes = "pt-2") {
                button(classes = "w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500") {
                    type = ButtonType.submit
                    +"Upload Picture"
                }
            }
        }
    }
}