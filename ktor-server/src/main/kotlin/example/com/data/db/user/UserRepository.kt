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
    
    // Facebook authentication methods
    suspend fun getUserByFacebookId(facebookId: String): User?
    suspend fun createOrUpdateFacebookUser(
        email: String, 
        facebookId: String, 
        firstName: String = "", 
        lastName: String = "", 
        profileImageUrl: String = ""
    ): User?
    
    // Password reset methods
    suspend fun saveResetToken(email: String, token: String, expiresAt: Long): Boolean
    suspend fun getUserByResetToken(token: String): User?
    suspend fun updatePassword(userId: Int, newPasswordHash: String, newSalt: String): Boolean
    suspend fun deleteResetToken(userId: Int): Boolean
}