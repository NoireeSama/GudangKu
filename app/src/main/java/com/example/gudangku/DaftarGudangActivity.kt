package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class DaftarGudangActivity : AppCompatActivity() {

    private lateinit var db: GudangKuDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_gudang)
        val session = SessionManager(this)

        db = GudangKuDatabase.getInstance(this)

        val rv = findViewById<RecyclerView>(R.id.rv_daftar_gudang)

        // 🔹 Adapter SATU KALI
        val adapter = GudangAdapter(this)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        findViewById<FloatingActionButton>(R.id.fab_add_gudang).setOnClickListener {
            startActivity(Intent(this, TambahGudangActivity::class.java))
        }

        // 🔹 AMBIL DATA DARI ROOM
        lifecycleScope.launch {
            db.gudangDao().getAllGudang().collect { list ->
                adapter.updateData(list)
            }
        }
    }
}
