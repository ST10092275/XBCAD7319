// FragmentMessage.kt
package com.example.xbcad7311

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7319.MessagesAdapter
import com.example.xbcad7319.data.Message
import com.google.firebase.database.*

class FragmentMessages : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessagesAdapter
    private val messageList = mutableListOf<Message>()
    private lateinit var database: DatabaseReference
    private lateinit var messageInput: EditText
    private lateinit var sendButton: ImageButton
    private var requestId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            requestId = it.getString(ARG_REQUEST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewMessages)
        messageInput = view.findViewById(R.id.messageInput)
        sendButton = view.findViewById(R.id.sendButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter

        if (requestId != null) {
            database = FirebaseDatabase.getInstance().getReference("messages/$requestId")
            loadMessages()

            sendButton.setOnClickListener {
                sendMessage()
            }
        } else {
            Toast.makeText(requireContext(), "Request ID is missing", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    private fun loadMessages() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) messageList.add(message)
                }
                messageAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messageList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage() {
        val content = messageInput.text.toString().trim()
        if (content.isNotEmpty()) {
            val message = Message(content, "admin_user_id", System.currentTimeMillis())
            database.push().setValue(message).addOnSuccessListener {
                messageInput.text.clear()
                recyclerView.scrollToPosition(messageList.size - 1)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val ARG_REQUEST_ID = "request_id"

        @JvmStatic
        fun newInstance(requestId: String) =
            FragmentMessages().apply {
                arguments = Bundle().apply {
                    putString(ARG_REQUEST_ID, requestId)
                }
            }
    }
}
