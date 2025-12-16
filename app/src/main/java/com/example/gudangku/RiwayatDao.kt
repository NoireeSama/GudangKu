package com.example.gudangku

import androidx.room.*

@Dao
interface RiwayatDao {

    @Insert
    suspend fun insert(riwayat: TableRiwayat)

    @Query("SELECT * FROM riwayat ORDER BY tanggal DESC")
    suspend fun getAll(): List<TableRiwayat>
}
