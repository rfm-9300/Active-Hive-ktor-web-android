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
}