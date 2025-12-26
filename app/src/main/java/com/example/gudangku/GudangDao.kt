package com.example.gudangku

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GudangDao {

    @Insert
    suspend fun insert(gudang: TableGudang)

    @Update
    suspend fun update(gudang: TableGudang)

    @Delete
    suspend fun delete(gudang: TableGudang)

    @Query("SELECT * FROM gudang")
    suspend fun getAll(): List<TableGudang>

    @Query("SELECT * FROM gudang WHERE idGudang = :id")
    suspend fun getById(id: Int): TableGudang?

    @Query("SELECT * FROM gudang ORDER BY idGudang DESC")
    fun getAllGudang(): Flow<List<TableGudang>>
}