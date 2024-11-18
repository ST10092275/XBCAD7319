// FragmentAdminMessage.kt
package com.example.xbcad7311

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7319.ClientAdapter
import com.example.xbcad7319.FragmentMessage
import com.example.xbcad7319.data.Client
import com.google.firebase.database.*

class FragmentAdminMessage : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var clientAdapter: ClientAdapter
    private val clientList = mutableListOf<Client>()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_message, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        clientAdapter = ClientAdapter(clientList) { client ->
            navigateToClientChat(client)
        }
        recyclerView.adapter = clientAdapter

        database = FirebaseDatabase.getInstance().getReference("clients")
        loadClients()
        return view
    }

    private fun loadClients() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clientList.clear()
                for (clientSnapshot in snapshot.children) {
                    val client = clientSnapshot.getValue(Client::class.java)
                    if (client != null) clientList.add(client)
                }
                clientAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load clients", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToClientChat(client: Client) {
        val fragment = FragmentMessage.newInstance(client.requestId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
