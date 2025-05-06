package com.example.lab4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val requestCodeSecondActivity = 1
    private lateinit var textViewCarte: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonAdaugaCarte = findViewById<Button>(R.id.buttonAdaugaCarte)
        textViewCarte = findViewById(R.id.textViewCarte)

        buttonAdaugaCarte.setOnClickListener {
            val intent = Intent(this, Lab4SecondActivity::class.java)
            startActivityForResult(intent, requestCodeSecondActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeSecondActivity && resultCode == Activity.RESULT_OK) {
            val carte: Carte? = data?.getParcelableExtra("carte")

            carte?.let {
                val carteInfo = """
                    Titlu: ${it.titlu}
                    Autor: ${it.autor}
                    An Publicatie: ${it.anPublicatie}
                    Numar Pagini: ${it.pagini}
                    Este eBook: ${if (it.esteEbook) "Da" else "Nu"}
                    Gen: ${it.gen.name}
                """.trimIndent()

                textViewCarte.text = carteInfo
            }
        }
    }
}
