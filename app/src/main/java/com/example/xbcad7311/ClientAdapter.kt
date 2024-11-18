// ClientAdapter.kt
package com.example.xbcad7319

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.databinding.ItemClientBinding
import com.example.xbcad7319.data.Client

class ClientAdapter(
    private val clientList: List<Client>,
    private val onClientClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val binding = ItemClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(clientList[position])
    }

    override fun getItemCount() = clientList.size

    inner class ClientViewHolder(private val binding: ItemClientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(client: Client) {
            binding.clientNameText.text = client.clientName
            itemView.setOnClickListener { onClientClick(client) }
        }
    }
}
