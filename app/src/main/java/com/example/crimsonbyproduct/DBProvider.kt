package com.example.crimsonbyproduct

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBProvider(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(database: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                KEYDATE_COl + " TEXT," +
                NOTE_COL + " TEXT" + ")")

        // we are calling sqlite
        // method for executing our query
        database.execSQL(query)
    }

    override fun onUpgrade(database: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(database)
    }

    // This method is for adding data in our database
    fun addDay(day: Day){

        // below we are creating
        // a content values variable
        val values = ContentValues()

        // we are inserting our values
        // in the form of key-value pair
        values.put(KEYDATE_COl, day.keyDate)
        values.put(NOTE_COL, day.note)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val database = this.writableDatabase

        // all values are inserted into database
        database.insert(TABLE_NAME, null, values)

        // at last we are
        // closing our database
        database.close()
    }

    // below method is to get
    // all data from our database
    fun getDay(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val database = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return database.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "DAY_DATABASE"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "day_table"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val KEYDATE_COl = "key_date"

        // below is the variable for age column
        val NOTE_COL = "note"
    }
}
