package com.example.crimsonbyproduct

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class NoteInputActivity : AppCompatActivity() {
    var note: String = ""
    var inputNote = ""
    var date = ""
    lateinit var noteInputWidget: EditText
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_input)

        inputNote = intent.getStringExtra("savedNote").toString()
        date = intent.getStringExtra("date").toString()

        noteInputWidget = findViewById(R.id.editNoteMultiLine)
        submitButton = findViewById(R.id.submitButton)

        noteInputWidget.setText(inputNote)
        submitButton.setOnClickListener {
            note = noteInputWidget.text.toString()
            openMainActivity()
        }
    }

    fun openMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        mainActivityIntent.putExtra("sourceActivityId", "NoteInputActivity")
        mainActivityIntent.putExtra("note", note)
        mainActivityIntent.putExtra("keyDate", date)
        startActivity(mainActivityIntent)
        finish()
    }


}