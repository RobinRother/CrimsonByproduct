package com.example.crimsonbyproduct

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    // on below line we are creating
    // variables for text view and calendar view
    private lateinit var noteCountTextView: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var dayEditButton: Button

    //val inputNote = intent.getStringExtra("note")
    private var dayContentStorage= DayStorage(this)
    private var keyDate: String = ""

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
        noteCountTextView = findViewById(R.id.noteCountTextView)
        calendarView = findViewById(R.id.calendarView)
        dayEditButton = findViewById(R.id.editDayButton)


        val days: List<DBProvider.Day> = dayDao.getAll()
        dayContentStorage.populateFromDatabase(days)

        val entryCount = dayContentStorage.getEntryCount().toString()
        noteCountTextView.setText("Currently, there are $entryCount days with notes.")


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
                OnDateChangeListener { _, year, month, dayOfMonth ->
                    keyDate = ((year).toString() + "_" +
                            (month +1).toString() + "_" +
                            dayOfMonth.toString())
                })

        dayEditButton.setOnClickListener {
            // openNoteInputActivity()
            openNoteInputActivity()
        }
    }

    private fun openNoteInputActivity() {
        val editNoteActivityIntent = Intent(this, NoteInputActivity::class.java)
        var savedNote = ""
        try{
            savedNote = dayContentStorage.readEntry(keyDate).note
        } catch (e: Exception) {
            savedNote = ""
        }
        finally {
            editNoteActivityIntent.putExtra("date", keyDate)
            editNoteActivityIntent.putExtra("savedNote", savedNote)
            startActivity(editNoteActivityIntent)
        }
    }

    private fun updateDatabase(keyDate: String, createNewEntry: Boolean) {
        val databaseDay = dayContentStorage.getDatabaseDay(keyDate)
        if(createNewEntry) {
            dayDao.insertAll(databaseDay)
        }
        else {
            dayDao.updateEntry(databaseDay)
        }
    }
}