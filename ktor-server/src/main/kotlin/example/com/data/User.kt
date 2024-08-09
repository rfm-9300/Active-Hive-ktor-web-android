package example.com.data

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val username: String,
    val password: String,
    val salt: String
)