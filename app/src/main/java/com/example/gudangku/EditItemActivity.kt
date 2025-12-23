package com.example.gudangku

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EditItemActivity : AppCompatActivity() {

    private lateinit var etKodeItem: EditText
    private lateinit var etNamaItem: EditText
    private lateinit var etBeratItem: EditText
    private lateinit var layoutDynamicInputs: LinearLayout
    private lateinit var switchManual: Switch

    private var idBarang: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        etKodeItem = findViewById(R.id.et_kode_item)
        etNamaItem = findViewById(R.id.et_nama_item)
        etBeratItem = findViewById(R.id.et_berat_item)
        switchManual = findViewById(R.id.switch_manual_code)
        layoutDynamicInputs = findViewById(R.id.layout_dynamic_inputs)

        idBarang = intent.getIntExtra("ID_BARANG", -1)
        if (idBarang == -1) {
            finish()
            return
        }

        val db = GudangKuDatabase.getInstance(this)

        // ==========================
        // LOAD DATA BARANG
        // ==========================
        lifecycleScope.launch {
            val barang = db.barangDao().getBarangById(idBarang)

            etKodeItem.setText(barang.kodeBarang)
            etNamaItem.setText(barang.namaBarang)
            etBeratItem.setText(barang.beratBarang.toString())
        }

        // ==========================
        // SWITCH KODE MANUAL
        // ==========================
        switchManual.setOnCheckedChangeListener { _, isChecked ->
            etKodeItem.isEnabled = isChecked
            etKodeItem.alpha = if (isChecked) 1f else 0.5f
        }

        // ==========================
        // TAMBAH DESKRIPSI DINAMIS
        // ==========================
        findViewById<TextView>(R.id.btn_add_description).setOnClickListener {
            addNewDescriptionField()
        }

        // ==========================
        // SIMPAN PERUBAHAN
        // ==========================
        findViewById<Button>(R.id.btn_simpan).setOnClickListener {
            val kode = etKodeItem.text.toString().trim()
            val nama = etNamaItem.text.toString().trim()
            val berat = etBeratItem.text.toString().toFloatOrNull() ?: 0f

            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama item tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                db.barangDao().updateBarang(
                    idBarang = idBarang,
                    kode = kode,
                    nama = nama,
                    berat = berat
                )

                runOnUiThread {
                    Toast.makeText(this@EditItemActivity, "Data item berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
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
        newEditText.background =
            ContextCompat.getDrawable(this, R.drawable.bg_input_field)
        newEditText.textSize = 14f

        layoutDynamicInputs.addView(newEditText)
    }
}
