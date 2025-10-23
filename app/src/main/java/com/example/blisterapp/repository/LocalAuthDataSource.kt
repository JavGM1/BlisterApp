package com.example.blisterapp.repository

interface LocalAuthDataSource {
    suspend fun register(username: String, email: String, password: CharArray): Pair<String, String>
    suspend fun login(username: String, password: CharArray): Pair<String, String>?
}