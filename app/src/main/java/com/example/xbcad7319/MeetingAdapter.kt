package com.example.xbcad7319

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7311.R
import com.example.xbcad7319.data.model.Meeting

class MeetingAdapter(
    private var meetings: List<Meeting>,
    private val onDelete: (Long) -> Unit // Callback for deleting a meeting
) : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    // ViewHolder class to hold the views for each meeting item
    class MeetingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        val agendaTextView: TextView = view.findViewById(R.id.agendaTextView)
        val clientNameTextView: TextView = view.findViewById(R.id.clientNameTextView)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meeting_item, parent, false)
        return MeetingViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetings[position]
        holder.dateTextView.text = "Date: ${meeting.date}"
        holder.timeTextView.text = "Time: ${meeting.time}"
        holder.agendaTextView.text = "Agenda: ${meeting.agenda}"
        holder.clientNameTextView.text = "Client Name: ${meeting.clientName}"

        // Handle item click to edit the meeting (implement your edit logic here)
        holder.itemView.setOnClickListener {
            // Logic for editing the meeting can be implemented here
        }

        // Handle long click to delete the meeting
        holder.itemView.setOnLongClickListener {
            onDelete(meeting.id) // Trigger delete callback with the meeting ID
            true // Indicate that the long click was handled
        }
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int = meetings.size

    // Update the list of meetings and notify the adapter of the changes
    fun updateMeetings(newMeetings: List<Meeting>) {
        meetings = newMeetings
        notifyDataSetChanged() // Notify the RecyclerView to refresh the displayed data
    }
}