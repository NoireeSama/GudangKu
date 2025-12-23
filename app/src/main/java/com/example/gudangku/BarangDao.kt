package com.example.gudangku

import androidx.room.*

@Dao
interface BarangDao {

    @Insert
    suspend fun insert(barang: TableBarang): Long

    @Update
    suspend fun update(barang: TableBarang)

    @Delete
    suspend fun delete(barang: TableBarang)

    @Query("SELECT * FROM barang")
    suspend fun getAll(): List<TableBarang>

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

    @Query("SELECT * FROM barang WHERE id = :id LIMIT 1")
    suspend fun getBarangById(id: Int): TableBarang
}
