package com.example.xbcad7319

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.xbcad7311.R
import com.example.xbcad7319.data.model.Client

class ClientAdapter(context: Context, clients: List<Client>) : ArrayAdapter<Client>(context, 0, clients) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val client = getItem(position) // Get the client at the current position
        // Inflate the layout for the list item if convertView is null
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_view, parent, false)

        // Find the TextView and ImageView in the list item layout
        val fullNameTextView: TextView = listItemView.findViewById(R.id.client_full_name)
        val clientImageView: ImageView = listItemView.findViewById(R.id.client_image)

        // Set the client's full name in the TextView
        fullNameTextView.text = client?.fullname
        // Set a placeholder image for the client
        clientImageView.setImageResource(R.drawable.baseline_more_horiz_24)

        return listItemView // Return the completed view to render on screen
    }
}
//Android Developers. (2024). RecyclerView. Available at: https://developer.android.com/guide/topics/ui/layout/recyclerview [Accessed 21 Oct. 2024].
