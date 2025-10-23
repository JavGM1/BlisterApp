package com.example.blisterapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val username: String,
    val email: String,
    val passwordHash: String, // PBKDF2 result encoded base64
    val salt: String // base64 salt
)