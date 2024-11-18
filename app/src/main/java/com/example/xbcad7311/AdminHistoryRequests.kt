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

    private lateinit var firestore: FirebaseFirestore
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var historyRequests = mutableListOf<ServiceRequest>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_history_requests, container, false)

        // Initialize Firestore and ListView
        firestore = FirebaseFirestore.getInstance()
        listView = view.findViewById(R.id.lvHistoryRequests)

        // Set up the ArrayAdapter for displaying request details in ListView
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        // Load history requests (only Accepted or Denied)
        loadHistoryRequests()

        return view
    }


    private fun loadHistoryRequests() {
        firestore.collection("service_requests_history")
            .whereIn("status", listOf("Accepted", "Denied"))
            .get()
            .addOnSuccessListener { documents ->
                historyRequests.clear()
                adapter.clear()

                Log.d("HistoryRequests", "Documents retrieved: ${documents.size()}")

                if (documents.isEmpty) {
                    Log.d("HistoryRequests", "No history requests found.")
                    Toast.makeText(requireContext(), "No history requests found", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val request = document.toObject(ServiceRequest::class.java)
                        request.id = document.id
                        historyRequests.add(request)

                        // Log the request details
                        Log.d("HistoryRequests", "Request: ${request.fullName}, Status: ${request.status}")

                        // Add a string representation of the request to the adapter
                        val displayText = "${request.fullName} - ${request.service_description} (${request.status})"
                        adapter.add(displayText)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("HistoryRequests", "Error fetching documents: $exception")
                Toast.makeText(requireContext(), "Failed to load history requests", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
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

