package com.example.blisterapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.blisterapp.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUsers(): Int

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getByUsername(username: String): UserEntity?

    @Insert
    suspend fun insert(user: UserEntity)
}