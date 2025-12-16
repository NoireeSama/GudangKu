package com.example.gudangku

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barang")
data class TableBarang(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val kodeBarang: String,
    val namaBarang: String,
    val jenisBarang: String,
    val beratBarang: Float,
    val deskripsiBarang: String,
    val satuanBarang: String
)
