package com.example.crimsonbyproduct

class Day (var note: String = "", var keyDate: String = "", var hasNote: Boolean = false){

    fun addNote(inputNote: String = "") : String{
        this.hasNote = inputNote != ""
        return inputNote
    }
}