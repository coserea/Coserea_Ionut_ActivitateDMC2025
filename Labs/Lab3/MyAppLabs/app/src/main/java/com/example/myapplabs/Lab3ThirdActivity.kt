package com.example.myapplabs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplabs.ui.theme.MyAppLabsTheme

class Lab3ThirdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Preluam datele primite din Lab3SecondActivity
        val message = intent.getStringExtra("MESSAGE") ?: "No message"
        val number1 = intent.getIntExtra("NUMBER1", 0)
        val number2 = intent.getIntExtra("NUMBER2", 0)
        val sum = number1 + number2
        val newMessage = "Sum calculation completed!"

        // Afișam datele într-un Toast
        Toast.makeText(this, "$message - Numbers: $number1 & $number2", Toast.LENGTH_LONG).show()

        setContent {
            MyAppLabsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ThirdActivityScreen(
                        message = message,
                        num1 = number1,
                        num2 = number2,
                        sum = sum,
                        onSendBack = {
                            val resultIntent = Intent().apply {
                                putExtra("RETURN_MESSAGE", newMessage)
                                putExtra("SUM_RESULT", sum)
                            }
                            setResult(RESULT_OK, resultIntent)
                            finish() // inchide activitatea si trimite datele inapoi
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ThirdActivityScreen(message: String, num1: Int, num2: Int, sum: Int, onSendBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = message)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Number 1: $num1")
        Text(text = "Number 2: $num2")
        Text(text = "Sum: $sum")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSendBack) {
            Text("Send Sum Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThirdActivityPreview() {
    MyAppLabsTheme {
        ThirdActivityScreen("Hello from SecondActivity!", 10, 20, 30, onSendBack = {})
    }
}
