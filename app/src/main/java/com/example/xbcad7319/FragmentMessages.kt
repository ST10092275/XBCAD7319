package com.example.xbcad7319

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xbcad7311.databinding.FragmentMessagesBinding
import com.example.xbcad7319.data.Message
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class FragmentMessage : Fragment() {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessagesAdapter
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize message list and adapter
        messageList = mutableListOf()
        messageAdapter = MessagesAdapter(messageList)

        // Set up RecyclerView
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("messages")

        // Set up a listener to retrieve messages from the database
        setupMessageListener()

        // Send button click listener
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEdit.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Create a unique message ID
                val messageId = database.push().key ?: return@setOnClickListener

                // Create a new message object
                val newMessage = Message(id = messageId, senderId = "Client", content = messageText, timestamp = System.currentTimeMillis())

                // Save the message to the database
                database.child(messageId).setValue(newMessage).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Message successfully sent to database
                        messageList.add(newMessage) // Add to local message list
                        messageAdapter.notifyItemInserted(messageList.size - 1)
                        binding.messageEdit.text.clear() // Clear the input field
                        binding.recyclerViewMessages.scrollToPosition(messageList.size - 1) // Scroll to the latest message
                    } else {
                        // Handle failure
                        Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupMessageListener() {
        // Listen for new messages from the database
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val newMessage = dataSnapshot.getValue(Message::class.java)
                newMessage?.let {
                    messageList.add(it)
                    messageAdapter.notifyItemInserted(messageList.size - 1)
                    binding.recyclerViewMessages.scrollToPosition(messageList.size - 1)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

