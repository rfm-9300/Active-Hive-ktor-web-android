package rfm.biblequizz.ui.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import rfm.biblequizz.R
import rfm.biblequizz.data.model.AuthResult
import rfm.biblequizz.ui.components.HeaderText
import rfm.biblequizz.ui.components.LoginTextField
import rfm.biblequizz.ui.theme.BiblequizzTheme

val defaultPadding = 30.dp
val itemSpacing = 8.dp
val spacingTop = 40.dp

// Defining const val for the keys
const val KEY_GOOGLE = "Google"
const val KEY_FACEBOOK = "Facebook"
const val KEY_INSTAGRAM = "Instagram"

// Defining drawable resources as vals
val ICON_GOOGLE = R.drawable.ic_google
val ICON_FACEBOOK = R.drawable.ic_facebook
val ICON_INSTAGRAM = R.drawable.ic_instagram

// Creating a map with const keys and drawable resource values
val iconsMap = mapOf(
    KEY_GOOGLE to ICON_GOOGLE,
    KEY_FACEBOOK to ICON_FACEBOOK,
    KEY_INSTAGRAM to ICON_INSTAGRAM
)


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToHome : () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (checked, onCheckedChanged) = rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when(result) {
                is AuthResult.Authorized -> {
                    Toast.makeText(context, "Authorized", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(context, "Unknown Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun doLogin(){
        val areFieldsValid = viewModel.validateFields(username = username, password= password)
        if (!areFieldsValid) return

        // event change login and password
        viewModel.onEvent(LoginUiEvent.SignInUsernameChanged(username))
        viewModel.onEvent(LoginUiEvent.SignInPasswordChanged(password))
        viewModel.onEvent(LoginUiEvent.Login)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(spacingTop))
        HeaderText(text = stringResource(id = R.string.login_screen_login),
            modifier = Modifier
                .padding(top = 30.dp)
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            value = username,
            onValueChange = setUsername,
            labelText = stringResource(id = R.string.login_screen_username),
            leadingIcon = Icons.Default.Person,
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            value = password,
            onValueChange = setPassword,
            labelText = stringResource(id = R.string.login_screen_password),
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Row (horizontalArrangement = Arrangement.Start) {
                Checkbox(checked = checked, onCheckedChange = onCheckedChanged)
                Text(
                    text = stringResource(id = R.string.login_screen_remember_me),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.login_screen_forgot_password))
            }
        }
        Spacer(modifier = Modifier.height(itemSpacing))

        Button(
            onClick = { doLogin() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.secondary
            )) {
            Text(text = stringResource(id = R.string.login_screen_login_button))
        }
        Spacer(modifier = Modifier.height(itemSpacing))
        AlternativeLogin(
            onIconClick = { key ->
                Toast.makeText(context, "$key Login", Toast.LENGTH_SHORT).show()
            },
            onSignUpClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        )
    }
}



@Composable
fun AlternativeLogin(
    onIconClick: (key: String) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.login_screen_or_login_with))
        Row (
            modifier = Modifier.padding(top = itemSpacing),
        ){
            iconsMap.forEach { (key, icon) ->
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = key, // You can use the key as the content description
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onIconClick(key) } // Pass the key instead of index
                )
                Spacer(modifier = Modifier.width(itemSpacing))
            }
        }
        Row (
            modifier = Modifier.padding(top = itemSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.login_screen_dont_have_account))
            TextButton(onClick = onSignUpClick) {
                Text(text = stringResource(id = R.string.login_screen_sign_up))
            }
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    BiblequizzTheme  {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen { }
        }

    }
}