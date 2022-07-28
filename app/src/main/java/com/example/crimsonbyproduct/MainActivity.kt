package com.example.crimsonbyproduct

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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

    lateinit var dayDao: DBProvider.DayDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val database = Room.databaseBuilder(
            applicationContext,
            DBProvider.AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        dayDao = database.dayDao()


        // initializing variables of
        // list view with their ids.
        dateTV = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)
        dayEditButton = findViewById(R.id.editDayButton)


        val days: List<DBProvider.Day> = dayDao.getAll()
        dayContentStorage.populateFromDatabase(days)


        var isFromNoteInput = false
        if(intent != null ) {
            isFromNoteInput = intent.getStringExtra("sourceActivityId") == "NoteInputActivity"
        }

        if (isFromNoteInput) {
            keyDate = intent.getStringExtra("keyDate").toString()
            val note = intent.getStringExtra("note").toString()

            val newEntryCreated =dayContentStorage.addEntry(keyDate, note)
            updateDatabase(keyDate, newEntryCreated)

        }

        val date = calendarView.date + (Calendar.MONTH + 1)
        val readableDateFormat = SimpleDateFormat("yyyy_MM_dd")
        keyDate = readableDateFormat.format(date)


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
            // openNoteInputActivity()
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
        finally {
            editNoteActivityIntent.putExtra("date", keyDate)
            editNoteActivityIntent.putExtra("savedNote", savedNote)
            startActivity(editNoteActivityIntent)
        }
    }

    fun updateDatabase(keyDate: String, createNewEntry: Boolean) {
        val databaseDay = dayContentStorage.getDatabaseDay(keyDate)
        if(createNewEntry) {
            dayDao.insertAll(databaseDay)
        }
        else {
            dayDao.updateEntry(databaseDay)
        }
    }
}