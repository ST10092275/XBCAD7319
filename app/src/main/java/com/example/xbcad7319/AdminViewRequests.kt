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



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AdminViewRequests : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var requests = mutableListOf<ServiceRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_view_requests, container, false)

        // Initialize Firestore and ListView
        firestore = FirebaseFirestore.getInstance()
        listView = view.findViewById(R.id.lvRequests)

        // Set up the ArrayAdapter for displaying request details in ListView
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        // Load requests based on the parameter
        loadRequests()

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedRequest: ServiceRequest = requests[position]
            val fragment: FragmentAdminDecision = FragmentAdminDecision.newInstance(
                selectedRequest.id,
                selectedRequest.fullName,
                selectedRequest.service_description
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loadRequests() {
        val collectionName = if (param1 == "Pending") {
            "service_requests"
        } else {
            "service_requests_history"
        }

        val query = if (param1 == "Pending") {
            firestore.collection(collectionName).whereEqualTo("status", "Pending")
        } else {
            firestore.collection(collectionName).whereIn("status", listOf("Accepted", "Denied"))
        }

        query.get()
            .addOnSuccessListener { documents ->
                requests.clear()
                adapter.clear()

                if (documents.isEmpty) {
                    Log.d("Requests", "No requests found.")
                    Toast.makeText(requireContext(), "No requests found", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        val request = document.toObject(ServiceRequest::class.java)
                        request.id = document.id
                        requests.add(request)

                        // Add a string representation of the request to the adapter
                        val displayText = "${request.fullName} - ${request.service_description} (${request.status})"
                        adapter.add(displayText)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Requests", "Error fetching documents: $exception")
                Toast.makeText(requireContext(), "Failed to load requests", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminViewRequests().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}