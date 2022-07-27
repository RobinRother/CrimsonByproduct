package com.example.crimsonbyproduct

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // on below line we are creating
    // variables for text view and calendar view
    lateinit var dateTV: TextView
    lateinit var calendarView: CalendarView
    lateinit var dayEditButton: Button

    var dayContentStorage= DayStorage();
    var keyDate: String = "Select a date!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initializing variables of
        // list view with their ids.
        dateTV = findViewById(R.id.idTVDate)
        calendarView = findViewById(R.id.calendarView)
        dayEditButton = findViewById(R.id.editDayButton)


        // on below line we are adding set on
        // date change listener for calendar view.
        calendarView
            .setOnDateChangeListener(
                OnDateChangeListener { view, year, month, dayOfMonth ->
                    // In this Listener we are getting values
                    // such as year, month and day of month
                    // on below line we are creating a variable
                    // in which we are adding all the cariables in it.
                    keyDate = (year.toString() + "_" +
                            (month + 1).toString() + "_" +
                            dayOfMonth.toString())

                    // set this date in TextView for Display
                    dateTV.setText(keyDate)
                })

        dayEditButton.setOnClickListener {
            dayContentStorage.addEntry(keyDate, Day("fun" + keyDate, true))
            val toast = Toast.makeText(this, dayContentStorage.readEntry(keyDate).note, Toast.LENGTH_SHORT)
            toast.show()
        }


    }
}