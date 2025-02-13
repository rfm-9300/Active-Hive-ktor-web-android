package example.com.routes

import example.com.data.responses.ApiResponse
import example.com.data.responses.ApiResponseData
import example.com.data.responses.CreateEventResponse
import io.ktor.http.*
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