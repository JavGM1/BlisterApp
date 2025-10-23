package com.example.blisterapp.ui.navigation

import android.content.Context
import com.example.blisterapp.auth.SessionManager
import com.example.blisterapp.ui.mi_ciclo.CycleEntity
import com.example.blisterapp.ui.mi_ciclo.CycleRepository
import com.example.blisterapp.ui.mi_ciclo.PillTakenRepository
import com.example.blisterapp.network.ScraperService
import com.example.blisterapp.repository.ScraperCotizarRepository
import com.example.blisterapp.repository.CotizarRepository
import kotlinx.coroutines.flow.*
import java.time.LocalDate

/**
 * ServiceLocator actualizado: llamar init(context) antes de usar sessionManager.
 */
object ServiceLocator {
    var sessionManager: SessionManager? = null
        private set

    fun init(context: Context) {
        if (sessionManager == null) {
            sessionManager = SessionManager(context.applicationContext)
        }
    }

    val cycleRepository: CycleRepository by lazy { InMemoryCycleRepository() }
    val pillTakenRepository: PillTakenRepository by lazy { InMemoryPillTakenRepository() }
    val cotizarRepository: CotizarRepository by lazy { ScraperCotizarRepository(ScraperService()) }

    private class InMemoryCycleRepository : CycleRepository {
        private val backing = MutableStateFlow<Map<String, CycleEntity>>(emptyMap())
        override fun getCycleForUserFlow(userId: String): Flow<CycleEntity?> = backing.map { it[userId] }
        override suspend fun setCycleForUser(userId: String, cycle: CycleEntity) {
            backing.value = backing.value + (userId to cycle)
        }
    }

    private class InMemoryPillTakenRepository : PillTakenRepository {
        private val backing = MutableStateFlow<Map<String, Set<LocalDate>>>(emptyMap())
        override fun getTakenDatesFlow(userId: String): Flow<Set<LocalDate>> = backing.map { it[userId] ?: emptySet() }
        override suspend fun markTaken(userId: String, date: LocalDate) {
            val current = backing.value[userId] ?: emptySet()
            backing.value = backing.value + (userId to (current + date))
        }
        override suspend fun unmarkTaken(userId: String, date: LocalDate) {
            val current = backing.value[userId] ?: emptySet()
            backing.value = backing.value + (userId to (current - date))
        }
    }
}