package com.example.gudangku

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class EditGudangActivity : AppCompatActivity() {

    private lateinit var etKodeGudang: EditText
    private lateinit var etNamaGudang: EditText
    private lateinit var etLokasiGudang: EditText
    private lateinit var etJumlahRak: EditText
    private lateinit var layoutDynamicInputs: LinearLayout
    private val dynamicEditTexts = mutableListOf<EditText>()
    private var idGudang = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_gudang)

        etKodeGudang = findViewById(R.id.et_edit_kode)
        etNamaGudang = findViewById(R.id.et_edit_nama_gudang)
        etLokasiGudang = findViewById(R.id.et_edit_lokasi)
        etJumlahRak = findViewById(R.id.et_edit_jumlah_rak)
        layoutDynamicInputs = findViewById(R.id.layout_dynamic_inputs)

        val btnAddDesc = findViewById<TextView>(R.id.btn_add_description_field)
        val btnSimpan = findViewById<TextView>(R.id.btn_simpan_perubahan)

        idGudang = intent.getIntExtra("GUDANG_ID", -1)
        if (idGudang == -1) {
            Toast.makeText(this, "Gudang tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = GudangKuDatabase.getInstance(this)

        lifecycleScope.launch {
            val gudang = db.gudangDao().getById(idGudang)
            gudang?.let {
                etKodeGudang.setText(it.kodeGudang)
                etNamaGudang.setText(it.namaGudang)
                etLokasiGudang.setText(it.lokasiGudang)
                etJumlahRak.setText(it.jumlahRak.toString())
            }
        }

        btnAddDesc.setOnClickListener { addNewDescriptionField() }

        btnSimpan.setOnClickListener { saveData() }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun addNewDescriptionField(text: String = "") {
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
        newEditText.setText(text)

        layoutDynamicInputs.addView(newEditText)
        dynamicEditTexts.add(newEditText)
    }

    private fun saveData() {
        val kode = etKodeGudang.text.toString().trim()
        val nama = etNamaGudang.text.toString().trim()
        val lokasi = etLokasiGudang.text.toString().trim()
        val jumlahRak = etJumlahRak.text.toString().toIntOrNull() ?: 0

        val db = GudangKuDatabase.getInstance(this)
        lifecycleScope.launch {
            val gudangLama = db.gudangDao().getById(idGudang)
            if (gudangLama != null) {
                val gudangBaru = gudangLama.copy(
                    kodeGudang = kode,
                    namaGudang = nama,
                    lokasiGudang = lokasi,
                    jumlahRak = jumlahRak
                )
                db.gudangDao().update(gudangBaru)

                runOnUiThread {
                    Toast.makeText(this@EditGudangActivity, "Data gudang diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@EditGudangActivity, "Gudang tidak ditemukan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
