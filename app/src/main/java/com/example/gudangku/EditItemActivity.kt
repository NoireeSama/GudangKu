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
    private lateinit var etJenisItem: EditText

    private lateinit var etBeratItem: EditText
    private lateinit var etDeskripsiBarang: EditText

    private lateinit var layoutDynamicInputs: LinearLayout
    private lateinit var switchManual: Switch

    private var idBarang = -1
    private var idGudang = -1
    private lateinit var namaGudang: String
    private lateinit var namaUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        etKodeItem = findViewById(R.id.et_kode_item)
        etNamaItem = findViewById(R.id.et_nama_item)
        etJenisItem = findViewById(R.id.et_jenis_item)
        etBeratItem = findViewById(R.id.et_berat_item)
        etDeskripsiBarang = findViewById(R.id.et_deskripsi_barang)

        switchManual = findViewById(R.id.switch_manual_code)
        layoutDynamicInputs = findViewById(R.id.layout_dynamic_inputs)

        idBarang = intent.getIntExtra("ID_BARANG", -1)

        val session = SessionManager(this)
        idGudang = session.getGudangAktifId()
        namaGudang = session.getGudangNama() ?: "-"
        namaUser = session.getDisplayName()

        if (idBarang == -1 || idGudang == -1) {
            finish()
            return
        }

        val db = GudangKuDatabase.getInstance(this)

        lifecycleScope.launch {
            val barang = db.barangDao().getBarangById(idBarang)
            etKodeItem.setText(barang?.kodeBarang)
            etNamaItem.setText(barang?.namaBarang)
            etJenisItem.setText(barang?.jenisBarang)
            etBeratItem.setText(barang?.beratBarang.toString())
            etDeskripsiBarang.setText(barang?.deskripsiBarang)

        }

        switchManual.setOnCheckedChangeListener { _, isChecked ->
            etKodeItem.isEnabled = isChecked
            etKodeItem.alpha = if (isChecked) 1f else 0.5f
        }

        findViewById<TextView>(R.id.btn_add_description).setOnClickListener {
            addNewDescriptionField()
        }

        findViewById<Button>(R.id.btn_simpan).setOnClickListener {

            val kode = etKodeItem.text.toString().trim()
            val nama = etNamaItem.text.toString().trim()
            val jenis = etJenisItem.text.toString().trim()
            val berat = etBeratItem.text.toString().toFloatOrNull() ?: 0f

            val deskripsiBarang = etDeskripsiBarang.text.toString().trim()

            val catatan = (0 until layoutDynamicInputs.childCount)
                .mapNotNull { index ->
                    (layoutDynamicInputs.getChildAt(index) as? EditText)
                        ?.text?.toString()?.takeIf { it.isNotBlank() }
                }
                .joinToString("\n")


            lifecycleScope.launch {
                val barangLama = db.barangDao().getBarangById(idBarang)
                if (barangLama != null) {
                    val barangBaru = barangLama.copy(
                        kodeBarang = kode,
                        namaBarang = nama,
                        jenisBarang = jenis,
                        beratBarang = berat,
                        deskripsiBarang = deskripsiBarang
                    )

                    db.barangDao().update(barangBaru)

                    db.riwayatDao().insert(
                        TableRiwayat(
                            idBarang = idBarang,
                            idGudang = idGudang,
                            jenis = "EDIT",
                            jumlah = 0,
                            namaGudang = namaGudang,
                            namaUser = namaUser,
                            catatan = catatan.ifBlank { "Edit data barang" },
                            tanggal = System.currentTimeMillis(),
                            namaBarang = barangBaru.namaBarang
                        )
                    )

                    runOnUiThread {
                        Toast.makeText(this@EditItemActivity, "Data diperbarui", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@EditItemActivity, "Data barang tidak ditemukan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun addNewDescriptionField() {
        val et = EditText(this)
        et.hint = "Deskripsi tambahan..."
        et.setPadding(24, 24, 24, 24)
        et.background = ContextCompat.getDrawable(this, R.drawable.bg_input_field)
        layoutDynamicInputs.addView(et)
    }

}
