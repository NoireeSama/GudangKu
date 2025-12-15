package com.example.gudangku

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BarangMasukActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_barang)

        findViewById<TextView>(R.id.tv_judul_halaman).text = "Barang Masuk"
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val rv = findViewById<RecyclerView>(R.id.rv_riwayat)
        val dataMasuk = listOf(
            ItemRiwayat("Beras Bulog", "B001", 50, "Karung", "08:00"),
            ItemRiwayat("Minyak Goreng", "M002", 20, "Dus", "09:30"),
            ItemRiwayat("Gula Pasir", "G003", 10, "Karung", "11:15")
        )

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = RiwayatAdapter(this, dataMasuk)
    }
}