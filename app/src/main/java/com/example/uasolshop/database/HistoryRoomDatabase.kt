package com.example.uasolshop.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.uasolshop.dataclass.Products

@Database(entities = [History::class], version = 35, exportSchema = false)
abstract class HistoryRoomDatabase : RoomDatabase() {
    abstract fun historiDao(): HistoryDao?
    companion object {
        @Volatile
        private var INSTANCE: HistoryRoomDatabase? = null
        fun getDatabase(context: Context): HistoryRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(HistoryRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        HistoryRoomDatabase::class.java, "produkGB"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}