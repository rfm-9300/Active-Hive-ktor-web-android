package rfm.biblequizz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import rfm.biblequizz.data.local.models.Question
import rfm.biblequizz.ui.theme.BiblequizzTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(Question::class)
            )
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiblequizzTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BiblequizzTheme {
        Greeting("Android")
    }
}