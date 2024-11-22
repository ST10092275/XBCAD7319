package com.example.xbcad7319

import android.content.Intent
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
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMessage.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAdminMessage : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var clientListView: ListView
    private val clients = mutableListOf<Pair<String, String>>() // Pair of clientId and fullname

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_message, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        clientListView = view.findViewById(R.id.clientListView)

        loadClients()

        clientListView.setOnItemClickListener { _, _, position, _ ->
            val clientId = clients[position].first
            val clientName = clients[position].second
            val fragment = AdminChatFragment()
            val bundle = Bundle()
            bundle.putString("client_id", clientId) // Pass client details
            fragment.arguments = bundle

// Navigate to the fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace with your container ID
                .addToBackStack(null) // Optional: Add to backstack for navigation
                .commit()

            // Optional: Navigate to chat or details screen
            navigateToChat(clientId, clientName)
        }
    }

    private fun loadClients() {
        db.collection("users")
            .whereEqualTo("role", "client") // Query users with "client" role
            .get()
            .addOnSuccessListener { querySnapshot ->
                clients.clear()
                Log.d("ClientList", "Fetched ${querySnapshot.size()} clients")
                for (document in querySnapshot.documents) {
                    val clientId = document.id
                    val fullname = document.getString("fullname") ?: "Unknown Client"
                    Log.d("ClientList", "Client found: $clientId, $fullname")  // Log client info
                    clients.add(Pair(clientId, fullname))
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    clients.map { it.second } // Display full names in the list
                )
                clientListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("ClientList", "Error loading clients: ${exception.message}")  // Log failure message
                Toast.makeText(requireContext(), "Error loading clients: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun navigateToChat(clientId: String, clientName: String) {
        // Instantiate the AdminChatFragment
        val chatFragment = AdminChatFragment().apply {
            arguments = Bundle().apply {
                putString("CLIENT_ID", clientId)
                putString("CLIENT_NAME", clientName)
            }
        }

        // Perform the fragment transaction
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, chatFragment) // Ensure 'fragment_container' exists in your layout
            .addToBackStack(null) // Adds this transaction to the back stack
            .commit()
    }

}