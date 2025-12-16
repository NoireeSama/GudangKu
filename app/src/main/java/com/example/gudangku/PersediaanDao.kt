package com.example.gudangku

import androidx.room.*

@Dao
interface PersediaanDao {

    @Insert
    suspend fun insert(persediaan: TablePersediaan)

    @Update
    suspend fun update(persediaan: TablePersediaan)

    @Query("SELECT * FROM persediaan")
    suspend fun getAll(): List<TablePersediaan>
}

