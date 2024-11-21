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

    // Variables to store parameters passed to the fragment
    private var param1: String? = null
    private var param2: String? = null
    private var fullName: String? = null
    private var serviceDescription: String? = null

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve arguments passed to the fragment
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            fullName = it.getString(ARG_FULL_NAME)
            serviceDescription = it.getString(ARG_SERVICE_DESCRIPTION)
        }
    }

    // Inflate the fragment layout and initialize the views
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_service_request, container, false)

        // Retrieve the full name of the user from SharedPreferences
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("User Prefs", MODE_PRIVATE)
        val displayName = sharedPreferences.getString("FULL_NAME", "User ") ?: "User "

        // If serviceDescription is null, use a default value
        val displayService = serviceDescription ?: "Service"

        // Find the TextView to display the thank you message
        val thankYouText = view.findViewById<TextView>(R.id.thankYouText)
        // Set the thank you message dynamically
        thankYouText.text = "Thank you $displayName,\nYour $displayService has been requested!"

        // Find the ImageView for the checkmark icon
        val checkmarkIcon = view.findViewById<ImageView>(R.id.checkmarkIcon)
        // Make the checkmark icon visible
        checkmarkIcon.visibility = View.VISIBLE

        // Call method to save the service request to Firestore
        saveServiceRequest(displayName, displayService)

        return view
    }

    // Method to save the service request to Firestore
    private fun saveServiceRequest(fullName: String, serviceDescription: String) {
        // Get the Firestore instance
        val firestore = FirebaseFirestore.getInstance()

        // Create a new ServiceRequest object with provided data
        val serviceRequest = ServiceRequest("", fullName, serviceDescription)

        // Add the service request to the Firestore collection
        firestore.collection("service_requests").add(serviceRequest)
            .addOnSuccessListener { documentReference ->
                // Log the success message with the document ID
                Log.d("Firestore", "Service request saved successfully with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Log the error message if the request fails
                Log.e("Firestore", "Error saving service request: ${e.message}")
            }
    }

    // Companion object to create a new instance of the fragment with arguments
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

