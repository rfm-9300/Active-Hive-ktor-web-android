package example.com.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteEventRequest(
    val eventId: Int
)