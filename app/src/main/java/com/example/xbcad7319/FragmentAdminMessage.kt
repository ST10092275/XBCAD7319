package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xbcad7311.databinding.FragmentAdminMessageBinding
import com.example.xbcad7319.data.Message

class FragmentAdminMessages : Fragment() {
    private var _binding: FragmentAdminMessageBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageList = mutableListOf()
        messageAdapter = MessagesAdapter(messageList)

        binding.recyclerViewAdminMessages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messageAdapter
        }

        binding.adminSendButton.setOnClickListener {
            val messageText = binding.adminMessageEdit.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(id = "unique_id", senderId = "Admin", content = messageText, timestamp = System.currentTimeMillis())
                messageList.add(newMessage)
                messageAdapter.notifyItemInserted(messageList.size - 1)
                binding.adminMessageEdit.text.clear()
                binding.recyclerViewAdminMessages.scrollToPosition(messageList.size - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
