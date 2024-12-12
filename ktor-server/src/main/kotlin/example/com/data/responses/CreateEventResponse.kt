package example.com.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class CreateEventResponse (
    val message : String,
)