package example.com.data

interface UserRepository {
    suspend fun getUser(username: String): User?
    suspend fun addUser(user: User)
}