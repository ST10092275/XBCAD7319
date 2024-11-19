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

class FragmentAdminService : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var serviceRequests = mutableListOf<ServiceRequest>()

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
        val view = inflater.inflate(R.layout.fragment_admin_service, container, false)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()
        listView = view.findViewById(R.id.lvRequests)

        // Set up the ArrayAdapter
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        loadRequests()

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedRequest = serviceRequests[position]
            val fragment = FragmentAdminDecision.newInstance(
                selectedRequest.id,
                selectedRequest.fullName,
                selectedRequest.service_description
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace with your fragment container's ID
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loadRequests() {
        firestore.collection("service_requests")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("ServiceRequest", "Documents fetched: ${documents.size()}")
                serviceRequests.clear()
                adapter.clear()

                if (!documents.isEmpty) {
                    for (document in documents) {
                        val request = document.toObject(ServiceRequest::class.java)
                        request.id = document.id // Set the document ID
                        serviceRequests.add(request)
                        adapter.add("${request.fullName} requested for ${request.service_description}")
                        Log.d("ServiceRequest", "Added request: $request")
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("ServiceRequest", "No data found in Firestore.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ServiceRequest", "Error fetching documents: $exception")
                Toast.makeText(requireContext(), "Failed to load requests", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentAdminService().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
