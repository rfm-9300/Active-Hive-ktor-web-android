package rfm.biblequizz.data.repository

import androidx.compose.material3.TimeInput
import retrofit2.HttpException
import rfm.biblequizz.data.local.Roomdb
import rfm.biblequizz.data.local.entity.UserEntity
import rfm.biblequizz.data.log.Timber
import rfm.biblequizz.data.model.AuthRequest
import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.data.remote.LoginApi
import rfm.biblequizz.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val db : Roomdb,
    private val api: LoginApi
): LoginRepository {
    override suspend fun signUp(username: String, password: String): AuthResult<Unit> {
        return try {
            api.signUp(
                request = AuthRequest(
                    username = username,
                    password = password)
            )
            login(username, password)
        } catch (e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun login(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.login(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            Timber.i("Login response: $response")
            val newUser  = UserEntity(
                username = username,
                token = response.token
            )
            db.userDao.save(newUser)
            AuthResult.Authorized()
        } catch(e: HttpException) {
            if(e.code() == 401) {
                Timber.i("Login error: Unauthorized")
                AuthResult.Unauthorized()
            } else {
                Timber.i("Login error: Unknown")
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Timber.i("Login error: $e")
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val user = db.userDao.get()

            api.authenticate("Bearer ${user.token}")
            AuthResult.Authorized()

        } catch(e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}