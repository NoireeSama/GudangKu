package com.example.gudangku

data class PersediaanDetail(
    val idPersediaan: Int,
    val idBarang: Int,
    val kodeBarang: String,
    val namaBarang: String,
    val jenisBarang: String,
    val beratBarang: Double,
    var stok: Int,
    val satuan: String,
    val deskripsiBarang: String
)


