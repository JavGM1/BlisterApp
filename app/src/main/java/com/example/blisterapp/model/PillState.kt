package com.example.blisterapp.model

import java.time.LocalDate

/**
 * PillState representa el estado de una pastilla en el ciclo.
 */
sealed class PillState {
    data class Taken(val date: LocalDate) : PillState()
    data class Missed(val date: LocalDate) : PillState()
    data class Upcoming(val date: LocalDate) : PillState()
    data class Placebo(val date: LocalDate) : PillState()
}