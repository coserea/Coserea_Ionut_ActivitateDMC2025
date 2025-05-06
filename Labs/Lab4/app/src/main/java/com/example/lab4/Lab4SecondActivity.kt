package com.example.lab4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Lab4SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab4_second)

        val editTextTitlu = findViewById<EditText>(R.id.editTextTitlu)
        val editTextAutor = findViewById<EditText>(R.id.editTextAutor)
        val editTextAnPublicatie = findViewById<EditText>(R.id.editTextAnPublicatie)
        val editTextPagini = findViewById<EditText>(R.id.editTextPagini)
        val checkBoxEbook = findViewById<CheckBox>(R.id.checkBoxEbook)
        val spinnerGenCarte = findViewById<Spinner>(R.id.spinnerGenCarte)
        val buttonTrimite = findViewById<Button>(R.id.buttonTrimite)

        val genuri = GenCarte.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genuri)
        spinnerGenCarte.adapter = adapter

        buttonTrimite.setOnClickListener {
            val titlu = editTextTitlu.text.toString()
            val autor = editTextAutor.text.toString()
            val anPublicatie = editTextAnPublicatie.text.toString().toIntOrNull() ?: 0
            val pagini = editTextPagini.text.toString().toIntOrNull() ?: 0
            val esteEbook = checkBoxEbook.isChecked
            val gen = GenCarte.valueOf(spinnerGenCarte.selectedItem.toString())

            if (titlu.isNotEmpty() && autor.isNotEmpty() && anPublicatie > 0 && pagini > 0) {
                val carte = Carte(titlu, autor, anPublicatie, pagini, esteEbook, gen)
                val resultIntent = Intent().apply {
                    putExtra("carte", carte)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Completeaza toate campurile!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
