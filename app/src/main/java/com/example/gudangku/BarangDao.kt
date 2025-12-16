package com.example.gudangku

import androidx.room.*

@Dao
interface BarangDao {

    @Insert
    suspend fun insert(barang: TableBarang)

    @Update
    suspend fun update(barang: TableBarang)

    @Delete
    suspend fun delete(barang: TableBarang)

    @Query("SELECT * FROM barang")
    suspend fun getAll(): List<TableBarang>
}
