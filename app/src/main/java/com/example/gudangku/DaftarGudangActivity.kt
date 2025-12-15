package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DaftarGudangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_gudang)

        val rv = findViewById<RecyclerView>(R.id.rv_daftar_gudang)
        val listGudang = listOf(
            Gudang("Gudang 1", "jl.kemana aja no.32\nGX-0001"),
            Gudang("Gudang 2", "jl.maguewohuarjhooh no.10\nGX-0002")
        )
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = GudangAdapter(this, listGudang)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        findViewById<FloatingActionButton>(R.id.fab_add_gudang).setOnClickListener {
            startActivity(Intent(this, TambahGudangActivity::class.java))
        }
    }
}