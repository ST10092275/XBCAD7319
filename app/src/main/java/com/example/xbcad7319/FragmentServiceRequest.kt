package com.example.xbcad7319

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.xbcad7311.R
import com.example.xbcad7319.data.model.ServiceRequest
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_FULL_NAME = "full_name"
private const val ARG_SERVICE_DESCRIPTION = "service_description"

class FragmentServiceRequest : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var fullName: String? = null
    private var serviceDescription: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            fullName = it.getString(ARG_FULL_NAME)
            serviceDescription = it.getString(ARG_SERVICE_DESCRIPTION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_service_request, container, false)
        // Retrieve the full name from Shared Preferences
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("User Prefs",  MODE_PRIVATE)
        val displayName = sharedPreferences.getString("FULL_NAME", "User ") ?: "User "
        val displayService = serviceDescription ?: "Service"

        // Find and update the thank you text view
        val thankYouText = view.findViewById<TextView>(R.id.thankYouText)
        thankYouText.text = "Thank you $displayName,\nYour $displayService has been requested!"

        // Show the checkmark icon
        val checkmarkIcon = view.findViewById<ImageView>(R.id.checkmarkIcon)
        checkmarkIcon.visibility = View.VISIBLE

        // Save service request to Firestore
        saveServiceRequest(displayName, displayService)
        return view
    }

    private fun saveServiceRequest(fullName: String, serviceDescription: String) {
        val firestore = FirebaseFirestore.getInstance()
        val serviceRequest = ServiceRequest("", fullName, serviceDescription)

        // Save the request
        firestore.collection("service_requests").add(serviceRequest)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Service request saved successfully with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving service request: ${e.message}")
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(fullName: String, serviceDescription: String) =
            FragmentServiceRequest().apply {
                arguments = Bundle().apply {
                    putString(ARG_FULL_NAME, fullName)
                    putString(ARG_SERVICE_DESCRIPTION, serviceDescription)
                }
            }
    }
}
