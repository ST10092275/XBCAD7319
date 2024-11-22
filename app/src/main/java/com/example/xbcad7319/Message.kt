package com.example.xbcad7319


import com.google.firebase.Timestamp

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now() // Use Timestamp.now without parentheses
)