package rfm.biblequizz.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.domain.model.UseCaseResult
import rfm.biblequizz.domain.usecase.LoginUseCase

class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var loginUseCase: LoginUseCase

    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `login success updates authResults with Authorized`(): Unit = runTest(testDispatcher) {
        // Arrange
        `when`(loginUseCase.invoke("testUser", "testPass")).thenReturn(UseCaseResult.Success(Unit))

        // Act
        viewModel.onEvent(LoginUiEvent.SignInUsernameChanged("testUser"))
        viewModel.onEvent(LoginUiEvent.SignInPasswordChanged("testPass"))
        viewModel.onEvent(LoginUiEvent.Login)

        // Assert
        val result = viewModel.authResults.first()
        assert(result is AuthResult.Authorized)
    }

    @Test
    fun `login failure updates authResults with Unauthorized`(): Unit = runTest(testDispatcher) {
        // Arrange
        `when`(loginUseCase.invoke("testUser", "testPass")).thenReturn(UseCaseResult.Error("Login failed"))

        // Act
        viewModel.onEvent(LoginUiEvent.SignInUsernameChanged("testUser"))
        viewModel.onEvent(LoginUiEvent.SignInPasswordChanged("testPass"))
        viewModel.onEvent(LoginUiEvent.Login)

        // Assert
        val result = viewModel.authResults.first()
        assert(result is AuthResult.Unauthorized)
    }

    @Test
    fun `test onEvent singIn`() = runTest(testDispatcher) {
        // Arrange
        val event = LoginUiEvent.SignInUsernameChanged("testUser")

        // Act
        viewModel.onEvent(event)

        // Assert
        assert(viewModel.uiState.value.signInUsername == "testUser")
    }

    @Test
    fun `test onEvent singUp`() = runTest(testDispatcher) {
        // Arrange
        val event = LoginUiEvent.SignUpUsernameChanged("testUser")

        // Act
        viewModel.onEvent(event)

        // Assert
        assert(viewModel.uiState.value.signUpUsername == "testUser")
    }

    @Test
    fun `test onEvent singInPasswordChanged`() = runTest(testDispatcher) {
        // Arrange
        val event = LoginUiEvent.SignInPasswordChanged("testPass")

        // Act
        viewModel.onEvent(event)

        // Assert
        assert(viewModel.uiState.value.signInPassword == "testPass")
    }

    @Test
    fun `test onEvent singUpPasswordChanged`() = runTest(testDispatcher) {
        // Arrange
        val event = LoginUiEvent.SignUpPasswordChanged("testPass")

        // Act
        viewModel.onEvent(event)

        // Assert
        assert(viewModel.uiState.value.signUpPassword == "testPass")
    }
}