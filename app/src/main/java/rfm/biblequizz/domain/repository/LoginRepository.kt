package rfm.biblequizz.domain.repository

import rfm.biblequizz.data.model.AuthResult

interface LoginRepository {
    suspend fun signUp(username: String, password: String): AuthResult<Unit>
    suspend fun login(username: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
}