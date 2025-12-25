package com.example.gudangku

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeskripsiItemActivity : AppCompatActivity() {

    private lateinit var tvNama: TextView
    private lateinit var tvDetail: TextView
    private lateinit var tvStok: TextView
    private lateinit var tvSatuan: TextView

    private var idPersediaan = -1
    private var idBarang = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deskripsi_item)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnEdit = findViewById<Button>(R.id.btn_edit_persediaan)
        val btnHapus = findViewById<Button>(R.id.btn_hapus)


        tvNama = findViewById(R.id.tv_nama_barang)
        tvDetail = findViewById(R.id.tv_detail_barang)
        tvStok = findViewById(R.id.tv_stok)
        tvSatuan = findViewById(R.id.tv_satuan)

        btnBack.setOnClickListener { finish() }

        // 1. Ambil data dari intent
        idPersediaan = intent.getIntExtra("ID_PERSEDIAAN", -1)
        idBarang = intent.getIntExtra("ID_BARANG", -1)

// 2. Validasi: Minimal harus ada salah satu ID
        if (idPersediaan == -1 && idBarang == -1) {
            Toast.makeText(this, "Data tidak valid", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val db = GudangKuDatabase.getInstance(this)

        lifecycleScope.launch {
            try {
                // 3. Ambil data dari database
                val detail = if (idPersediaan != -1) {
                    db.persediaanDao().getDetailByPersediaan(idPersediaan)
                } else {
                    db.persediaanDao().getDetailByBarang(idBarang)
                }

                // 4. Update nilai ID lokal agar fungsi hapus/edit tidak error
                idBarang = detail.idBarang
                idPersediaan = detail.idPersediaan

                tampilkanDetail(detail)

                btnEdit.setOnClickListener {
                    startActivity(
                        Intent(this@DeskripsiItemActivity, EditItemActivity::class.java)
                            .putExtra("ID_PERSEDIAAN", idPersediaan)
                            .putExtra("ID_BARANG", idBarang)
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DeskripsiItemActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        btnHapus.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Item")
                .setMessage("Item akan dihapus permanen. Lanjutkan?")
                .setPositiveButton("Hapus") { _, _ -> hapusItem() }
                .setNegativeButton("Batal", null)
                .show()
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

    private fun hapusItem() {
        val db = GudangKuDatabase.getInstance(this)

        lifecycleScope.launch {
            try {
                val session = SessionManager(this@DeskripsiItemActivity)
                val idGudang = session.getGudangAktifId()
                val namaGudang = session.getGudangNama() ?: "-"
                val namaUser = session.getDisplayName()

                if (idBarang == -1 || idGudang == -1) {
                    throw Exception("ID tidak valid")
                }

                val barang = db.barangDao().getBarangById(idBarang)
                val namaBarang = barang?.namaBarang ?: "(Barang tidak ditemukan)"

                // 1️⃣ SIMPAN LOG DULU
                db.riwayatDao().insert(
                    TableRiwayat(
                        idBarang = idBarang,
                        idGudang = idGudang,
                        jenis = "HAPUS",
                        jumlah = 0,
                        namaGudang = namaGudang,
                        namaUser = namaUser,
                        catatan = "Menghapus barang $namaBarang",
                        tanggal = System.currentTimeMillis()
                    )
                )

                // 2️⃣ BARU HAPUS DATA
                db.persediaanDao().deleteByBarangDanGudang(idBarang, idGudang)
                db.barangDao().deleteById(idBarang)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DeskripsiItemActivity, "Item dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DeskripsiItemActivity,
                        "Gagal hapus: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }



}

