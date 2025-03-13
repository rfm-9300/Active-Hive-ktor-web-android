package example.com.web.pages.profilePage

import kotlinx.html.*

fun FlowContent.profileEditBox() {
    div(classes = "fixed top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white rounded-xl shadow-xl p-6 w-96 z-50 hidden") {
        id = "profile-edit-box"
        
        // Close button
        div(classes = "absolute top-3 right-3 cursor-pointer text-gray-500 hover:text-gray-700") {
            onClick = "hideEditProfile()"
            span(classes = "text-2xl") { +"×" }
        }
        
        // Title
        h3(classes = "text-xl font-semibold text-gray-800 mb-4") {
            +"Update Profile Picture"
        }
        
        // Form
        form(classes = "space-y-4") {
            encType = FormEncType.multipartFormData
            method = FormMethod.post
            action = "#"
            
            // File input
            div(classes = "flex flex-col") {
                label(classes = "text-sm font-medium text-gray-700 mb-1") {
                    htmlFor = "image"
                    +"Select new image"
                }
                div(classes = "relative border border-gray-300 rounded-md px-3 py-2 shadow-sm focus-within:ring-1 focus-within:ring-blue-600 focus-within:border-blue-600") {
                    input(classes = "block w-full border-0 p-0 text-gray-900 placeholder-gray-500 focus:ring-0 sm:text-sm") {
                        type = InputType.file
                        name = "image"
                        id = "image"
                        accept = "image/*"
                    }
                }
                p(classes = "mt-1 text-xs text-gray-500") {
                    +"JPG, PNG or GIF files up to 5MB"
                }
            }

            // Image preview and cropping area
            div(classes = "mt-4 hidden") {
                id = "image-cropper-container"
                img( src = "", alt = "Image Preview", classes = "max-w-full h-[300px]"){
                    id = "image-preview"
                }
            }
            
            // Submit button
            div(classes = "flex justify-end") {
                button(classes = "inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500") {
                    attributes["type"] = "button"
                    onClick = "submitProfileEdit()"
                    +"Save"
                }
            }
        }
    }
    
    // Add the script for showing/hiding the edit box

}