package com.example.myapplabs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplabs.ui.theme.MyAppLabsTheme

class Lab3SecondActivity : ComponentActivity() {
    // Callback pentru a primi datele de la Lab3ThirdActivity
    private val thirdActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val returnMessage = data?.getStringExtra("RETURN_MESSAGE") ?: "No message received"
                val sumResult = data?.getIntExtra("SUM_RESULT", 0) ?: 0

                // Afisam rezultatele intr-un Toast
                Toast.makeText(this, "$returnMessage - Sum: $sumResult", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppLabsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SecondActivityScreen(
                        modifier = Modifier.padding(innerPadding),
                        onNavigateToThird = {
                            val message = "Hello from SecondActivity!"
                            val number1 = 10
                            val number2 = 20

                            val intent = Intent(this, Lab3ThirdActivity::class.java).apply {
                                putExtra("MESSAGE", message)
                                putExtra("NUMBER1", number1)
                                putExtra("NUMBER2", number2)
                            }
                            thirdActivityLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SecondActivityScreen(modifier: Modifier = Modifier, onNavigateToThird: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = "Hello Android! 2nd Activity")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToThird) {
            Text("Go to Third Activity")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MyAppLabsTheme {
        SecondActivityScreen(onNavigateToThird = {})
    }
}
