package rfm.biblequizz.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rfm.biblequizz.repositories.FakeLoginRepository


class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginRepository: FakeLoginRepository

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        loginRepository = FakeLoginRepository()
        viewModel = LoginViewModel(loginRepository)

    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login success updates UI state correctly`() = runTest {
        // Given
        loginRepository.setReturnError(false)

        // When
        viewModel.onEvent(LoginUiEvent.LoginButtonClicked("testUser", "testPass"))

        // Complete all coroutines actions
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.isAuthorized)
        assertTrue(viewModel.uiState.value.errorMessages.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login unauthorized updates UI state with error`() = runTest {
        // Given
        loginRepository.setReturnError(true)

        // When
        viewModel.onEvent(LoginUiEvent.LoginButtonClicked("testUser", "testPass"))

        advanceUntilIdle()

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isAuthorized)
        assertTrue(viewModel.uiState.value.errorMessages.isNotEmpty())
    }
}
