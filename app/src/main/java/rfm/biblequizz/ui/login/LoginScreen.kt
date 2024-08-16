package rfm.biblequizz.ui.login

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rfm.biblequizz.R
import rfm.biblequizz.ui.components.HeaderText
import rfm.biblequizz.ui.components.LoginTextField
import rfm.biblequizz.ui.theme.BiblequizzTheme

val defaultPadding = 30.dp
val itemSpacing = 8.dp
val spacingTop = 40.dp


@Composable
fun LoginScreen() {
    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (checked, onCheckedChanged) = rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(spacingTop))
        HeaderText(text = "Login",
            modifier = Modifier
                .padding(top = 30.dp)
                .align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            value = username,
            onValueChange = setUsername,
            labelText = "Username",
            leadingIcon = Icons.Default.Person,
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        LoginTextField(
            value = password,
            onValueChange = setPassword,
            labelText = "Password",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password
        )
        Spacer(modifier = Modifier.height(itemSpacing))
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Row (horizontalArrangement = Arrangement.Start) {
                Checkbox(checked = checked, onCheckedChange = onCheckedChanged)
                Text(text = "Remember me", modifier = Modifier.align(Alignment.CenterVertically))
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "Forgot password?")
            }
        }
        Spacer(modifier = Modifier.height(itemSpacing))

        Button(
            onClick = { }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.primary
            )) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(itemSpacing))
        AlternativeLogin(
            onIconClick = { index ->
                when(index) {
                    0 -> { /*TODO*/ }
                    1 -> { /*TODO*/ }
                    2 -> { /*TODO*/ }
                }
            },
            onSignUpClick = { /*TODO*/ },
            modifier = Modifier.fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        )
    }
}

@Composable
fun AlternativeLogin(
    onIconClick: (index: Int) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icons = listOf(R.drawable.ic_google, R.drawable.ic_github, R.drawable.ic_instagram)
    Column (
        modifier = modifier.fillMaxWidth()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Or login with")
        Row (
            modifier = Modifier.padding(top = itemSpacing),
        ){
            icons.forEachIndexed { index, icon ->
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "alternative login",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable { onIconClick(index) }
                )
                Spacer(modifier = Modifier.width(itemSpacing))
            }
        }
        Row (
            modifier = Modifier.padding(top = itemSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an account?")
            TextButton(onClick = onSignUpClick) {
                Text(text = "Sign up")
            }
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    BiblequizzTheme () {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen()
        }

    }
}