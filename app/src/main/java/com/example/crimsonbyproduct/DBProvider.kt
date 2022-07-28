package com.example.crimsonbyproduct

import androidx.room.*

class DBProvider {
    @Entity
    data class Day(
        @PrimaryKey val did: Int,
        @ColumnInfo(name = "keyData") val keyDate: String?,
        @ColumnInfo(name = "note") val note: String?
    )

    @Dao
    interface DayDao {
        @Query("SELECT * FROM day")
        fun getAll(): List<Day>

        @Insert
        fun insertAll(vararg days: Day)

        @Update
        fun updateEntry(day: Day)


        @Delete
        fun delete(day: Day)
    }

    @Database(entities = [Day::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun dayDao(): DayDao
    }
}