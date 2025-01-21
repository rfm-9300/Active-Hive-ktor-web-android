package example.com.data.utils

data class UserSession(
    val userId: Int,
    val userName: String,
    val email: String,
    val token: String
) {
}