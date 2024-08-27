package rfm.biblequizz.domain.repository

import rfm.biblequizz.data.model.LoginResult
import rfm.biblequizz.domain.model.Result

interface LoginRepository {
    suspend fun signUp(username: String, password: String): Result<LoginResult<Unit>>
    suspend fun login(username: String, password: String): Result<LoginResult<Unit>>
    suspend fun authenticate(): Result<LoginResult<Unit>>
}