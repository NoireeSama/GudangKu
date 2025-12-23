package com.example.gudangku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: TableUser)

    // Cek username atau email saat register
    @Query("""
        SELECT * FROM user 
        WHERE username = :input OR email = :input
        LIMIT 1
    """)
    suspend fun getByUsernameOrEmail(input: String): TableUser?

    // Login pakai username ATAU email
    @Query("""
        SELECT * FROM user
        WHERE (username = :input OR email = :input)
        AND password = :password
        LIMIT 1
    """)
    suspend fun login(input: String, password: String): TableUser?
}

