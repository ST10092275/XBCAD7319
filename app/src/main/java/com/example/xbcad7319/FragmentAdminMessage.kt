package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xbcad7311.databinding.FragmentAdminMessageBinding
import com.example.xbcad7319.data.Message
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class FragmentAdminMessages : Fragment() {

    // View binding for fragment layout
    private var _binding: FragmentAdminMessageBinding? = null
    private val binding get() = _binding!!

    // Firebase Database reference and local message list/adapter variables
    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessagesAdapter
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment and initialize binding
        _binding = FragmentAdminMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Database reference for the "messages" node
        database = FirebaseDatabase.getInstance().getReference("messages")

        // Initialize message list and adapter
        messageList = mutableListOf()
        messageAdapter = MessagesAdapter(messageList)

        // Set up RecyclerView with linear layout and adapter
        binding.recyclerViewAdminMessages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }

        // Set up the Send button functionality
        binding.adminSendButton.setOnClickListener {
            val messageText = binding.adminMessageEdit.text.toString().trim()

            // Check if message is not empty before sending
            if (messageText.isNotEmpty()) {
                // Create a new message object with unique ID, admin sender, and current timestamp
                val newMessage = Message(
                    id = database.push().key ?: "unique_id",
                    senderId = "Admin",
                    content = messageText,
                    timestamp = System.currentTimeMillis()
                )

                // Add the new message to Firebase Database and update the local message list
                database.child(newMessage.id).setValue(newMessage).addOnSuccessListener {
                    messageList.add(newMessage)
                    messageAdapter.notifyItemInserted(messageList.size - 1)

                    // Clear the input field and scroll to the latest message
                    binding.adminMessageEdit.text.clear()
                    binding.recyclerViewAdminMessages.scrollToPosition(messageList.size - 1)
                }
            }
        }
    }

    // Clean up the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

