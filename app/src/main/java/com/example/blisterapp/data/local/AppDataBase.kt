package com.example.blisterapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.blisterapp.data.local.dao.CycleDao
import com.example.blisterapp.data.local.dao.UserDao
import com.example.blisterapp.data.local.entities.CycleEntity
import com.example.blisterapp.data.local.entities.PillTakenEntity
import com.example.blisterapp.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class, CycleEntity::class, PillTakenEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cycleDao(): CycleDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "blister_app.db"
                ).build()
                INSTANCE = instance
                instance
            }
    }
}