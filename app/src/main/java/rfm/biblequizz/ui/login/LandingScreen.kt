package rfm.biblequizz.ui.login

import AppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import rfm.biblequizz.R
import rfm.biblequizz.ui.components.AppSnackbarHost

@Composable
fun LandingScreen(
    navHostController: NavHostController,
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    if (uiState.isLoading) {
        FullScreenLoading()
    } else {
        Scaffold(
            snackbarHost = { AppSnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            LandingContent(
                modifier = Modifier.padding(paddingValues),
            )
        }

    }
}

    @Composable
    fun LandingContent(
        modifier: Modifier = Modifier,
    ) {
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent, // 100% transparent at the top
                Color.White.copy(alpha = 1f) // Fully opaque black at the bottom
            ),
            startY = 0f,
            endY = 300f // Adjust this value as needed based on your layout
        )
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            // Image filling 80% of the height
            Image(
                painter = painterResource(id = R.drawable.splashss),
                contentDescription = "Launch Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)

            )

            // Column starting at 40% of the Box height
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .align(Alignment.BottomCenter)
                    .background(brush = gradientBrush),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Column (
                    modifier = Modifier.weight(0.3f),
                ){
                    Text(
                        text = "Bem vindo à Hillsong Portugal",
                        style = MaterialTheme.typography.displayMedium.copy(
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        ))
                    Button(
                        onClick = { /* Handle button click here */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Button background color
                            contentColor = Color.White // Text color
                        ),
                        modifier = Modifier.padding(vertical = 40.dp, horizontal = 30.dp)
                            .fillMaxWidth()// Optional padding above the button
                    ) {
                        Text(text = "Get Started") // Button text
                    }
                }

            }
        }
    }

@Preview
@Composable
fun PreviewLandingScreen() {
    AppTheme {
        LandingScreen(
            navHostController = NavHostController(LocalContext.current),
            uiState = LoginUiState(),
            onEvent = {}
        )
    }

}