package example.com.routes


import example.com.data.db.user.UserRepository
import example.com.plugins.Logger
import example.com.web.pages.profilePage.profilePage
import io.ktor.http.*
import io.ktor.server.html.*
import io.ktor.server.routing.*

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

}


