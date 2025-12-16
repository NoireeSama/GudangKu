package com.example.gudangku

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "riwayat")
data class TableRiwayat(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idBarang: Int,
    val idGudang: Int,
    val jenis: String, // MASUK / KELUAR / EDIT
    val jumlah: Int,
    val tanggal: Long = System.currentTimeMillis()
)
