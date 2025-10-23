package com.example.blisterapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pill_taken")
data class PillTakenEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cycleId: Long,
    val dayIndex: Int, // 0..27
    val taken: Boolean,
    val timestampIso: String? = null // optional when marked
)