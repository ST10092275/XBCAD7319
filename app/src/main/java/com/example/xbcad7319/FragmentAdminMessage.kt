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
import com.google.firebase.auth.FirebaseAuth
// other imports

class FragmentAdminMessage : Fragment() {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessagesAdapter
    private lateinit var database: DatabaseReference
    private lateinit var requestId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if the user is authenticated
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(context, "Please sign in to access the chat room.", Toast.LENGTH_SHORT).show()
            return // Exit if the user is not authenticated
        }

        messageList = mutableListOf()
        messageAdapter = MessagesAdapter(messageList)

        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }

        database = FirebaseDatabase.getInstance().getReference("chats").child(requestId).child("messages")
        setupMessageListener()

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEdit.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val messageId = database.push().key ?: return@setOnClickListener

                val newMessage = Message(
                    id = messageId,
                    senderId = "Admin",
                    content = messageText,
                    timestamp = System.currentTimeMillis()
                )

                database.child(messageId).setValue(newMessage).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        messageList.add(newMessage)
                        messageAdapter.notifyItemInserted(messageList.size - 1)
                        binding.messageEdit.text.clear()
                        binding.recyclerViewMessages.scrollToPosition(messageList.size - 1)
                    } else {
                        Toast.makeText(context, "Failed to send message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupMessageListener() {
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

    companion object {
        fun newInstance(requestId: String) = FragmentAdminMessage().apply {
            arguments = Bundle().apply {
                putString("requestId", requestId)
            }
        }
    }
}
