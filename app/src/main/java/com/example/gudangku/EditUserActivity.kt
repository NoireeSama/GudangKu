package com.example.gudangku

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class EditUserActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBack: ImageView
    private lateinit var ivFoto: ImageView
    private lateinit var etConfirm: EditText
    private lateinit var btnUpload: Button
    private var foto: Uri? = null
    private var userId: Int = 0
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        session = SessionManager(this)

        userDao = GudangKuDatabase.getInstance(this).userDao()
        userId = intent.getIntExtra("USER_ID", 0)


        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirm = findViewById(R.id.et_konfirmasi_password)
        ivFoto = findViewById(R.id.iv_foto)
        btnUpload = findViewById(R.id.btn_upload_gambar)
        btnSimpan = findViewById(R.id.btn_simpan)
        btnBack = findViewById(R.id.btn_back)

        userId = intent.getIntExtra("USER_ID", 0)

        loadUser()

        btnUpload.setOnClickListener { pickImageFromGallery() }
        btnBack.setOnClickListener { finish() }
        btnSimpan.setOnClickListener { updateUser() }
    }

    private fun loadUser() {
        lifecycleScope.launch {
            val user = userDao.getById(userId)
            user?.let {
                etUsername.setText(it.username)
                etEmail.setText(it.email)
                etPassword.setText(it.password)
                etConfirm.setText(it.password)

                if (!it.foto.isNullOrEmpty()) {
                    val file = File(it.foto)
                    if (file.exists()) ivFoto.setImageURI(Uri.fromFile(file))
                }
            }
        }
    }

    private fun updateUser() {
        val password = etPassword.text.toString()
        val confirm = etConfirm.text.toString()
        if (password != confirm) {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
            return
        }

        val fotoPath = foto?.let { saveImageToInternal(it) }

        val user = TableUser(
            id = userId,
            username = etUsername.text.toString(),
            email = etEmail.text.toString(),
            password = password,
            foto = fotoPath ?: ""
        )

        lifecycleScope.launch {
            userDao.update(user)

            val session = SessionManager(this@EditUserActivity)
            session.saveUser(
                id = user.id,
                username = user.username,
                email = user.email,
                displayName = user.username,
                foto = user.foto ?: ""
            )

            Toast.makeText(this@EditUserActivity, "User berhasil diperbarui", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun saveImageToInternal(uri: Uri): String {
        val input = contentResolver.openInputStream(uri)!!
        val file = File(filesDir, "user_${System.currentTimeMillis()}.jpg")
        val output = FileOutputStream(file)
        input.copyTo(output)
        input.close()
        output.close()
        return file.absolutePath
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            foto = data?.data
            ivFoto.setImageURI(foto)
        }
    }
}
