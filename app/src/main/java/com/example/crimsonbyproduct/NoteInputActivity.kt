package com.example.crimsonbyproduct

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class NoteInputActivity : AppCompatActivity() {
    var note: String = ""
    var inputNote = ""
    var date = ""
    lateinit var noteInputWidget: EditText
    lateinit var submitButton: Button
    lateinit var dateTextView: TextView

    // Create the text input and the submit button that opens the main activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_input)

        inputNote = intent.getStringExtra("savedNote").toString()
        date = intent.getStringExtra("date").toString()

        noteInputWidget = findViewById(R.id.editNoteMultiLine)
        submitButton = findViewById(R.id.submitButton)
        dateTextView = findViewById(R.id.dateTextView)

        dateTextView.setText("Date: $date")

        noteInputWidget.setText(inputNote)
        submitButton.setOnClickListener {
            note = noteInputWidget.text.toString()
            openMainActivity()
        }
    }

    // opens the main activity and passes the entered note to it
    fun openMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.putExtra("sourceActivityId", "NoteInputActivity")
        mainActivityIntent.putExtra("note", note)
        mainActivityIntent.putExtra("keyDate", date)
        startActivity(mainActivityIntent)
        finish()
    }


}