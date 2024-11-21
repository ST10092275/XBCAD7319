package com.example.xbcad7319


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.R
import com.example.xbcad7319.data.model.Report

class ReportsAdapter(
    private val reportList: List<Report>, // List of Report objects
    private val itemClick: (String, String) -> Unit // Lambda function for item click
) : RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList[position]
        holder.titleTextView.text = report.title

        // Add click listener to the item
        holder.itemView.setOnClickListener {
            itemClick(report.title, report.details) // Accessing details property
        }
    }

    override fun getItemCount(): Int {
        return reportList.size
    }
}