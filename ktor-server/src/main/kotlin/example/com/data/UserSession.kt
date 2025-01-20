package example.com.data

data class UserSession(
    val userId: Int,
    val userName: String,
    val email: String,
    val token: String
) {
}