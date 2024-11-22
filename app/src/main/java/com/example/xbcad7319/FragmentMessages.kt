package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xbcad7311.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast


class FragmentMessages : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var adminListView: ListView
    private val admins = mutableListOf<Pair<String, String>>() // Pair of adminId and fullname

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        adminListView = view.findViewById(R.id.adminListView)

        loadAdmins()

        adminListView.setOnItemClickListener { _, _, position, _ ->
            val adminId = admins[position].first
            val adminName = admins[position].second

            val bundle = Bundle()
            bundle.putString("ADMIN_ID", adminId)  // Pass admin details
            val fragment = ClientChatFragment()
            bundle.putString("admin_name", adminName)
            fragment.arguments = bundle


            // Navigate to the chat fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace with your container ID
                .addToBackStack(null) // Optional: Add to backstack for navigation
                .commit()
        }
    }

    private fun loadAdmins() {
        db.collection("users")
            .whereEqualTo("role", "admin") // Query users with "admin" role
            .get()
            .addOnSuccessListener { querySnapshot ->
                admins.clear()
                for (document in querySnapshot.documents) {
                    val adminId = document.id
                    val fullname = document.getString("fullname") ?: "Unknown Admin"
                    admins.add(Pair(adminId, fullname))
                }

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    admins.map { it.second } // Display full names in the list
                )
                adminListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error loading admins: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
