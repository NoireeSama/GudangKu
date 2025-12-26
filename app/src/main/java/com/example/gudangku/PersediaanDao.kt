package com.example.gudangku

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PersediaanDao {

    @Insert
    suspend fun insert(persediaan: TablePersediaan)

    @Update
    suspend fun update(persediaan: TablePersediaan)

    @Query("SELECT * FROM persediaan")
    suspend fun getAll(): List<TablePersediaan>

    // ================= LIST UNTUK HOME & PERSEDIAAN =================
    @Query("""
        SELECT 
            p.id AS idPersediaan,
            p.idBarang AS idBarang,
            b.kodeBarang AS kodeBarang,
            b.namaBarang AS namaBarang,
            b.jenisBarang AS jenisBarang,
            b.beratBarang AS beratBarang,
            p.stok AS stok,
            b.satuanBarang AS satuan,
            b.deskripsiBarang AS deskripsiBarang
        FROM persediaan p
        JOIN barang b ON p.idBarang = b.id
        WHERE p.idGudang = :idGudang
    """)
    fun getPersediaanByGudang(idGudang: Int): Flow<List<PersediaanDetail>>

    // ================= DETAIL ITEM =================
    @Query("""
        SELECT 
            p.id AS idPersediaan,
            p.idBarang AS idBarang,
            b.kodeBarang AS kodeBarang,
            b.namaBarang AS namaBarang,
            b.jenisBarang AS jenisBarang,
            b.beratBarang AS beratBarang,
            p.stok AS stok,
            b.satuanBarang AS satuan,
            b.deskripsiBarang AS deskripsiBarang
        FROM persediaan p
        JOIN barang b ON p.idBarang = b.id
        WHERE p.id = :idPersediaan
        LIMIT 1
    """)
    suspend fun getDetailItem(idPersediaan: Int): PersediaanDetail

    // ================= UPDATE STOK =================
    @Query("""
        UPDATE persediaan 
        SET stok = :stok 
        WHERE id = :id
    """)
    suspend fun updateStok(id: Int, stok: Int)

    // ================= HOME SUMMARY =================
    @Query("""
        SELECT
            COUNT(p.id) AS totalBarang,
            SUM(p.stok * b.beratBarang) AS totalBerat,
            SUM(CASE WHEN p.stok > 0 THEN p.stok ELSE 0 END) AS barangMasuk,
            SUM(CASE WHEN p.stok = 0 THEN 1 ELSE 0 END) AS barangKeluar
        FROM persediaan p
        JOIN barang b ON p.idBarang = b.id
        WHERE p.idGudang = :idGudang
    """)
    suspend fun getHomeSummary(idGudang: Int): HomeSummary

    @Query("""
        SELECT 
            p.id AS idPersediaan,
            b.id AS idBarang,
            g.idGudang AS idGudang,
            g.namaGudang AS namaGudang,
            b.kodeBarang AS kodeBarang,
            b.namaBarang AS namaBarang,
            b.jenisBarang AS jenisBarang,
            b.beratBarang AS beratBarang,
            p.stok AS stok,
            b.satuanBarang AS satuan,
            b.deskripsiBarang AS deskripsiBarang
        FROM persediaan p
        JOIN barang b ON p.idBarang = b.id
        JOIN gudang g ON p.idGudang = g.idGudang
        WHERE p.id = :idPersediaan
        LIMIT 1
    """)
    suspend fun getDetailByPersediaan(idPersediaan: Int): PersediaanDetail

    @Query("""
    SELECT 
        p.id AS idPersediaan, 
        p.idBarang, 
        p.idGudang, 
        p.stok, 
        b.kodeBarang, 
        b.namaBarang, 
        b.jenisBarang, 
        b.beratBarang, 
        b.deskripsiBarang, 
        b.satuanBarang AS satuan
    FROM persediaan p
    INNER JOIN barang b ON p.idBarang = b.id 
    WHERE p.idBarang = :idBarang 
    LIMIT 1
""")
    suspend fun getDetailByBarang(idBarang: Int): PersediaanDetail


    @Query("DELETE FROM persediaan WHERE idBarang = :idBarang AND idGudang = :idGudang")
    suspend fun deleteByBarangDanGudang(idBarang: Int, idGudang: Int)

}
