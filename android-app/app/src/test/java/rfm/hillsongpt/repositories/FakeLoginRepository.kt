package rfm.hillsongpt.repositories

import rfm.hillsongpt.data.model.LoginResult
import rfm.hillsongpt.domain.repository.LoginRepository
import rfm.hillsongpt.domain.model.Result

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

