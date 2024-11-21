package com.example.xbcad7319

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.xbcad7311.R
import com.example.xbcad7319.data.model.ServiceRequest
import com.google.firebase.firestore.FirebaseFirestore

class AdminHistoryRequests : Fragment() {

    // Declare Firebase Firestore instance
    private lateinit var firestore: FirebaseFirestore
    // Declare ListView for displaying service request history
    private lateinit var listView: ListView
    // Declare an ArrayAdapter for populating the ListView
    private lateinit var adapter: ArrayAdapter<String>
    // List to hold the history of service requests
    private var historyRequests = mutableListOf<ServiceRequest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_admin_history_requests, container, false)

        // Initialize Firestore instance
        firestore = FirebaseFirestore.getInstance()
        // Initialize ListView by finding it in the inflated view
        listView = view.findViewById(R.id.lvHistoryRequests)

        // Initialize ArrayAdapter with a simple list item layout and an empty list
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        // Set the adapter to the ListView
        listView.adapter = adapter

        // Load the service request history data (only Accepted or Denied status)
        loadHistoryRequests()

        return view
    }

    // Function to load history requests from Firestore
    private fun loadHistoryRequests() {
        // Query Firestore for service requests that have "Accepted" or "Denied" status
        firestore.collection("service_requests_history")
            .whereIn("status", listOf("Accepted", "Denied"))
            .get()
            .addOnSuccessListener { documents ->
                // Clear any previous data in the historyRequests list and adapter
                historyRequests.clear()
                adapter.clear()

                // Log the number of documents retrieved
                Log.d("HistoryRequests", "Documents retrieved: ${documents.size()}")

                // Check if no documents were found
                if (documents.isEmpty) {
                    Log.d("HistoryRequests", "No history requests found.")
                    // Show a toast message to inform the user that no history requests were found
                    Toast.makeText(requireContext(), "No history requests found", Toast.LENGTH_SHORT).show()
                } else {
                    // Loop through each document retrieved from Firestore
                    for (document in documents) {
                        // Convert the Firestore document to a ServiceRequest object
                        val request = document.toObject(ServiceRequest::class.java)
                        // Set the document ID to the request's ID
                        request.id = document.id
                        // Add the request to the historyRequests list
                        historyRequests.add(request)

                        // Log the details of each request
                        Log.d("HistoryRequests", "Request: ${request.fullName}, Status: ${request.status}")

                        // Prepare a string representation of the request to display in the ListView
                        val displayText = "${request.fullName} - ${request.service_description} (${request.status})"
                        // Add the formatted string to the adapter
                        adapter.add(displayText)
                    }
                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                // Log an error message if the query fails
                Log.e("HistoryRequests", "Error fetching documents: $exception")
                // Show a toast message if there was an error loading the history requests
                Toast.makeText(requireContext(), "Failed to load history requests", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        // Static method to create a new instance of AdminHistoryRequests with parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminHistoryRequests().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
    }
}

