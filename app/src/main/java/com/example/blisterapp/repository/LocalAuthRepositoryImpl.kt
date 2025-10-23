package com.example.blisterapp.repository

import android.content.Context
import com.example.blisterapp.auth.PasswordUtils
import com.example.blisterapp.data.local.AppDataBase
import com.example.blisterapp.data.local.entities.UserEntity
import java.util.UUID

class LocalAuthRepositoryImpl(private val context: Context) : LocalAuthDataSource {
    private val userDao by lazy { AppDataBase.getInstance(context).userDao() }

    override suspend fun register(username: String, email: String, password: CharArray): Pair<String, String> {
        val salt = PasswordUtils.generateSalt()
        val hash = PasswordUtils.hashPassword(password, salt)
        val user = UserEntity(
            username = username,
            email = email,
            passwordHash = PasswordUtils.toBase64(hash),
            salt = PasswordUtils.toBase64(salt)
        )
        userDao.insert(user)
        val token = UUID.randomUUID().toString()
        return Pair(username, token)
    }

    override suspend fun login(username: String, password: CharArray): Pair<String, String>? {
        val user = userDao.getByUsername(username) ?: return null
        val ok = PasswordUtils.verify(password, user.salt, user.passwordHash)
        if (!ok) return null
        val token = UUID.randomUUID().toString()
        return Pair(user.username, token)
    }

    suspend fun hasUsers(): Boolean {
        return userDao.countUsers() > 0
    }
}