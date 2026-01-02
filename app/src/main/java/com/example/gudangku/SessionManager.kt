package com.example.gudangku

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("WMS_PREFS", Context.MODE_PRIVATE)

    companion object {
        const val KEY_IS_LOGGED_IN = "is_logged_in"
        const val KEY_USER_ID = "user_id"
        const val KEY_USERNAME = "username"
        const val KEY_EMAIL = "email"
        const val KEY_GUDANG_ID = "gudang_id"
        const val KEY_GUDANG_NAMA = "gudang_nama"
        const val KEY_GUDANG_KODE = "gudang_kode"
        const val KEY_GUDANG_JUMLAH_RAK = "gudang_jumlah_rak"
    }

    fun createLoginSession(
        userId: Int,
        username: String,
        email: String
    ) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putInt(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .putString(KEY_EMAIL, email)
            .apply()
    }

    fun isLoggedIn(): Boolean =
        prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUsername(): String? =
        prefs.getString(KEY_USERNAME, null)

    fun getEmail(): String? =
        prefs.getString(KEY_EMAIL, null)

    fun getDisplayName(): String =
        getUsername() ?: getEmail() ?: "User"

    fun logout() {
        prefs.edit().clear().apply()
    }

    fun setActiveGudang(
        gudangId: Int,
        namaGudang: String,
        kodeGudang: String,
        jumlahRak: Int
    ) {
        prefs.edit()
            .putInt(KEY_GUDANG_ID, gudangId)
            .putString(KEY_GUDANG_NAMA, namaGudang)
            .putString(KEY_GUDANG_KODE, kodeGudang)
            .putInt(KEY_GUDANG_JUMLAH_RAK, jumlahRak)
            .apply()
    }

    fun getGudangNama(): String? =
        prefs.getString(KEY_GUDANG_NAMA, null)

    fun getGudangAktifId(): Int {
        return prefs.getInt(KEY_GUDANG_ID, -1)
    }
}