package com.example.gudangku

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BarangKeluarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_barang)

        findViewById<TextView>(R.id.tv_judul_halaman).text = "Barang Keluar"
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val rv = findViewById<RecyclerView>(R.id.rv_riwayat)
        val dataKeluar = listOf(
            ItemRiwayat("Indomie Goreng", "I001", 5, "Dus", "13:00"),
            ItemRiwayat("Kopi Kapal Api", "K002", 2, "Renteng", "14:45"),
            ItemRiwayat("Susu UHT", "S005", 10, "Pcs", "16:20")
        )

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = RiwayatAdapter(this, dataKeluar)
    }
}