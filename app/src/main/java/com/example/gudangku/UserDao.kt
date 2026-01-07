package com.example.gudangku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert suspend fun insert(user: TableUser)

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): TableUser?

    @androidx.room.Update
    suspend fun update(user: TableUser)

    @Query("SELECT * FROM user WHERE username = :input OR email = :input LIMIT 1")
    suspend fun getByUsernameOrEmail(input: String): TableUser?

    @Query("""
        SELECT * FROM user
        WHERE (username = :input OR email = :input)
        AND password = :password
        LIMIT 1
    """)
    suspend fun login(input: String, password: String): TableUser?
}
