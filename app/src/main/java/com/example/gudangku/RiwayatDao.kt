package com.example.gudangku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RiwayatDao {

    @Insert
    suspend fun insert(riwayat: TableRiwayat)

    @Query("""
    SELECT 
        r.id,
        r.jenis,
        r.jumlah,
        r.tanggal,
        r.namaBarang,
        r.namaGudang,
        r.namaUser,
        r.catatan
    FROM riwayat r
    WHERE r.idGudang = :idGudang
    ORDER BY r.tanggal DESC
""")
    fun getByGudang(idGudang: Int): Flow<List<RiwayatBarang>>

    @Query("""
    SELECT 
        id,
        jenis,
        jumlah,
        tanggal,
        namaBarang,
        namaGudang,
        namaUser,
        catatan
    FROM riwayat
    ORDER BY tanggal DESC
""")
    fun getAllRiwayat(): Flow<List<RiwayatBarang>>

    @Query("SELECT * FROM riwayat ORDER BY tanggal DESC")
    suspend fun getAll(): List<TableRiwayat>
}