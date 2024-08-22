package rfm.biblequizz.repositories

import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.domain.repository.LoginRepository

class FakeLoginRepository : LoginRepository {

    // Define the initial states or configurations for tests
    private var signUpResult: AuthResult<Unit> = AuthResult.Authorized()
    private var loginResult: AuthResult<Unit> = AuthResult.Authorized()
    private var authenticateResult: AuthResult<Unit> = AuthResult.Authorized()

   //Set the results of the tests
    fun setSignUpResult(result: AuthResult<Unit>) {
        signUpResult = result
    }

    fun setLoginResult(result: AuthResult<Unit>) {
        loginResult = result
    }

    fun setAuthenticateResult(result: AuthResult<Unit>) {
        authenticateResult = result
    }

    override suspend fun signUp(username: String, password: String): AuthResult<Unit> {
        return signUpResult
    }

    override suspend fun login(username: String, password: String): AuthResult<Unit> {
        return loginResult
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return authenticateResult
    }
}
