package com.example.xbcad7319.data

data class Message(
    val id: String, // Unique identifier for the message
    val senderId: String, // ID of the user who sent the message
    val content: String, // The actual message content
    val timestamp: Long // Time the message was sent
)

