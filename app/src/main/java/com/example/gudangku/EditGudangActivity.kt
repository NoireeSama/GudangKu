package com.example.gudangku

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class EditGudangActivity : AppCompatActivity() {

    lateinit var layoutDynamicInputs: LinearLayout
    val dynamicEditTexts = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gudang)

        layoutDynamicInputs = findViewById(R.id.layout_dynamic_inputs)
        val btnAddDesc = findViewById<TextView>(R.id.btn_add_description_field)
        val btnSimpan = findViewById<TextView>(R.id.btn_simpan_perubahan)

        btnAddDesc.setOnClickListener {
            addNewDescriptionField()
        }

        btnSimpan.setOnClickListener {
            saveData()
        }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun addNewDescriptionField() {
        val newEditText = EditText(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            150
        )
        params.setMargins(0, 0, 0, 30)
        newEditText.layoutParams = params

        newEditText.hint = "Deskripsi Tambahan..."
        newEditText.setPadding(30, 30, 30, 30)
        newEditText.background = ContextCompat.getDrawable(this, R.drawable.bg_input_field)
        newEditText.textSize = 14f

        layoutDynamicInputs.addView(newEditText)
        dynamicEditTexts.add(newEditText)
    }

    private fun saveData() {
        val allDescriptions = StringBuilder()

        for (editText in dynamicEditTexts) {
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                allDescriptions.append(text).append("\n")
            }
        }
        Toast.makeText(this, "Data Disimpan:\n$allDescriptions", Toast.LENGTH_LONG).show()
    }
}