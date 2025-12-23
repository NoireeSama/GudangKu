package com.example.gudangku

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotifikasiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)

        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }

        val rv = findViewById<RecyclerView>(R.id.rv_notifikasi)
        rv.layoutManager = LinearLayoutManager(this)

        val session = SessionManager(this)
        val gudangId = session.getGudangAktifId()

        val db = GudangKuDatabase.getInstance(this)

        lifecycleScope.launch {
            db.riwayatDao()
                .getAllRiwayat()
                .collectLatest { data ->
                    rv.adapter = LogAdapter(data)
                }
        }
    }
}
