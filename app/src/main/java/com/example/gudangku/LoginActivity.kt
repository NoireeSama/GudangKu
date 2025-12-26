package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegisterNav = findViewById<Button>(R.id.btnRegisterNav)

        val session = SessionManager(this)
        if (session.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener {

            val input = etLogin.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (input.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = GudangKuDatabase.getInstance(this@LoginActivity)
                val user = db.userDao().login(input, password)

                runOnUiThread {
                    if (user != null) {

                        session.createLoginSession(
                            user.id,
                            user.username,
                            user.email
                        )

                        Toast.makeText(
                            this@LoginActivity,
                            "Login berhasil",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(this@LoginActivity, MainActivity::class.java)
                        )
                        finish()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Username/Email atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        btnRegisterNav.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}