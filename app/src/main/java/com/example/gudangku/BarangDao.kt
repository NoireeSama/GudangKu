package com.example.gudangku

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BarangDao {

    @Insert
    suspend fun insert(barang: TableBarang): Long

    @Update
    suspend fun update(barang: TableBarang)

    @Delete
    suspend fun delete(barang: TableBarang)

    @Query("SELECT * FROM barang")
    fun getAll(): Flow<List<TableBarang>>

    @Query("""
    UPDATE barang SET
        kodeBarang = :kode,
        namaBarang = :nama,
        beratBarang = :berat
    WHERE id = :idBarang
    """)
    suspend fun updateBarang(
        idBarang: Int,
        kode: String,
        nama: String,
        berat: Float
    )

    @Query("DELETE FROM barang WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM barang WHERE id = :id")
    suspend fun getBarangById(id: Int): TableBarang
}