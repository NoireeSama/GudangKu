package com.example.gudangku

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotifikasiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val rv = findViewById<RecyclerView>(R.id.rv_notifikasi)
        rv.layoutManager = LinearLayoutManager(this)

        val listData = listOf(
            LogNotifikasi("Barang Masuk", "Beras Bulog +10 Karung", "10:30", "MASUK"),
            LogNotifikasi("Barang Keluar", "Tanggo Kaleng -2 Box", "09:15", "KELUAR"),
            LogNotifikasi("Edit Item", "Update Stok Minyak Goreng", "Kemarin", "EDIT"),
            LogNotifikasi("Barang Masuk", "Indomie Goreng +50 Dus", "Kemarin", "MASUK"),
            LogNotifikasi("Barang Keluar", "Beras Bulog -1 Karung", "12 Des", "KELUAR")
        )

        rv.adapter = LogAdapter(listData)
    }
}