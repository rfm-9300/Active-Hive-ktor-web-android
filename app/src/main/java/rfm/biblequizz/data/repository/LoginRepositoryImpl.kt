package rfm.biblequizz.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import rfm.biblequizz.data.local.Roomdb
import rfm.biblequizz.data.local.entity.UserEntity
import rfm.biblequizz.data.log.Timber
import rfm.biblequizz.data.model.LoginRequest
import rfm.biblequizz.data.model.LoginResult
import rfm.biblequizz.data.remote.LoginApi
import rfm.biblequizz.domain.model.Result
import rfm.biblequizz.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val db : Roomdb,
    private val api: LoginApi
): LoginRepository {
    override suspend fun signUp(username: String, password: String): Result<LoginResult<Unit>> = withContext(Dispatchers.IO) {
        return@withContext try {
            api.signUp(
                request = LoginRequest(
                    username = username,
                    password = password)
            )
            login(username, password)
        } catch (e: HttpException) {
            if(e.code() == 401) {
                Result.Error(LoginResult.Unauthorized())
            } else {
                Result.Error(LoginResult.UnknownError())
            }
        } catch (e: Exception) {
            Result.Error(LoginResult.UnknownError())
        }
    }

    override suspend fun login(username: String, password: String): Result<LoginResult<Unit>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = api.login(
                request = LoginRequest(
                    username = username,
                    password = password
                )
            )
            Timber.i("Login response: $response")
            val newUser  = UserEntity(
                username = username,
                token = response.token
            )
            val oldUser = db.userDao.get()
            oldUser?.let {
                db.userDao.delete(oldUser)
            }
            db.userDao.save(newUser)
            Result.Success(LoginResult.Authorized())
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Timber.i("Login error: Unauthorized")
                Result.Error(LoginResult.Unauthorized())
            } else {
                Timber.i("Login error: Unknown")
                Result.Error(LoginResult.UnknownError())
            }
        } catch (e: Exception) {
            Timber.i("Login error: $e")
            Result.Error(LoginResult.UnknownError())
        }
    }

    override suspend fun authenticate(): Result<LoginResult<Unit>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val user = db.userDao.get() ?: return@withContext Result.Error(LoginResult.Unauthorized())
            api.authenticate("Bearer ${user.token}")
            Result.Success(LoginResult.Authorized())

        } catch(e: HttpException) {
            if(e.code() == 401) {
                Result.Error(LoginResult.Unauthorized())
            } else {
                Result.Error(LoginResult.UnknownError())
            }
        } catch (e: Exception) {
            Result.Error(LoginResult.UnknownError())
        }
    }
}