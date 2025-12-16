package com.example.gudangku

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gudang")
data class TableGudang (
    @PrimaryKey(autoGenerate = true)
    val idGudang: Int = 0,
    val kodeGudang: String,
    val namaGudang: String,
    val lokasiGudang: String,
    val jumlahRak: Int
)
