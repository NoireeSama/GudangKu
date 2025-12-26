package com.example.gudangku

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class TambahGudangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_gudang)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val etKodeGudang: EditText = findViewById(R.id.et_kode_gudang)
        val etNamaGudang: EditText = findViewById(R.id.et_nama_gudang)
        val etLokasiGudang: EditText = findViewById(R.id.et_lokasi_gudang)
        val etJumlahRak: EditText = findViewById(R.id.et_jumlah_rak)
        val btn_simpan: Button = findViewById(R.id.btn_simpan)

        btn_simpan.setOnClickListener {
            val kodeGudang = etKodeGudang.text.toString()
            val namaGudang = etNamaGudang.text.toString()
            val lokasiGudang = etLokasiGudang.text.toString()
            val jumlahRak = etJumlahRak.text.toString()

            if (kodeGudang.isNotEmpty() && namaGudang.isNotEmpty() && lokasiGudang.isNotEmpty() && jumlahRak.isNotEmpty()) {
                finish()
            } else {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = GudangKuDatabase.getInstance(this@TambahGudangActivity)
                db.gudangDao().insert(
                    TableGudang(
                        kodeGudang = kodeGudang,
                        namaGudang = namaGudang,
                        lokasiGudang = lokasiGudang,
                        jumlahRak = jumlahRak.toInt()
                    )
                )

                runOnUiThread {
                    Toast.makeText(this@TambahGudangActivity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}