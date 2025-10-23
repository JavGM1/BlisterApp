package com.example.blisterapp.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blisterapp.auth.SessionManager
import com.example.blisterapp.ui.mi_ciclo.CycleRepository
import com.example.blisterapp.ui.mi_ciclo.MiCicloViewModel
import com.example.blisterapp.ui.mi_ciclo.PillTakenRepository

class MiCicloViewModelFactory(
    private val cycleRepository: CycleRepository,
    private val pillTakenRepository: PillTakenRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MiCicloViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MiCicloViewModel(cycleRepository, pillTakenRepository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}