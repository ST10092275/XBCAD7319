package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xbcad7311.databinding.FragmentMessagesBinding
import com.example.xbcad7319.data.Message

class FragmentMessage : Fragment() {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEdit.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(id = "unique_id", senderId = "Client", content = messageText, timestamp = System.currentTimeMillis())
                messageList.add(newMessage)
                messageAdapter.notifyItemInserted(messageList.size - 1)
                binding.messageEdit.text.clear()
                binding.recyclerViewMessages.scrollToPosition(messageList.size - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
