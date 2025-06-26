package rfm.hillsongpt.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest

data class GoogleAccount(
    val token: String,
    val displayName: String = "",
    val profileImageUrl: String? = null
)

class GoogleAuthUiProvider(
    private val activityContext: Context,
    private val credentialManager: CredentialManager
) {
    suspend fun signIn(): GoogleAccount? = try {
        val result = credentialManager.getCredential(
            context = activityContext,
            request = getCredentialRequest()
        )

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
        GoogleAccount(
            token = googleIdTokenCredential.idToken,
            displayName = googleIdTokenCredential.displayName ?: "",
            profileImageUrl = googleIdTokenCredential.profilePictureUri?.toString()
        )
    } catch (e: Exception) {
        null
    }

    private fun getCredentialRequest(): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId("Add your Google web client id")
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }
}

class GoogleAuthProvider {
    @Composable
    fun getUiProvider(): GoogleAuthUiProvider {
        TODO("Not yet implemented")
    }

    suspend fun signOut() {
        // Implement sign out logic
    }
}