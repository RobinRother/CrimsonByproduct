package com.example.crimsonbyproduct

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    // on below line we are creating
    // variables for text view and calendar view
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    lateinit var dayEditButton: Button

    //val inputNote = intent.getStringExtra("note")
    var dayContentStorage= DayStorage(this)
    var keyDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing variables of
        // list view with their ids.
        dateTV = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)
        dayEditButton = findViewById(R.id.editDayButton)

        val database = DBProvider(this, null)
        dayContentStorage.populateFromDatabase(database)

        if(intent != null) {
            keyDate = intent.getStringExtra("keyDate").toString()
            dayContentStorage.addEntry(keyDate, Day(intent.getStringExtra("note").toString(), keyDate, true))
            dayContentStorage.saveToDatabase(database, keyDate)
        }


        // on below line we are adding set on
        // date change listener for calendar view.
        calendarView
            .setOnDateChangeListener(
                OnDateChangeListener { view, year, month, dayOfMonth ->
                    keyDate = ((year).toString() + "_" +
                            (month +1).toString() + "_" +
                            dayOfMonth.toString())

                    // set this date in TextView for Display
                    dateTV.setText(keyDate)
                })

        dayEditButton.setOnClickListener {
            openNoteInputActivity()
        }
    }

    fun openNoteInputActivity() {
        val editNoteActivityIntent = Intent(this, NoteInputActivity::class.java)

        var savedNote: String = ""
        try{
            savedNote = dayContentStorage.readEntry(keyDate).note
        } catch (e: Exception) {
            savedNote = ""
            val toast = Toast.makeText(this, "EXCEPTION: " + e.message, Toast.LENGTH_SHORT)
            toast.show()
        }

        editNoteActivityIntent.putExtra("date", keyDate)
        editNoteActivityIntent.putExtra("savedNote", savedNote)
        finish()
        startActivity(editNoteActivityIntent)
    }
}