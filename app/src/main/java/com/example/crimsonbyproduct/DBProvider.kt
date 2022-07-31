package com.example.crimsonbyproduct

import androidx.room.*

// a database Wrapper that uses the room database library for SQlite
class DBProvider {

    // define a data entity
    @Entity
    data class Day(
        @PrimaryKey val did: Int,
        @ColumnInfo(name = "keyData") val keyDate: String?,
        @ColumnInfo(name = "note") val note: String?
    )

    // the data access object (the actual wrapper)
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