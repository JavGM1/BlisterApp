package com.example.blisterapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

private val Context.dataStore by preferencesDataStore(name = "preferences-usuario")

class PreferencesDataStore(private val context: Context) {
    companion object {
        val NOTIF_HOUR = stringPreferencesKey("notificacion_hora")
        val BIOMETRY_ENABLED = booleanPreferencesKey("biometria_habilitada")
        val PREMIUM = booleanPreferencesKey("premium")
    }

    suspend fun setNotifHour(value: String) {
        context.dataStore.edit { prefs ->
            prefs[NOTIF_HOUR] = value
        }
    }

    fun getNotifHour(): Flow<String?> {
        return context.dataStore.data.map { prefs -> prefs[NOTIF_HOUR] }
    }

    suspend fun setBiometryEnabled(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[BIOMETRY_ENABLED] = value }
    }

    fun isBiometryEnabled(): Flow<Boolean?> {
        return context.dataStore.data.map { prefs -> prefs[BIOMETRY_ENABLED] }
    }
}