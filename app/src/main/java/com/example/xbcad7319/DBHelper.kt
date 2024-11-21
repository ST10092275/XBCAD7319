package com.example.xbcad7319

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.xbcad7319.data.model.Meeting

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "meetings.db" // Name of the database
        private const val DATABASE_VERSION = 1 // Database version
        private const val TABLE_MEETINGS = "meetings" // Table name for meetings
        private const val COLUMN_ID = "id" // Column name for ID
        private const val COLUMN_DATE = "date" // Column name for date
        private const val COLUMN_TIME = "time" // Column name for time
        private const val COLUMN_AGENDA = "agenda" // Column name for agenda
        private const val COLUMN_CLIENT_NAME = "client_name" // Column name for client name
        private const val COLUMN_FULLNAME = "fullname" // Column name for fullname
    }

    override fun onCreate(db: SQLiteDatabase) {
        // SQL statement to create the meetings table
        val createTable = ("CREATE TABLE $TABLE_MEETINGS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_TIME TEXT," +
                "$COLUMN_AGENDA TEXT," +
                "$COLUMN_CLIENT_NAME TEXT," +
                "$COLUMN_FULLNAME TEXT)") // Create table with fullname
        db.execSQL(createTable) // Execute the SQL statement
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the existing table if it exists and create a new one
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEETINGS")
        onCreate(db) // Create a new table
    }

    // Method to add a meeting to the database
    fun addMeeting(meeting: Meeting): Long {
        val db = writableDatabase // Get writable database
        val values = ContentValues() // Create ContentValues object to hold meeting data
        values.put(COLUMN_DATE, meeting.date)
        values.put(COLUMN_TIME, meeting.time)
        values.put(COLUMN_AGENDA, meeting.agenda)
        values.put(COLUMN_CLIENT_NAME, meeting.clientName)
        values.put(COLUMN_FULLNAME, meeting.fullname) // Store fullname
        return db.insert(TABLE_MEETINGS, null, values) // Insert values into the database
    }

    // Method to retrieve all meetings from the database
    fun getAllMeetings(): List<Meeting> {
        val meetings = mutableListOf<Meeting>() // List to hold meetings
        val db = readableDatabase // Get readable database
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_MEETINGS", null) // Query to select all meetings

        // Loop through the cursor to extract meeting data
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)) ?: "Unknown Date"
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)) ?: "Unknown Time"
                val agenda = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AGENDA)) ?: "No Agenda"
                val clientName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLIENT_NAME)) ?: "No Client Name"
                val fullname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULLNAME)) ?: "No Fullname"

                // Add the meeting to the list
                meetings.add(Meeting(id, date, time, agenda, clientName, fullname))
            } while (cursor.moveToNext()) // Move to the next row
        }
        cursor.close() // Close the cursor
        return meetings // Return the list of meetings
    }

    // Method to delete a meeting by ID
    fun deleteMeeting(id: Long) {
        val db = writableDatabase // Get writable database
        db.delete(TABLE_MEETINGS, "$COLUMN_ID = ?", arrayOf(id.toString())) // Delete the meeting
        db.close() // Close the database
    }
}

//Android Developers. (2024). Saving Data in SQLite Databases. Available at: https://developer.android.com/training/data-storage/sqlite [Accessed 20 Oct. 2024].