package com.example.xbcad7319

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
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
=======
import com.example.xbcad7311.R
>>>>>>> 7b2294277f9743e8167c9cad78d4983238e00ca4

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

<<<<<<< HEAD
    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessagesAdapter
    private lateinit var database: DatabaseReference
    private lateinit var requestId: String
=======
/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMessages.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMessages : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
>>>>>>> 7b2294277f9743e8167c9cad78d4983238e00ca4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
<<<<<<< HEAD
            requestId = it.getString("requestId") ?: ""
=======
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
>>>>>>> 7b2294277f9743e8167c9cad78d4983238e00ca4
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
<<<<<<< HEAD
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    senderId = "Client",
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
=======
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMessages.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMessages().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
>>>>>>> 7b2294277f9743e8167c9cad78d4983238e00ca4
                }
            }
    }
<<<<<<< HEAD

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
        fun newInstance(requestId: String) = FragmentMessage().apply {
            arguments = Bundle().apply {
                putString("requestId", requestId)
            }
        }
    }
}
=======
}
>>>>>>> 7b2294277f9743e8167c9cad78d4983238e00ca4
