package example.com.data.db.user

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: Int? = null,
    val username: String,
    val password: String,
    val salt: String
)