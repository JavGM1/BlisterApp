package com.example.blisterapp.repository

import android.content.Context
import com.example.blisterapp.data.local.AppDatabase
import com.example.blisterapp.data.local.entities.UserEntity

class UserRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val dao = db.userDao()

    suspend fun registerUser(nombre: String, correo: String, claveHash: String): Long {
        val user = UserEntity(nombre = nombre, correo = correo, claveHash = claveHash)
        return dao.insert(user)
    }

    suspend fun findByEmail(email: String): UserEntity? {
        return dao.findByEmail(email)
    }
}