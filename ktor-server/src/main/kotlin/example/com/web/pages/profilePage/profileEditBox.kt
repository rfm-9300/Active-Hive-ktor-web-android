package example.com.web.pages.profilePage

import kotlinx.html.*

fun HtmlBlockTag.profileEditBox() {
    div(classes = "w-[90%] z-10 absolute p-4 rounded-lg hidden") {
        id = "floating-profile-page"
        form(
            action = "#",
            method = FormMethod.post,
            classes = "w-full bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4",
            encType = FormEncType.multipartFormData
        ) {
            id = "profile-edit-form"
            // Image upload
            div(classes = "mb-4") {
                label(classes = "block text-gray-700 text-sm font-bold mb-2") {
                    attributes["for"] = "image"
                    +"Profile Image"
                }
                div(classes = "mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md") {
                    div(classes = "flex flex-col space-y-1 text-center") {
                        div(classes = "flex text-sm text-gray-600 justify-center items-center") {
                            label(classes = "relative cursor-pointer bg-white rounded-md font-medium text-blue-600 hover:text-blue-500 items-center") {
                                input(classes = "sr-only") {
                                    attributes["type"] = "file"
                                    attributes["name"] = "image"
                                    attributes["id"] = "image"
                                    attributes["accept"] = "image/*"
                                }
                                div(classes = "items-center") {
                                    span { +"Upload a file"}
                                    span(classes = "pl-1") { +" or drag and drop" }
                                }
                            }
                        }
                        p(classes = "text-xs text-gray-500") {
                            id = "image-upload-text"
                            +"PNG, JPG, GIF up to 10MB"
                        }
                        p(classes = "text-sm text-green-600 mt-2") {
                            id = "upload-status"
                            +""
                        }
                    }
                }
                // Image preview and cropping area
                div(classes = "mt-4 hidden") {
                    id = "image-cropper-container"
                    img( src = "", alt = "Image Preview", classes = "max-w-full h-[300px]"){
                        id = "image-preview"
                    }
                }
            }

            // Submit button
            div(classes = "flex items-center justify-center") {
                button(classes = "bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded") {
                    attributes["type"] = "button"
                    onClick = "submitProfileEdit()"
                    +"Save"
                }
            }
        }
    }
}