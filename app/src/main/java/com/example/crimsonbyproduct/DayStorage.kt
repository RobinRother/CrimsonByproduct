package com.example.crimsonbyproduct

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DayStorage (var context: AppCompatActivity) {
    private var dayMap = mutableListOf<Day>()

    fun getEntryCount (): Int {
        return dayMap.size
    }

    // returns true if new entry is created
    fun addEntry(keyDate: String, note: String): Boolean {
        var newId = -1
        var newEntry = false
        var entryExists = false

        if (dayMap.isEmpty()) {
            newId = 1
            val newDay = Day(newId, keyDate, note)
            dayMap.add(newDay)
            newEntry = true
            entryExists = true
        }
        else {
            dayMap.forEach {
                if(it.keyDate == keyDate) {
                    newId = it.id
                    it.note = note
                    newEntry = false
                    entryExists = true
                }
            }
        }

        if (!entryExists) {
            newId =dayMap.last().id + 1
            val newDay = Day(newId, keyDate, note)
            dayMap.add(newDay)
            newEntry = true
        }

        return newEntry
    }

    fun readEntry(dateString: String): Day {
        var returnValue = Day(-1)
        if(dayMap.isEmpty()) {
            throw Exception("day container is empty")
        }
        else {
            dayMap.forEach {
                if(it.keyDate == dateString) {
                    returnValue = it
                }
            }
            if (returnValue.id < 1){
                throw Exception("entry not found: " + dateString)
            }
            else {
                return returnValue
            }
        }
    }

    fun removeEntry(dateString: String){
        if(dayMap.isEmpty()) {
            throw Exception("day container is empty")
        }
        for (i in 0 until dayMap.size) {
            if(dayMap[i].keyDate == dateString) {
                dayMap.removeAt(i)
            }
        }
    }

    fun getDatabaseDay(keyDate: String): DBProvider.Day {
        if(dayMap.isEmpty()) {
            throw Exception("day container is empty")
        }
        val day = readEntry(keyDate)
        return DBProvider.Day(day.id, day.keyDate, day.note)
    }

    fun populateFromDatabase(dayQueryResults: List<DBProvider.Day>) {
        for(i in 0 until dayQueryResults.size) {
            val day = Day(dayQueryResults[i].did, dayQueryResults[i].keyDate!!, dayQueryResults[i].note!!)
            dayMap.add(day)
        }
    }
}