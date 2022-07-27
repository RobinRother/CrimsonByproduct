package com.example.crimsonbyproduct

class DayStorage {
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

    fun saveToDatabase() {}
    fun populateFromDatabase() {}
}