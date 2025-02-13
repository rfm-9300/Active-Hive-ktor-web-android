package example.com.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val success : Boolean,
    val message : String,
    val data: ApiResponseData? = null
)

@Serializable
sealed class ApiResponseData {
    @Serializable
    data class AuthResponse (val token: String) : ApiResponseData()
}