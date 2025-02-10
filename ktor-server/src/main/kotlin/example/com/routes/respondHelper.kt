package example.com.routes

import example.com.data.responses.CreateEventResponse
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun respondHelper(
    call: RoutingCall,
    success: Boolean,
    message: String,
    statusCode: HttpStatusCode = HttpStatusCode.OK
) {
    call.respond(statusCode, CreateEventResponse(
        success = success,
        message = message
    )
    )
}