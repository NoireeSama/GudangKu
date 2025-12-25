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
        r.id AS id,
        r.jenis AS jenis,
        r.jumlah AS jumlah,
        r.tanggal AS tanggal,
        COALESCE(b.namaBarang, '(Barang dihapus)') AS namaBarang,
        g.namaGudang AS namaGudang,
        r.namaUser AS namaUser,
        r.catatan AS catatan
    FROM riwayat r
    LEFT JOIN barang b ON r.idBarang = b.id
    INNER JOIN gudang g ON r.idGudang = g.idGudang
    WHERE r.idGudang = :idGudang
    ORDER BY r.tanggal DESC
""")
    fun getByGudang(idGudang: Int): Flow<List<RiwayatBarang>>



    @Query("""
    SELECT 
        r.id AS id,
        r.jenis AS jenis,
        r.jumlah AS jumlah,
        r.tanggal AS tanggal,
        b.namaBarang AS namaBarang,
        g.namaGudang AS namaGudang,
        r.namaUser AS namaUser,
        r.catatan AS catatan
    FROM riwayat r
    INNER JOIN barang b ON r.idBarang = b.id
    INNER JOIN gudang g ON r.idGudang = g.idGudang
    ORDER BY r.tanggal DESC
""")
    fun getAllRiwayat(): Flow<List<RiwayatBarang>>

    @Query("SELECT * FROM riwayat ORDER BY tanggal DESC")
    suspend fun getAll(): List<TableRiwayat>
}
