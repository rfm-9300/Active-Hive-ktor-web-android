package example.com.routes

import example.com.data.responses.ApiResponse
import example.com.data.responses.ApiResponseData
import example.com.data.responses.CreateEventResponse
import example.com.security.Roles
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun respondHelper(
    call: RoutingCall,
    success: Boolean,
    message: String,
    data: ApiResponseData? = null,
    statusCode: HttpStatusCode = HttpStatusCode.OK
) {
    call.respond(statusCode, ApiResponse(
        success = success,
        message = message,
        data = data
    ))
}

// return the userID from the JWT token for authenticated routes
suspend fun getIdFromRequest(call: RoutingCall): String? {
    val principal = call.principal<JWTPrincipal>()
    if (principal == null) {
        respondHelper(
            call = call,
            success = false,
            message = "User not found",
            statusCode = HttpStatusCode.Unauthorized
        )
        return null
    }

    val userId = principal.getClaim("userId", String::class)
    if (userId == null) {
        respondHelper(
            call = call,
            success = false,
            message = "User not found",
            statusCode = HttpStatusCode.Unauthorized
        )
        return null
    }
    return userId
}

fun authorizeUser(userId: String): Boolean {
    return Roles.returnRole(userId.toInt()) == Roles.Role.ADMIN
}