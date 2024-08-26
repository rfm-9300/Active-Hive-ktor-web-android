package rfm.biblequizz.ui.login


import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.domain.model.UseCaseResult
import rfm.biblequizz.domain.usecase.LoginUseCase
import javax.inject.Inject

data class LoginUiState (
    val isLoading: Boolean = false,
    val usernameField: String = "",
    val passwordField: String = "",
    val isUsernameValid : Boolean = false,
    val isPasswordValid : Boolean = false,
    val isRememberMeChecked : Boolean = false,
    val isAuthorized: Boolean = false
)

private data class LoginViewModelState(
    val usernameField : String = "",
    val passwordField : String = "",
    val isUsernameValid : Boolean = false,
    val isPasswordValid : Boolean = false,
    val isRememberMeChecked : Boolean = false,
    val isLoading : Boolean = false,
    val isAuthorized: Boolean = false
){
    /**
     * Converts this [LoginViewModelState] to a [LoginUiState] for use in the UI layer.
     */
    fun toUiState(): LoginUiState {
        return LoginUiState(
            usernameField = usernameField,
            passwordField = passwordField,
            isUsernameValid = isUsernameValid,
            isPasswordValid = isPasswordValid,
            isRememberMeChecked = isRememberMeChecked,
            isLoading = isLoading,
            isAuthorized = isAuthorized
        )
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val viewModelState = MutableStateFlow(LoginViewModelState(isLoading = true))

    val uiState = viewModelState
        .map(LoginViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when(event) {
            is LoginUiEvent.LoginButtonClicked -> {
                viewModelState.value = viewModelState.value.copy(isAuthorized = true)
            }

            is LoginUiEvent.UsernameTextFieldChanged -> {
                viewModelState.value = viewModelState.value.copy(usernameField = event.value)
            }
            is LoginUiEvent.PasswordTextFieldChanged -> {
                viewModelState.value = viewModelState.value.copy(passwordField = event.value)
            }

            is LoginUiEvent.RememberMeCheckboxChanged -> TODO()
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            viewModelState.value = viewModelState.value.copy(isLoading = false)
            val loginResult = loginUseCase(username, password)
            when(loginResult) {
              is UseCaseResult.Success -> {
                  resultChannel.send(AuthResult.Authorized())
              }
                is UseCaseResult.Error -> {
                    resultChannel.send(AuthResult.Unauthorized())
                }
            }
        }
    }

    fun validateFields(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}