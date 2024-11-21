package com.example.xbcad7319.data.model
// Data class to represent a service request
data class ServiceRequest(
    var id: String = "",  // Unique identifier for the service request (e.g., database ID)
    var fullName: String = "",  // Full name of the user who submitted the request
    var service_description: String = "",  // Description of the service being requested
    var status: String = "Pending"  // The current status of the request (default is "Pending")
)


