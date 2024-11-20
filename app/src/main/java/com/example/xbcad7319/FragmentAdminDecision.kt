package com.example.xbcad7319


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.xbcad7311.R
import com.google.firebase.firestore.FirebaseFirestore

class FragmentAdminDecision : Fragment() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var requestId: String
    private lateinit var clientName: String
    private lateinit var serviceType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firestorm
        firestore = FirebaseFirestore.getInstance()
        // Get request details from the arguments
        arguments?.let {
            requestId = it.getString("requestId") ?: ""
            clientName = it.getString("clientName") ?: "Unknown Client"
            serviceType = it.getString("serviceType") ?: "Unknown Service"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_decision, container, false)

        // Update the UI with request details
        val requestDetailText = "$clientName requested for $serviceType"
        view.findViewById<TextView>(R.id.tvRequestDetail).text = requestDetailText
        // Set the appropriate image based on the serviceType
        val serviceImageView = view.findViewById<ImageView>(R.id.ivServiceImage)
        when (serviceType) {
            "Wi-Fi" -> serviceImageView.setImageResource(R.drawable.wifi)
            "Telephones" -> serviceImageView.setImageResource(R.drawable.telephone)
            "ICT support services" -> serviceImageView.setImageResource(R.drawable.helpdesk)
            "Cartridges and Stationery" -> serviceImageView.setImageResource(R.drawable.stationery)
            else -> serviceImageView.setImageResource(R.drawable.default_image) // Fallback image
        }

        // Set onClick listeners for the Accept and Deny buttons
        view.findViewById<Button>(R.id.btnAccept).setOnClickListener {
            updateRequestStatus("Accepted")
        }

        view.findViewById<Button>(R.id.btnDeny).setOnClickListener {
            updateRequestStatus("Denied")
        }

        return view
    }

    private fun updateRequestStatus(status: String) {
        // Update the request status in Firestore
        firestore.collection("service_requests").document(requestId)
            .update("status", status)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Request $status", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed() // Go back to the previous screen after the update
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update request", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(requestId: String, clientName: String, serviceType: String) =
            FragmentAdminDecision().apply {
                arguments = Bundle().apply {
                    putString("requestId", requestId)
                    putString("clientName", clientName)
                    putString("serviceType", serviceType)
                }
            }
    }

}
