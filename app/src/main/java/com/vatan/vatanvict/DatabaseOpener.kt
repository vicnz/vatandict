package com.vatan.vatanvict

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.io.File
import java.io.FileOutputStream

/*
* @DB_NAME = database asset file name
* @DB_VERSION = database schema change version update
* @TABLE_NAME = database table (dictionary)
* */

const val DB_NAME = "store.db"
const val DB_VERSION = 1
const val DB_TABLE_NAME = "dictionary"


class DBHelper(val context : Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    private var db : SQLiteDatabase? = null

    init {
        val dbExist = checkDatabase() //check if database exists
        if(dbExist) {
            //if database already exist then do nothing
        }
        else{
            //else clone database from asset to device
            createDatabase()
        }
    }

    /**
     * CHECK DATABASE EXISTENCE
     * @return Boolean
     * */

    private fun checkDatabase() : Boolean {
        val dbFile = File(context.getDatabasePath(DB_NAME).path)
        return dbFile.exists()
    }

    /**
     * CREATE DATABASE
     */
    private fun createDatabase(){
        copyDatabase()
    }

    //CLONE DATABASE FROM ASSETS
    private fun copyDatabase() {
        val entry = context.assets.open("${DB_NAME}")

        val output = File(context.getDatabasePath(DB_NAME).path)
        val outStream = FileOutputStream(output)

        val byteToCopy = entry.copyTo(outStream)

        entry.close()
        outStream.flush()
        outStream.close()
    }

    //OPEN DATABASE : initialize db object with self reference
    private fun openDatabase(){
        db = SQLiteDatabase.openDatabase(context.getDatabasePath(DB_NAME).path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    //CLOSE DATABASE OBJECT
    override fun close() {
        db?.close()
        super.close()
    }

    //GET A SINGLE RECORD BASED ON WORD
    fun getSingleDefinition(word: String) : Array<String>{

        openDatabase()

        val cursor = db?.rawQuery("SELECT * FROM $DB_TABLE_NAME WHERE WORD = ?", arrayOf(word))
        if(cursor != null){
            cursor?.moveToFirst()
            val row = arrayOf(
                    cursor.getString(1), //word
                    cursor.getString(2), //definition
                    cursor.getString(3), //type
                    cursor.getString(4) //example
            )
            cursor?.close()
            close()

            return row
        }else{
            close()
            return arrayOf()
        }
    }

    //GET ALL WORDS FROM RECORD
    fun getAllRecord() : ArrayList<String> {
        openDatabase()

        val cursor = db?.rawQuery("SELECT * FROM $DB_TABLE_NAME", null)

        if(cursor != null && cursor.count > 0){
            var rows = arrayListOf<String>()
            while(cursor?.moveToNext()) {
                rows.add(cursor?.getString(1)) //get word
            }

            cursor?.close()
            close()

            return rows
        }else{
            close()
            return arrayListOf<String>()
        }
    }

}
