package com.example.xbcad7319

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.R
import com.example.xbcad7319.data.PriceList

class PriceListAdapter(private val priceList: List<PriceList>) :
    RecyclerView.Adapter<PriceListAdapter.PriceViewHolder>() {

    class PriceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.service_name)
        val price: TextView = itemView.findViewById(R.id.service_price)
        val description: TextView = itemView.findViewById(R.id.service_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_price_list, parent, false)
        return PriceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        val currentItem = priceList[position]
        holder.serviceName.text = currentItem.serviceName
        holder.price.text = "R${currentItem.price}/pm"
        holder.description.text = currentItem.description
    }

    override fun getItemCount() = priceList.size
}
