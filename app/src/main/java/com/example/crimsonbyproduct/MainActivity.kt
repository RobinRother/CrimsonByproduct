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
    // predefine widgets
    private lateinit var noteCountTextView: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var dayEditButton: Button

    // initialize data storage
    private var dayContentStorage= DayStorage(this)
    private var keyDate: String = ""

    // preinitialize data access object
    lateinit var dayDao: DBProvider.DayDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // instantiate room database
        val database = Room.databaseBuilder(
            applicationContext,
            DBProvider.AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        dayDao = database.dayDao()


        // initialize widgets
        noteCountTextView = findViewById(R.id.noteCountTextView)
        calendarView = findViewById(R.id.calendarView)
        dayEditButton = findViewById(R.id.editDayButton)

        // query database for all saved days and save to data storage object
        val days: List<DBProvider.Day> = dayDao.getAll()
        dayContentStorage.populateFromDatabase(days)

        // set text on top of calendar
        val entryCount = dayContentStorage.getEntryCount().toString()
        noteCountTextView.setText("Currently, there are $entryCount days with notes.")

        // if this activity is called from the edit activity, get its Information
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

        // get the current date from calendar widget
        val date = calendarView.date + (Calendar.MONTH + 1)
        val readableDateFormat = SimpleDateFormat("yyyy_MM_dd")
        keyDate = readableDateFormat.format(date)


        // click event listener for calendarView, that returns the selected date
        calendarView
            .setOnDateChangeListener(
                OnDateChangeListener { _, year, month, dayOfMonth ->
                    keyDate = ((year).toString() + "_" +
                            (month +1).toString() + "_" +
                            dayOfMonth.toString())
                })

        // Event Listener for the add Entry button (opens Note Entry activity)
        dayEditButton.setOnClickListener {
            // openNoteInputActivity()
            openNoteInputActivity()
        }
    }

    // open the noteEntry Activity and pas the selected date and optionally the saved note
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

    // update the database if changes occured
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