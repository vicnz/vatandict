package com.vatan.vatanvict

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class MainActivity : AppCompatActivity() {

    lateinit var db : DBHelper //database reference
    lateinit var searchView : SearchView //search View
    lateinit var listView : ListView //list View

    override fun onStart() {
        super.onStart()
        initializeList()
    }

    //initialize word list from database
    private fun initializeList(){
        db = DBHelper(this)

        val wordList = db.getAllRecord()

        //attach arrayadapter
        val wordArrayAdapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, wordList)
        listView.adapter = wordArrayAdapter

        //search query operation
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                wordArrayAdapter.filter.filter(newText)
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        //word selected event -> render Word_Preview
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val itemValue = listView.getItemAtPosition(position)

            //initialize Activity
            val intent = Intent(this, Word_Preview::class.java)
            intent.putExtra("word", "${itemValue.toString()}")
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize views reference
        listView = findViewById<ListView>(R.id.listView)
        searchView = findViewById<SearchView>(R.id.searchView)

        //default toolbar title
        title = "Ivatan Dictionary"
    }
}