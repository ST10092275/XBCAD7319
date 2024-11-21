package com.example.xbcad7319

import android.os.Bundle
import android.content.Intent
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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminViewRequests.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminViewRequests : Fragment() {

    // Parameters for fragment initialization, used for determining the type of requests to load
    private var param1: String? = null
    private var param2: String? = null

    // Firestore instance and ListView to display requests
    private lateinit var firestore: FirebaseFirestore
    private lateinit var listView: ListView

    // Adapter for the ListView to display request information
    private lateinit var adapter: ArrayAdapter<String>

    // List to hold the service requests
    private var requests = mutableListOf<ServiceRequest>()

    // Called when the fragment is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get fragment parameters passed during initialization
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // Called to create the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_view_requests, container, false)

        // Initialize Firestore and the ListView
        firestore = FirebaseFirestore.getInstance()
        listView = view.findViewById(R.id.lvRequests)

        // Set up the ArrayAdapter to display request details as list items in ListView
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        // Load requests based on the fragment parameter (Pending or History)
        loadRequests()

        // Set an item click listener to handle selection of a request
        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedRequest: ServiceRequest = requests[position]

            // Create and navigate to the decision fragment for the selected request
            val fragment: FragmentAdminDecision = FragmentAdminDecision.newInstance(
                selectedRequest.id,
                selectedRequest.fullName,
                selectedRequest.service_description
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace the current fragment with decision fragment
                .addToBackStack(null) // Add to back stack for navigation history
                .commit()
        }

        return view
    }

    // Function to load requests based on the status type (Pending or History)
    private fun loadRequests() {
        // Determine which Firestore collection to query based on 'param1' (Pending or History)
        val collectionName = if (param1 == "Pending") {
            "service_requests" // For pending requests
        } else {
            "service_requests_history" // For completed or denied requests
        }

        // Build query depending on whether we are looking for Pending or Accepted/Denied requests
        val query = if (param1 == "Pending") {
            firestore.collection(collectionName).whereEqualTo("status", "Pending")
        } else {
            firestore.collection(collectionName).whereIn("status", listOf("Accepted", "Denied"))
        }

        // Fetch the service requests from Firestore
        query.get()
            .addOnSuccessListener { documents ->
                // Clear existing requests and adapter data
                requests.clear()
                adapter.clear()

                // Handle empty results
                if (documents.isEmpty) {
                    Log.d("Requests", "No requests found.")
                    Toast.makeText(requireContext(), "No requests found", Toast.LENGTH_SHORT).show()
                } else {
                    // Process each document and convert to ServiceRequest object
                    for (document in documents) {
                        val request = document.toObject(ServiceRequest::class.java)
                        request.id = document.id
                        requests.add(request)

                        // Add a string representation of the request to the adapter for display
                        val displayText = "${request.fullName} - ${request.service_description} (${request.status})"
                        adapter.add(displayText)
                    }
                    // Notify adapter that data has changed and the list needs to be refreshed
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure in fetching documents
                Log.e("Requests", "Error fetching documents: $exception")
                Toast.makeText(requireContext(), "Failed to load requests", Toast.LENGTH_SHORT).show()
            }
    }

    // Factory method to create a new instance of the fragment with the required parameters
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminViewRequests().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1) // Pass request status type (Pending or History)
                    putString(ARG_PARAM2, param2) // Can be used for additional parameters
                }
            }
    }
}

