package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        session = SessionManager(this)

        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        tvUserName.text = session.getUsername()

        val tvJumlahGudang: TextView = findViewById(R.id.tv_jumlah_gudang)
        val tvJumlahRak: TextView = findViewById(R.id.tv_jumlah_rak)

        val db = GudangKuDatabase.getInstance(this)
        val gudangDao = db.gudangDao()

        lifecycleScope.launch {
            val jumlahRak = gudangDao.getJumlahRak()
            tvJumlahRak.text = jumlahRak.toString()
            val jumlahGudang = gudangDao.getJumlahGudang()
            tvJumlahGudang.text = jumlahGudang.toString()
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            session.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.cv_jumlah_gudang).setOnClickListener {
            val intent = Intent(this, DaftarGudangActivity::class.java)
            startActivity(intent)

        }

        findViewById<Button>(R.id.btnHelp).setOnClickListener {
        }

        findViewById<Button>(R.id.btnPolicy).setOnClickListener {
        }

        findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
        }
    }
}