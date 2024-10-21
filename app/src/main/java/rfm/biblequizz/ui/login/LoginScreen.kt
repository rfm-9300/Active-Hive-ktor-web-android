package rfm.biblequizz.ui.login


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.compose.AppTheme
import rfm.biblequizz.R
import rfm.biblequizz.ui.MainNavHost
import rfm.biblequizz.ui.components.AppSnackbarHost
import rfm.biblequizz.ui.components.HeaderText
import rfm.biblequizz.ui.components.LoginTextField
import rfm.biblequizz.ui.home.MainNavHost

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
    navHostController: NavHostController,
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {

    val context = LocalContext.current

    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (checked, onCheckedChanged) = rememberSaveable { mutableStateOf(false) }



    LaunchedEffect(uiState.isAuthorized) {
        if (uiState.isAuthorized) {

        }
    }

    if (uiState.isLoading) {
        FullScreenLoading()
    } else {
        Scaffold(
            snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            LoginContent(
                modifier = Modifier.padding(paddingValues),
                username = username,
                setUsername = setUsername,
                password = password,
                setPassword = setPassword,
                checked = checked,
                onCheckedChanged = onCheckedChanged,
                onEvent = onEvent
            )
        }

    }

    // Process the error messages
    if (uiState.errorMessages.isNotEmpty()) {
        // function to dismiss the error
        val onErrorDismiss = { id: Long -> onEvent(LoginUiEvent.ErrorDismissed(id)) }

        // If onErrorDismiss change while the LaunchedEffect is running,
        // don't restart the effect and use the latest lambda values.
        val onErrorDismissState by rememberUpdatedState(newValue = onErrorDismiss)

        // Remember the errorMessage to show
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }

        // Get the text from the string resource
        val errorMessageText = stringResource(id = errorMessage.messageId)
        val retryMessageText = stringResource(id = R.string.retry)

        // Effect to show the dialog
        LaunchedEffect(key1 = errorMessage, key2 = errorMessageText, key3 = snackbarHostState) {
            // toast(errorMessageText)
            Toast.makeText(context, errorMessageText, Toast.LENGTH_SHORT).show()

            snackbarHostState.showSnackbar(
                message = errorMessageText,
                actionLabel = retryMessageText
            )

            // Remove the error message from the state
            onErrorDismissState(errorMessage.id)
        }

    }

}



@Composable
fun LoginContent(
    modifier: Modifier,
    username: String,
    setUsername: (String) -> Unit,
    password: String,
    setPassword: (String) -> Unit,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    onEvent: (LoginUiEvent) -> Unit
) {
    // if debug mode is enabled, fill the fields with some default values
    setUsername("aaa")
    setPassword("aaaaaaaa")


    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(defaultPadding)
            .clip(RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,

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
                onClick = { onEvent(LoginUiEvent.LoginButtonClicked(username, password)) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )) {
                Text(
                    text = stringResource(id = R.string.login_screen_login_button),
                    modifier = Modifier.padding(5.dp),
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                )
            }
            Spacer(modifier = Modifier.height(itemSpacing))
            AlternativeLogin(
                onIconClick = { /*TODO*/ },
                onSignUpClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.BottomCenter)
            )
        }
    }
}

/**
 * Full screen circular progress indicator
 */
@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
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
    AppTheme  {
        LoginScreen (
            navHostController = NavHostController(LocalContext.current),
            uiState = LoginUiState(),
            onEvent = {}
        )
    }
}