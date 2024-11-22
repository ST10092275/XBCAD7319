package com.example.xbcad7319

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.R
import com.example.xbcad7311.databinding.ItemMessageBinding

class MessageAdapter(
    private var messages: MutableList<Message>,
    private val currentUserId: String // Pass currentUserId to the adapter
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    fun updateMessages(newMessages: List<Message>) {
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMessageBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        val isSentByAdmin = message.senderId == currentUserId // Compare sender ID
        holder.bind(message, isSentByAdmin)
    }

    override fun getItemCount() = messages.size

    class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message, isSentByAdmin: Boolean) {
            binding.messageText.text = message.text
            binding.timestamp.text = message.timestamp.toDate().toString()

            // Set different background and alignment based on sender
            val layoutParams = binding.messageText.layoutParams as FrameLayout.LayoutParams

            if (isSentByAdmin) {
                // Admin message styling
                binding.messageText.setBackgroundResource(R.drawable.background_admin_message)
                layoutParams.gravity = Gravity.END
            } else {
                // Client message styling
                binding.messageText.setBackgroundResource(R.drawable.background_client_message)
                layoutParams.gravity = Gravity.START
            }

            binding.messageText.layoutParams = layoutParams
        }
    }
}
