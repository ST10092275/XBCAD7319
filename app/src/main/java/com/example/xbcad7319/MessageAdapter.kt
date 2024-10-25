package com.example.xbcad7319

// MessageAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.R
import com.example.xbcad7311.databinding.ItemChatMessageBinding
import com.example.xbcad7319.data.model.Message

class MessageAdapter(
    private val messages: List<Message>,
    private val currentUserId: String // The ID of the current user for message alignment
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textViewMessage.text = message.content
            // Align messages based on the senderId
            if (message.senderId == currentUserId) {
                // Logic for sent messages
                binding.textViewMessage.setBackgroundResource(R.drawable.bg_message_sent) // Sent background
            } else {
                // Logic for received messages
                binding.textViewMessage.setBackgroundResource(R.drawable.bg_message_received) // Received background
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size
}
