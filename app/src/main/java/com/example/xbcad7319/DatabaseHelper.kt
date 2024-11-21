package com.example.xbcad7319


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.xbcad7319.data.model.Report

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "reports.db" // Name of the database
        private const val DATABASE_VERSION = 1 // Database version
        private const val TABLE_NAME = "reports" // Name of the table
        private const val COLUMN_ID = "id" // Column for the report ID
        private const val COLUMN_TITLE = "title" // Column for the report title
        private const val COLUMN_DETAILS = "details" // Column for the report details
    }

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DETAILS TEXT)")
        db.execSQL(createTable) // Execute SQL statement to create the table
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") // Drop the old table
        onCreate(db) // Create the new table
    }

    // Method to insert a new report into the database
    fun insertReport(title: String, details: String) {
        val db = writableDatabase // Get writable database
        val cv = ContentValues() // Create ContentValues to hold the data
        cv.put(COLUMN_TITLE, title) // Put title into ContentValues
        cv.put(COLUMN_DETAILS, details) // Put details into ContentValues
        db.insert(TABLE_NAME, null, cv) // Insert the data into the table
        db.close() // Close the database
    }

    // Method to retrieve all reports from the database
    fun getReports(): List<Report> {
        val reports = mutableListOf<Report>() // List to hold the reports
        val db = readableDatabase // Get readable database
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null) // Query to select all reports

        // Check if the cursor has any data
        if (cursor.moveToFirst()) {
            do {
                // Get the indices of the columns
                val titleIndex = cursor.getColumnIndex(COLUMN_TITLE)
                val detailsIndex = cursor.getColumnIndex(COLUMN_DETAILS)

                // Check if the indices are valid
                if (titleIndex != -1 && detailsIndex != -1) {
                    val title = cursor.getString(titleIndex) // Get the title
                    val details = cursor.getString(detailsIndex) // Get the details
                    reports.add(Report(title, details)) // Add report to the list
                }
            } while (cursor.moveToNext()) // Move to the next record
        }
        cursor.close() // Close the cursor
        db.close() // Close the database
        return reports // Return the list of reports
    }
}