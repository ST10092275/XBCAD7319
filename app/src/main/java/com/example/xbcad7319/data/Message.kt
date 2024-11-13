package com.example.xbcad7319.data

data class Message(
    val id: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = 0
) {
    // No-argument constructor for Firebase
    constructor() : this("", "", "", 0L)
}