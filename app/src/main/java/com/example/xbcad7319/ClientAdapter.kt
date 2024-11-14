package com.example.xbcad7319

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.xbcad7311.R
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.Api.Client

class ClientAdapter(
    private val clients: List<Client>,
    private val onClientClick: (Client) -> Unit
) : RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_client, parent, false)
        return ClientViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(clients[position], onClientClick)
    }

    override fun getItemCount() = clients.size

    class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(client: Client, onClientClick: (Client) -> Unit) {
            itemView.findViewById<TextView>(R.id.clientNameTextView).text = client.name
            itemView.setOnClickListener { onClientClick(client) }
        }
    }
}
