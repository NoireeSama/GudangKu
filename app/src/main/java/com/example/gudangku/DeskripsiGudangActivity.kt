package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DeskripsiGudangActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deskripsi_gudang)

        val tvNama = findViewById<TextView>(R.id.tv_nama_gudang)
        val tvKode = findViewById<TextView>(R.id.tv_kode_gudang)
        val tvJumlah = findViewById<TextView>(R.id.tv_jumlah_rak)
        val tvLokasi = findViewById<TextView>(R.id.tv_lokasi_gudang)

        val idGudang = intent.getIntExtra("ID_GUDANG", -1)

        val db = GudangKuDatabase.getInstance(this)
        val gudangDao = db.gudangDao()

        lifecycleScope.launch {
            val gudang = gudangDao.getDeskripsiById(idGudang)

            tvNama.text = gudang?.namaGudang
            tvKode.text = "Kode: ${gudang?.kodeGudang}"
            tvJumlah.text = "Jumlah Rak: ${gudang?.jumlahRak}"
            tvLokasi.text = "Lokasi: ${gudang?.lokasiGudang}"
        }

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        findViewById<Button>(R.id.btn_edit_gudang).setOnClickListener {
            val intent = Intent(this, EditGudangActivity::class.java)
            intent.putExtra("GUDANG_ID", idGudang)
            startActivity(intent)
        }
    }
}
