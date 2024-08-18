package rfm.biblequizz.ui.login


import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import rfm.biblequizz.domain.usecase.LoginUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when(event) {
            is LoginUiEvent.Login-> {
                _uiState.value = _uiState.value.copy(isLoading = true)
                login(
                    username = _uiState.value.signInUsername,
                    password = _uiState.value.signInPassword
                )
            }

            is LoginUiEvent.SignUpUsernameChanged -> {
                _uiState.value = _uiState.value.copy(signUpUsername = event.value)
            }
            is LoginUiEvent.SignUpPasswordChanged -> {
                _uiState.value = _uiState.value.copy(signUpPassword = event.value)
            }
            is LoginUiEvent.SignInPasswordChanged -> {
                _uiState.value = _uiState.value.copy(signInPassword = event.value)
            }
            is LoginUiEvent.SignInUsernameChanged -> {
                _uiState.value = _uiState.value.copy(signInUsername = event.value)
            }
        }
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginUseCase(username, password)
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}