package example.com.data.user

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val username: String,
    val password: String,
    val salt: String
)