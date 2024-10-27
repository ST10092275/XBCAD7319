package com.example.xbcad7319

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.databinding.ItemMessageBinding
import com.example.xbcad7319.data.Message

class MessagesAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageContent.text = message.content
            binding.senderName.text = message.senderId // Display sender
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size
}
