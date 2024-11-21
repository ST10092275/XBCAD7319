package com.example.xbcad7319.data.model

data class Meeting(
    val id: Long,               // Unique identifier for the meeting
    val date: String,           // Date of the meeting
    val time: String,           // Time of the meeting
    val agenda: String,         // Agenda or purpose of the meeting
    val clientName: String,     // Name of the client associated with the meeting
    val fullname: String        // Fullname of the user creating the meeting
)
