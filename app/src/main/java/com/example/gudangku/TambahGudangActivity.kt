package com.example.gudangku

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TambahGudangActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_gudang)
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
    }
}