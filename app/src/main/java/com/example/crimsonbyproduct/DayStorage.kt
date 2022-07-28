package com.example.crimsonbyproduct

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DayStorage (var context: AppCompatActivity) {
    private var dayMap = mutableMapOf<String, Day>()

    fun addEntry(dateString: String, inputDay: Day): Boolean {
        return dayMap.put(dateString, inputDay) != null
    }

    fun readEntry(dateString: String): Day {
        if(!dayMap.any()) {
            throw Exception("No entries stored yet")
        }
        else if(!dayMap.contains(dateString)){
            throw Exception("No entry for this date found: " + dateString)
        }
        else {
            return dayMap.getOrElse(dateString){ Day() }
        }
    }

    fun removeEntry(dateString: String): Boolean {
        if(!dayMap.any()) {
            throw Exception("No entries stored yet")
        }
        else if(dayMap.remove(dateString) == null){
            throw Exception("No entry for this date found: " + dateString)
        }
        else {
            return true
        }
    }

    fun saveToDatabase(database: DBProvider, keyDate: String) {
        try {
            database.addDay(dayMap.get(keyDate)!!)
        }
        catch (e: Exception) {
            val toast = Toast.makeText(context, "EXCEPTION: " + e.message, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun populateFromDatabase(database: DBProvider) {
        // below is the variable for cursor
        // we have called method to get
        // all names from our database
        // and add to name text view
        val cursor = database.getDay()

        // moving the cursor to first position and
        // appending value in the text view
        cursor!!.moveToFirst()
        var day = Day()
        day.keyDate = cursor.getString(cursor.getColumnIndex(DBProvider.KEYDATE_COl)) + "\n"
        day.addNote(cursor.getString(cursor.getColumnIndexOrThrow(DBProvider.NOTE_COL)) + "\n")
        dayMap.put(day.keyDate, day)


        // moving our cursor to next
        // position and appending values
        while(cursor.moveToNext()){
            var nextday = Day()
            nextday.keyDate = cursor.getString(cursor.getColumnIndexOrThrow(DBProvider.KEYDATE_COl)) + "\n"
            nextday.addNote(cursor.getString(cursor.getColumnIndexOrThrow(DBProvider.NOTE_COL)) + "\n")
            dayMap.put(nextday.keyDate, day)
        }

        // at last we close our cursor
        cursor.close()
    }
}