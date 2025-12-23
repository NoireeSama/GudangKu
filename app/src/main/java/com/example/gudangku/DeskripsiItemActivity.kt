package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DeskripsiItemActivity : AppCompatActivity() {

    private lateinit var tvNama: TextView
    private lateinit var tvDetail: TextView
    private lateinit var tvStok: TextView
    private lateinit var tvSatuan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deskripsi_item)

        // view
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnEdit = findViewById<Button>(R.id.btn_edit_persediaan)

        tvNama = findViewById(R.id.tv_nama_barang)
        tvDetail = findViewById(R.id.tv_detail_barang)
        tvStok = findViewById(R.id.tv_stok)
        tvSatuan = findViewById(R.id.tv_satuan)

        btnBack.setOnClickListener { finish() }

        // ambil ID persediaan
        val idPersediaan = intent.getIntExtra("ID_PERSEDIAAN", -1)
        if (idPersediaan == -1) {
            finish()
            return
        }

        val db = GudangKuDatabase.getInstance(this)

        lifecycleScope.launch {
            val detail = db.persediaanDao()
                .getDetailByPersediaan(idPersediaan)

            tampilkanDetail(detail)

            btnEdit.setOnClickListener {
                startActivity(
                    Intent(this@DeskripsiItemActivity, EditItemActivity::class.java)
                        .putExtra("ID_PERSEDIAAN", detail.idPersediaan)
                        .putExtra("ID_BARANG", detail.idBarang)
                )
            }
        }
    }

    private fun tampilkanDetail(detail: PersediaanDetail) {
        tvNama.text = detail.namaBarang
        tvStok.text = detail.stok.toString()
        tvSatuan.text = detail.satuan

        tvDetail.text = """
            Kode Barang : ${detail.kodeBarang}
            Jenis       : ${detail.jenisBarang}
            Berat/item  : ${detail.beratBarang}
            Total berat : ${detail.beratBarang * detail.stok}
            
            Deskripsi:
            ${detail.deskripsiBarang.ifBlank { "-" }}
        """.trimIndent()
    }
}
