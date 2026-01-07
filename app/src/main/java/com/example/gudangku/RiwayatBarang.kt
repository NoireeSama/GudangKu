package com.example.gudangku

data class RiwayatBarang(
    val id: Int,
    val jenis: String,
    val jumlah: Int,
    val tanggal: Long,
    val namaBarang: String,
    val namaGudang: String,
    val namaUser: String,
    val catatan: String
)
