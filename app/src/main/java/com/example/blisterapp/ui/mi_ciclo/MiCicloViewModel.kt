package com.example.blisterapp.ui.mi_ciclo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blisterapp.auth.SessionManager
import com.example.blisterapp.model.PillState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class PillUiState(
    val index: Int,
    val date: LocalDate,
    val state: PillState,
    val isToday: Boolean
)

data class CycleEntity(
    val userId: String,
    val startDate: LocalDate
)

interface CycleRepository {
    fun getCycleForUserFlow(userId: String): Flow<CycleEntity?>
    suspend fun setCycleForUser(userId: String, cycle: CycleEntity)
}

interface PillTakenRepository {
    fun getTakenDatesFlow(userId: String): Flow<Set<LocalDate>>
    suspend fun markTaken(userId: String, date: LocalDate)
    suspend fun unmarkTaken(userId: String, date: LocalDate)
}

class MiCicloViewModel(
    private val cycleRepository: CycleRepository,
    private val pillTakenRepository: PillTakenRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<PillUiState>>(emptyList())
    val uiState: StateFlow<List<PillUiState>> = _uiState.asStateFlow()

    init {
        // Observe current user id changes and combine the two repository flows for that user.
        // Filter out empty userIds to avoid querying repositories with an invalid id ("").
        sessionManager.currentUserId
            .filter { it.isNotBlank() }
            .flatMapLatest { userId: String ->
                combine(
                    cycleRepository.getCycleForUserFlow(userId),
                    pillTakenRepository.getTakenDatesFlow(userId)
                ) { cycle: CycleEntity?, taken: Set<LocalDate> ->
                    Pair(cycle, taken)
                }
            }
            .onEach { pair ->
                val (cycleEntity, takenDates) = pair
                val pillList = buildPillUiStateList(cycleEntity, takenDates)
                _uiState.value = pillList
            }
            .launchIn(viewModelScope)
    }

    private fun buildPillUiStateList(cycleEntity: CycleEntity?, takenDates: Set<LocalDate>): List<PillUiState> {
        val today = LocalDate.now()
        val startDate = cycleEntity?.startDate ?: today

        val list = mutableListOf<PillUiState>()
        for (i in 0 until 28) {
            val date = startDate.plusDays(i.toLong())
            val isToday = date.isEqual(today)
            val state: PillState = when {
                i >= 21 -> PillState.Placebo(date)
                takenDates.contains(date) -> PillState.Taken(date)
                date.isBefore(today) -> PillState.Missed(date)
                else -> PillState.Upcoming(date)
            }
            list.add(PillUiState(index = i, date = date, state = state, isToday = isToday))
        }
        return list
    }

    fun toggleTaken(index: Int) {
        val currentList = _uiState.value
        if (index < 0 || index >= currentList.size) return
        val item = currentList[index]
        val userId = sessionManager.currentUserId.value
        if (userId.isBlank()) return // guard: no user id -> no action
        viewModelScope.launch {
            when (item.state) {
                is PillState.Taken -> pillTakenRepository.unmarkTaken(userId, item.date)
                else -> pillTakenRepository.markTaken(userId, item.date)
            }
        }
    }

    fun setStartDate(newStartDate: LocalDate) {
        val userId = sessionManager.currentUserId.value
        if (userId.isBlank()) return // guard: require valid user id
        viewModelScope.launch {
            cycleRepository.setCycleForUser(userId, CycleEntity(userId = userId, startDate = newStartDate))
        }
    }

    suspend fun getTodayIndex(): Int? {
        val userId = sessionManager.currentUserId.value
        if (userId.isBlank()) return null
        val cycle = cycleRepository.getCycleForUserFlow(userId).firstOrNull()
        val start = cycle?.startDate ?: return null
        val today = LocalDate.now()
        val days = ChronoUnit.DAYS.between(start, today).toInt()
        return if (days >= 0) days % 28 else null
    }
}