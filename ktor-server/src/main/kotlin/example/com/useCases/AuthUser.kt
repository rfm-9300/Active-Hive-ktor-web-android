package example.com.useCases

import example.com.data.db.user.UserRepository
import example.com.data.db.user.User
import example.com.data.db.user.AuthProvider
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import java.security.MessageDigest
import java.util.*

class AuthUser(
    private val userRepository: UserRepository, 
    private val httpClient: HttpClient = HttpClient(CIO) // Specify CIO engine explicitly
) {
    private val secret = "your-jwt-secret-key" // Replace with your actual secret
    
    suspend fun authenticateUser(email: String, password: String): User? {
        val user = userRepository.getUser(email) ?: return null
        
        // If user is from Google, don't allow password authentication
        if (user.authProvider == AuthProvider.GOOGLE) {
            return null
        }
        
        val hashedPassword = hashPassword(password, user.salt)
        return if (hashedPassword == user.password) user else null
    }
    
    suspend fun authenticateGoogleUser(idToken: String): User? {
        // Verify the token with Google
        val googleUserInfo = verifyGoogleToken(idToken) ?: return null
        
        // Extract user information from Google response
        val googleId = googleUserInfo["sub"]?.jsonPrimitive?.content ?: return null
        val email = googleUserInfo["email"]?.jsonPrimitive?.content ?: return null
        val firstName = googleUserInfo["given_name"]?.jsonPrimitive?.content ?: ""
        val lastName = googleUserInfo["family_name"]?.jsonPrimitive?.content ?: ""
        val pictureUrl = googleUserInfo["picture"]?.jsonPrimitive?.content ?: ""
        
        // Check if user exists by Google ID
        val existingUser = userRepository.getUserByGoogleId(googleId)
        if (existingUser != null) {
            return existingUser
        }
        
        // Create or update Google user
        return userRepository.createOrUpdateGoogleUser(
            email = email,
            googleId = googleId,
            firstName = firstName,
            lastName = lastName,
            profileImageUrl = pictureUrl
        )
    }
    
    private suspend fun verifyGoogleToken(idToken: String): JsonObject? {
        return try {
            // Google endpoint to verify token
            val url = "https://oauth2.googleapis.com/tokeninfo?id_token=$idToken"
            
            val response = withContext(Dispatchers.IO) {
                httpClient.get(url)
            }
            
            if (response.status.isSuccess()) {
                val responseText = response.bodyAsText()
                Json.parseToJsonElement(responseText).jsonObject
            } else {
                null
            }
        } catch (e: Exception) {
            println("Error verifying Google token: ${e.message}")
            null
        }
    }
    
    fun generateJwtToken(user: User): String {
        // Replace with your actual JWT generation logic
        // This is just a placeholder - you should use a proper JWT library
        val header = Base64.getEncoder().encodeToString("""{"alg":"HS256","typ":"JWT"}""".toByteArray())
        val payload = Base64.getEncoder().encodeToString("""{"userId":${user.id},"email":"${user.email}"}""".toByteArray())
        val signature = signToken("$header.$payload", secret)
        return "$header.$payload.$signature"
    }
    
    fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val saltedPassword = password + salt
        val hashedBytes = md.digest(saltedPassword.toByteArray())
        return hashedBytes.joinToString("") { "%02x".format(it) }
    }
    
    fun generateSalt(): String {
        return UUID.randomUUID().toString()
    }
    
    private fun signToken(data: String, secret: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val signature = md.digest((data + secret).toByteArray())
        return Base64.getEncoder().encodeToString(signature)
    }
}