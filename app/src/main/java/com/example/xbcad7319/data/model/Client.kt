package com.example.xbcad7319.data.model

data class Client(
    val fullname: String, // Full name of the client
    val phone_number: String, // Phone number of the client
    val id: String, // Document ID from Firestore
    val clientId: String // Unique client identifier
)