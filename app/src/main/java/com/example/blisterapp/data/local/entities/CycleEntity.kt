package com.example.blisterapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cycles")
data class CycleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val startDateIso: String, // ISO string for start date (e.g. "2025-10-23")
    val pillsCount: Int = 28,
    val activePills: Int = 21,
    val placeboPills: Int = 7
)