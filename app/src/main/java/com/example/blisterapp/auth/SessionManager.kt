package com.example.blisterapp.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SessionManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefsName = "mi_blister_prefs_secure"

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        prefsName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _isLoggedIn = MutableStateFlow(prefs.getBoolean(KEY_IS_LOGGED_IN, false))
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Expose current user id as a StateFlow so ViewModels can observe user changes.
    private val _currentUserId = MutableStateFlow(prefs.getString(KEY_USER_ID, "") ?: "")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    var token: String?
        get() = prefs.getString(KEY_TOKEN, null)
        private set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    var biometricEnabled: Boolean
        get() = prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false)
        private set(value) = prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, value).apply()

    /**
     * Login and optionally set the current user id.
     * If userId is provided it will be persisted and emitted via [currentUserId].
     */
    fun login(tokenValue: String, enableBiometric: Boolean = false, userId: String? = null) {
        token = tokenValue
        biometricEnabled = enableBiometric
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
        _isLoggedIn.value = true

        userId?.let {
            prefs.edit().putString(KEY_USER_ID, it).apply()
            _currentUserId.value = it
        }
    }

    fun logout() {
        prefs.edit().clear().apply()
        _isLoggedIn.value = false
        // clear user id
        _currentUserId.value = ""
    }

    // Renamed to avoid JVM signature clash with the property setter.
    fun enableBiometric(enabled: Boolean) {
        biometricEnabled = enabled
    }

    /**
     * Explicit setter to change current user id (persists and emits).
     */
    fun setCurrentUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
        _currentUserId.value = userId
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_USER_ID = "user_id"
    }
}