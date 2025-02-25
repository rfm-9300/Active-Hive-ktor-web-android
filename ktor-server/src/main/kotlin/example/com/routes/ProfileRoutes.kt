package example.com.routes


import example.com.data.db.user.UserRepository
import example.com.plugins.Logger
import example.com.web.pages.profilePage.profilePage
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray

fun Route.profileRoutes(
    userRepository: UserRepository
){

    /**
     * UI Routes
     */
    get(Routes.Ui.Profile.ROOT) {
        val token = call.request.cookies["authToken"]
        Logger.d("Token: $token")
        val userId = getUserIdFromToken(token) ?: return@get Logger.d("Invalid token")
        val userProfile = userRepository.getUserProfile(userId)
        call.respondHtml(HttpStatusCode.OK){
            profilePage(userProfile)
        }
    }


    /**
     * Api Routes
     */
    post(Routes.Api.Profile.UPDATE){
        try {
            val multiPart = call.receiveMultipart()
            var image = ""

            multiPart.forEachPart {
                when(it){
                    is PartData.FormItem -> {

                    }
                    is PartData.FileItem -> {
                        if(it.name == "image"){
                            val fileName = it.originalFileName ?: "unnamed.jpg"
                            val fileBytes = it.provider().readRemaining().readByteArray()
                            image = ImageFileHandler.saveImage(fileBytes, fileName)
                        }
                    }
                    else -> {}
                }
            }

        }catch (e: Exception){

        }
    }

}


