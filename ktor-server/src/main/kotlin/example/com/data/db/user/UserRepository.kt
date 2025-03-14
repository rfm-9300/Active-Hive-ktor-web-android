package example.com.data.db.user

interface UserRepository {
    suspend fun getUser(email: String): User?
    suspend fun getUserById(userId: Int): User?
    suspend fun addUser(user: User) : Boolean
    suspend fun getUserProfile(userId: Int): UserProfile?
    suspend fun updateUserProfile(userProfile: UserProfile): Boolean
    
    // Google authentication methods
    suspend fun getUserByGoogleId(googleId: String): User?
    suspend fun createOrUpdateGoogleUser(
        email: String, 
        googleId: String, 
        firstName: String = "", 
        lastName: String = "", 
        profileImageUrl: String = ""
    ): User?
}