package rfm.biblequizz.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.domain.model.UseCaseResult
import rfm.biblequizz.domain.repository.LoginRepository;

class LoginUseCase (
    private val loginRepository: LoginRepository,
    private val dispatcher:CoroutineDispatcher= Dispatchers.IO
) {
    suspend operator fun invoke(username: String, password: String): UseCaseResult<Unit, String> = withContext(dispatcher) {
        return@withContext try {
            val authResult = loginRepository.login(username, password)
            if (authResult is AuthResult.Authorized) {
                UseCaseResult.Success(Unit)
            } else {
                UseCaseResult.Error("Login failed")
            }
        } catch (e: Exception) {
            UseCaseResult.Error("Login failed")
        }
    }
}