package example.com.data.db.user

interface UserRepository {
    suspend fun getUser(username: String): User?
    suspend fun addUser(user: User) : Boolean
}