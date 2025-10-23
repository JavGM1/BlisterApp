package com.example.blisterapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.blisterapp.data.local.entities.CycleEntity
import com.example.blisterapp.data.local.entities.PillTakenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CycleDao {
    @Insert
    suspend fun insertCycle(cycle: CycleEntity): Long

    @Insert
    suspend fun insertPillTaken(pillTaken: PillTakenEntity): Long

    @Query("SELECT * FROM cycles WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    fun getLastCycleForUser(userId: Long): Flow<CycleEntity?>

    @Query("SELECT * FROM pill_taken WHERE cycleId = :cycleId")
    fun getTakenForCycle(cycleId: Long): Flow<List<PillTakenEntity>>
}