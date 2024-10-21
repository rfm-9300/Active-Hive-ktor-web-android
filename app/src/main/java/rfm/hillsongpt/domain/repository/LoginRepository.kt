package rfm.hillsongpt.domain.repository

import rfm.hillsongpt.data.model.LoginResult
import rfm.hillsongpt.domain.model.Result

interface LoginRepository {
    suspend fun signUp(username: String, password: String): Result<LoginResult<Unit>>
    suspend fun login(username: String, password: String): Result<LoginResult<Unit>>
    suspend fun authenticate(): Result<LoginResult<Unit>>
}