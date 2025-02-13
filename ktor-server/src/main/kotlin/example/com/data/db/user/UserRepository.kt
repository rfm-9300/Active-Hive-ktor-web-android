package example.com.data.db.user

interface UserRepository {
    suspend fun getUser(email: String): User?
    suspend fun getUserById(userId: Int): User?
    suspend fun addUser(user: User) : Boolean
    suspend fun getUserProfile(userId: Int): UserProfile
}