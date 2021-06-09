package com.vatan.vatanvict

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class Word_Preview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word__preview)

        val intent : Intent = getIntent()
        val word = intent.getStringExtra("word")

        val db = DBHelper(this)
        val arrayList = db.getSingleDefinition("$word")


        populateTextViews(arrayList.get(0), arrayList.get(2), arrayList.get(1), arrayList.get(3))
        title = word.toString() //initialize activity title


        val exitButton = findViewById<ImageButton>(R.id.closeActivity)

        exitButton.setOnClickListener {
            onBackPressed()
        }
    }

    //Populate View
    private fun populateTextViews(word: String, type : String, definition : String, example: String?) {
        val wordTitle = findViewById<TextView>(R.id.wordTitle)
        val wordType = findViewById<TextView>(R.id.word_type)
        val wordDef = findViewById<TextView>(R.id.definitionText)
        val exampleSent = findViewById<TextView>(R.id.exampleTextView)

        wordTitle.text = word.capitalize()
        wordType.text = type
        wordDef.text = definition.capitalize()
        exampleSent.text = example?.capitalize()
    }
}