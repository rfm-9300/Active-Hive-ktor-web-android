package example.com.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class CreateEventResponse (
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val organizerId: Int
)