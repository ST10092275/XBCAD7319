package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.xbcad7311.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Profile : Fragment() {

    private var fullname: String? = null // User's full name
    private var phone_number: String? = null // User's phone number
    private lateinit var nameTextView: TextView // TextView to display fullname
    private lateinit var phoneTextView: TextView // TextView to display phone number
    private lateinit var serviceRequestsTextView: TextView // TextView for displaying service requests
    private lateinit var serviceRequestsTextView2: TextView // TextView for displaying meetings
    private lateinit var db: FirebaseFirestore // Firestore instance
    private lateinit var auth: FirebaseAuth // Firebase Auth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve passed arguments
        arguments?.let {
            fullname = it.getString(ARG_PARAM1)
            phone_number = it.getString(ARG_PARAM2)
        }
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize TextViews
        nameTextView = view.findViewById(R.id.textView_value)
        phoneTextView = view.findViewById(R.id.textView_names)
        serviceRequestsTextView = view.findViewById(R.id.serviceRequestsTextView) // For service requests
        serviceRequestsTextView2 = view.findViewById(R.id.serviceRequestsTextView2) // For ScrollView displaying meetings

        // Set user details on the TextViews
        nameTextView.text = fullname ?: "No name"
        phoneTextView.text = phone_number ?: "No phone number"

        // Fetch meetings and service descriptions for the specific user
        fetchMeetings()
        fetchServiceDescriptions()

        // Button to navigate back to FragmentHome
        view.findViewById<Button>(R.id.button).setOnClickListener {
            if (fullname != null) {
                val fragmentHome = FragmentHome.newInstance(fullname!!)
                requireFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentHome, "FRAGMENT_HOME_TAG")
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Fullname is not available", Toast.LENGTH_SHORT).show()
            }
        }

        return view // Return the inflated view
    }

    private fun fetchMeetings() {
        // Get the current FragmentHome instance to access the RecyclerView data
        val fragmentHome = requireFragmentManager().findFragmentByTag("FRAGMENT_HOME_TAG") as? FragmentHome
        fragmentHome?.let {
            // Fetch meetings related to the user
            val meetings = it.getMeetingsByClient(fullname) // This method should be implemented in FragmentHome
            if (meetings.isEmpty()) {
                serviceRequestsTextView2.text = "No meetings found." // No meetings available
            } else {
                val meetingDescriptions = StringBuilder()
                for (meeting in meetings) {
                    meetingDescriptions.append("Date: ${meeting.date}, Time: ${meeting.time}, Agenda: ${meeting.agenda}, Client Name: $fullname\n")
                }
                serviceRequestsTextView2.text = meetingDescriptions.toString() // Display meetings
            }
        } ?: run {
            serviceRequestsTextView2.text = "Error retrieving meetings." // Error handling
        }
    }

    private fun fetchServiceDescriptions() {
        val currentUser = auth.currentUser // Get the current authenticated user
        currentUser?.let {
            // Query Firestore for service requests matching the full name
            db.collection("service_requests").whereEqualTo("fullName", fullname).get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        serviceRequestsTextView.text = "No service requests found." // No service requests
                    } else {
                        val serviceDescriptions = StringBuilder()
                        for (document in documents) {
                            val description = document.getString("service_description") ?: "No description"
                            serviceDescriptions.append("$description\n") // Append each service description
                        }
                        serviceRequestsTextView.text = serviceDescriptions.toString() // Display service descriptions
                    }
                }
                .addOnFailureListener { exception ->
                    serviceRequestsTextView.text = "Error fetching service requests: ${exception.message}" // Error handling
                }
        } ?: run {
            serviceRequestsTextView.text = "No authenticated user." // User not authenticated
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1" // Argument key for fullname
        private const val ARG_PARAM2 = "param2" // Argument key for phone number

        // Factory method to create a new instance of FragmentProfile
        @JvmStatic
        fun newInstance(fullname: String, phone_number: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, fullname)
                    putString(ARG_PARAM2, phone_number)
                }
            }
    }
}

//Firebase. (n.d.). Get Started with Firebase Authentication. Available at: https://firebase.google.com/docs/auth/android/start [Accessed 20 Oct. 2023].