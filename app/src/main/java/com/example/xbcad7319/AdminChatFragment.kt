package com.example.xbcad7319

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.R
import com.example.xbcad7311.databinding.FragmentAdminChatBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions


class AdminChatFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var firestore: FirebaseFirestore

    private lateinit var currentUserId: String
    private lateinit var clientId: String
    private lateinit var chatId: String
    private val messages = mutableListOf<Message>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAdminChatBinding.inflate(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        Log.d("AdminChatFragment", "Current User ID: $currentUserId")

        clientId = arguments?.getString("CLIENT_ID") ?: ""
        chatId = generateChatId(currentUserId, clientId)

        recyclerView = binding.recyclerView
        messageInput = binding.messageInput
        sendButton = binding.sendButton

        // Initialize RecyclerView and Adapter
        messageAdapter = MessageAdapter(messages, currentUserId)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = messageAdapter

        // Fetch and listen for messages
        fetchMessages()

        sendButton.setOnClickListener {
            sendMessage()
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        messageAdapter = MessageAdapter(messages, currentUserId)
        recyclerView.adapter = messageAdapter

    }

    private fun fetchMessages() {
        firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("ChatFragment", "Error fetching messages", exception)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    Log.d("ChatFragment", "Fetched snapshot size: ${snapshot.documents.size}")
                    messages.clear()  // Clear the list before adding new messages

                    snapshot.documents.forEach { doc ->
                        val message = doc.toObject(Message::class.java)
                        if (message != null) {
                            Log.d("ChatFragment", "Fetched message: ${message.text} | Sender: ${message.senderId}")
                            messages.add(message)
                        }
                    }

                    // Notify the adapter and update the RecyclerView
                    messageAdapter.updateMessages(messages)
                    recyclerView.scrollToPosition(messages.size - 1)
                } else {
                    Log.d("ChatFragment", "No messages found")
                }
            }
    }


    private fun sendMessage() {
        val messageText = messageInput.text.toString().trim()
        if (messageText.isNotEmpty()) {
            val message = Message(
                senderId = currentUserId,
                receiverId = clientId,
                text = messageText,
                timestamp = Timestamp.now()
            )

            val chatRef = firestore.collection("chats").document(chatId)
            chatRef.collection("messages")
                .add(message)
                .addOnSuccessListener {
                    Log.d("AdminChatFragment", "Message sent successfully: $messageText")
                    messageInput.text.clear()
                    messages.add(message)
                    messageAdapter.updateMessages(messages)
                    recyclerView.scrollToPosition(messages.size - 1)
                }
                .addOnFailureListener { exception ->
                    Log.e("ChatFragment", "Error sending message", exception)
                }

            // Optionally, update the chat document with the latest message
            chatRef.set(
                mapOf(
                    "chatId" to chatId,
                    "users" to listOf(currentUserId, clientId),
                    "lastMessage" to messageText,
                    "timestamp" to Timestamp.now()
                ),
                SetOptions.merge()
            )
        }
    }


    private fun generateChatId(user1: String, user2: String): String {
        val chatId = if (user1 < user2) "$user1-$user2" else "$user2-$user1"
        Log.d("ChatFragment", "Generated Chat ID: $chatId")
        return chatId
    }

}
