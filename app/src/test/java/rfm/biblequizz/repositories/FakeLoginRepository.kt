package rfm.biblequizz.repositories

import rfm.biblequizz.data.model.LoginResult
import rfm.biblequizz.domain.repository.LoginRepository
import rfm.biblequizz.domain.model.Result

class FakeLoginRepository : LoginRepository {

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun signUp(username: String, password: String): Result<LoginResult<Unit>> {
        return if (shouldReturnError) {
            Result.Error(LoginResult.Unauthorized())
        } else {
            Result.Success(LoginResult.Authorized())
        }
    }

    override suspend fun login(username: String, password: String): Result<LoginResult<Unit>> {
        return if (shouldReturnError) {
            Result.Error(LoginResult.Unauthorized())
        } else {
            Result.Success(LoginResult.Authorized())
        }
    }

    override suspend fun authenticate(): Result<LoginResult<Unit>> {
        return if (shouldReturnError) {
            Result.Error(LoginResult.Unauthorized())
        } else {
            Result.Success(LoginResult.Authorized())
        }
    }
}

