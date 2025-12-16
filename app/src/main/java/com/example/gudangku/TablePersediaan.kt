package com.example.gudangku

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persediaan")
data class TablePersediaan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val idBarang: Int,
    val idGudang: Int,
    val stok: Int
)
