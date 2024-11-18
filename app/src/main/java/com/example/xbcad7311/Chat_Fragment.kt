package com.example.xbcad7311

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.data.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var messageAdapter: MessagesAdapter
    private val messages = mutableListOf<Message>()

    private var chatId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        messageInput = view.findViewById(R.id.messageBox)
        sendButton = view.findViewById(R.id.sendButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter = MessagesAdapter(
            messages,
            currentUserId = TODO()
        )
        recyclerView.adapter = messageAdapter

        database = FirebaseDatabase.getInstance().reference

        val clientFullname = arguments?.let { ChatFragmentArgs.fromBundle(it).clientFullname }
        val adminId = FirebaseAuth.getInstance().currentUser?.uid

        chatId = if (adminId != null && clientFullname != null) {
            "$adminId-${clientFullname.lowercase()}"
        } else null

        chatId?.let { loadMessages(it) }

        sendButton.setOnClickListener {
            sendMessage()
        }

        return view
    }

    private fun loadMessages(chatId: String) {
        database.child("chats").child(chatId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messages.add(message)
                    messageAdapter.notifyItemInserted(messages.size - 1)
                    recyclerView.scrollToPosition(messages.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {}

            // Unused callbacks
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        })
    }

    private fun sendMessage() {
        val message = messageInput.text.toString().trim()
        if (message.isNotEmpty() && chatId != null) {
            val messageObj = Message(
                id = database.push().key ?: "",
                senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                content = message,
                timestamp = System.currentTimeMillis()
            )
            database.child("chats").child(chatId!!).push().setValue(messageObj)
            messageInput.text.clear()
        }
    }
}

