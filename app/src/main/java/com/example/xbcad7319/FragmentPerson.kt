package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xbcad7311.R
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.example.xbcad7319.data.model.Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FragmentPerson : Fragment() {

    private lateinit var listView: ListView // ListView to display clients
    private lateinit var adapter: ClientAdapter // Adapter for the ListView
    private var clients: MutableList<Client> = mutableListOf() // List to hold client data
    private lateinit var auth: FirebaseAuth // Firebase Authentication instance
    private lateinit var db: FirebaseFirestore // Firestore instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_person, container, false)

        // Initialize ListView and Adapter
        listView = view.findViewById(R.id.client_list)
        adapter = ClientAdapter(requireContext(), clients) // Create adapter with client data
        listView.adapter = adapter // Set the adapter to the ListView

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Get current user and fetch their details
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            Log.d("FragmentPerson", "Current user ID: ${currentUser.uid}")
            fetchUserDetails(currentUser.uid) // Fetch details of the current user
        } else {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
        }

        // Set item click listener for the ListView
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedClient = clients[position] // Get selected client info
            val fragmentProfile = Profile.newInstance(
                selectedClient.fullname, // Pass fullname to FragmentProfile
                selectedClient.phone_number // Pass phone number to FragmentProfile
            )
            // Navigate to FragmentProfile
            navigateToFragment(fragmentProfile)
        }

        return view // Return the inflated view
    }

    // Method to fetch user details from Firestore
    private fun fetchUserDetails(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userEmail = document.getString("email") ?: "No Email"
                    Log.d("FragmentPerson", "Fetched user email: $userEmail")
                    fetchClients(userEmail) // Fetch clients associated with the user's email
                } else {
                    Toast.makeText(requireContext(), "User document not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error fetching user details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Method to fetch clients from Firestore
    private fun fetchClients(userEmail: String) {
        db.collection("users").whereEqualTo("role", "client") // Query for clients
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val fullname = document.getString("fullname") ?: "Unknown User"
                    val phoneNumber = document.getString("number") ?: "No Phone"
                    val clientId = document.id
                    Log.d("FragmentPerson", "Fetched client: Fullname: $fullname, Phone: $phoneNumber, Email: $userEmail")
                    val client = Client(fullname, phoneNumber, userEmail, clientId) // Create client object
                    clients.add(client) // Add client to the list
                }
                adapter.notifyDataSetChanged() // Notify adapter of data change
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error fetching user details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Method to navigate to another fragment
    private fun navigateToFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment) // Replace current fragment with new fragment
            ?.addToBackStack(null) // Add to back stack
            ?.commit() // Commit the transaction
    }
}


//Android Developers. (2024). Fragments. Available at: https://developer.android.com/guide/fragments [Accessed 30 Oct. 2024].
//Firebase. (2024). Get Started with Cloud Firestore. Available at: https://firebase.google.com/docs/firestore [Accessed 30 Oct. 2024].

