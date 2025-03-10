package example.com.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class ApproveUserEventRequest(
    val eventId: Int,
    val userId: Int
)