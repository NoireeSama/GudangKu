package com.example.gudangku

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class EditItemActivity : AppCompatActivity() {

    private lateinit var layoutDynamicInputs: LinearLayout
    private lateinit var etKodeItem: EditText
    private lateinit var switchManual: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        etKodeItem = findViewById(R.id.et_kode_item)
        switchManual = findViewById(R.id.switch_manual_code)
        layoutDynamicInputs = findViewById(R.id.layout_dynamic_inputs)

        switchManual.setOnCheckedChangeListener { _, isChecked ->
            etKodeItem.isEnabled = isChecked

            if (isChecked) {
                etKodeItem.alpha = 1.0f
            } else {
                etKodeItem.alpha = 0.5f
            }
        }

        findViewById<TextView>(R.id.btn_add_description).setOnClickListener {
            addNewDescriptionField()
        }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        findViewById<Button>(R.id.btn_simpan).setOnClickListener {
            Toast.makeText(this, "Data Item Disimpan!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun addNewDescriptionField() {
        val newEditText = EditText(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 150
        )
        params.setMargins(0, 0, 0, 30)
        newEditText.layoutParams = params
        newEditText.hint = "Deskripsi Tambahan..."
        newEditText.setPadding(30, 30, 30, 30)
        newEditText.background = ContextCompat.getDrawable(this, R.drawable.bg_input_field)
        newEditText.textSize = 14f

        layoutDynamicInputs.addView(newEditText)
    }
}