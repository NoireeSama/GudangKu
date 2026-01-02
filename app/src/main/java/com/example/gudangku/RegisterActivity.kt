package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etRegisterUsername)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val etKonfirmasi = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegisterAction)
        val btnLoginNav = findViewById<Button>(R.id.btnLoginNav)

        btnRegister.setOnClickListener {

            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val konfirmasi = etKonfirmasi.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || konfirmasi.isEmpty()
            ) {
                Toast.makeText(this, "Semua data wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != konfirmasi) {
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = GudangKuDatabase.getInstance(this@RegisterActivity)

                val existingUser =
                    db.userDao().getByUsernameOrEmail(username) ?:
                    db.userDao().getByUsernameOrEmail(email)

                runOnUiThread {
                    if (existingUser != null) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Username atau Email sudah terdaftar",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        lifecycleScope.launch {
                            db.userDao().insert(
                                TableUser(
                                    username = username,
                                    email = email,
                                    password = password
                                )
                            )

                            runOnUiThread {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Pendaftaran berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        }
                    }
                }
            }
        }

        btnLoginNav.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}