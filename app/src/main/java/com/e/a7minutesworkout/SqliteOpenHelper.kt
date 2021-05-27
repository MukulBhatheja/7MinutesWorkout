package com.e.a7minutesworkout

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class SqliteOpenHelper(context: Context,factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION) {




    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_HISTORY_TABLE = ("CREATE TABLE " +
                TABLE_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_COMPLETED_DATE
                + " TEXT)") // Create History Table Query.
        db.execSQL(CREATE_HISTORY_TABLE) // Executing the create table query.
    }




    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY") // It drops the existing history table
        onCreate(db) // Calls the onCreate function so all the updated tables will be created.
    }



    fun addDate(date: String) {
        val values = ContentValues() // Creates an empty set of values using the default initial size
        values.put(COLUMN_COMPLETED_DATE,date)
        // Putting the value to the column along with the value.

        val db = this.writableDatabase // Create and/or open a database that will be used for reading and writing.
        db.insert(TABLE_HISTORY, null, values) // Insert query is return
        db.close() // Database is closed after insertion.
    }


    fun deleteDate(idt:String){
        try {
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(COLUMN_COMPLETED_DATE, idt)
            db.delete(TABLE_HISTORY, "$COLUMN_COMPLETED_DATE=$idt",null)
            db.close()


        } catch (e: Exception)
        {
            Log.i("Delete","check this")
        }


    }

    fun getAllCompletedDatesList(): ArrayList<String> {
        val list = ArrayList<String>()
        val db =this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_HISTORY", null)


        while (cursor.moveToNext()) {
            // Returns the zero-based index for the given column name, or -1 if the column doesn't exist.

            val dateValue=(cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETED_DATE)))
            list.add(dateValue)

        }
        cursor.close() // Cursor is closed after its used.
        return list
    }

    companion object {
        private const val DATABASE_VERSION = 1 // This DATABASE Version
        private const val DATABASE_NAME = "SevenMinutesWorkout.db" // Name of the DATABASE
        private const val TABLE_HISTORY = "history" // Table Name
        private const val COLUMN_ID = "_id" // Column Id
        private const val COLUMN_COMPLETED_DATE = "completed_date"

        // Column for Completed Date
    }
}



