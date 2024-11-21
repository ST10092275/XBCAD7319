package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xbcad7311.R
import android.app.AlertDialog
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.xbcad7319.data.model.Meeting

class FragmentHome : Fragment() {
    private lateinit var databaseHelper: DBHelper // Database helper for SQLite operations
    private lateinit var recyclerView: RecyclerView // RecyclerView to display meetings
    private lateinit var meetingAdapter: MeetingAdapter // Adapter for the RecyclerView
    private var currentSelectedDate: String = "" // Variable to store the selected date
    private var currentFullName: String? = null // To store the fullname

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        databaseHelper = DBHelper(requireContext()) // Initialize DBHelper

        recyclerView = view.findViewById(R.id.recyclerView) // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Set layout manager

        // Retrieve fullname from arguments
        currentFullName = arguments?.getString(ARG_FULLNAME)

        // Get all meetings from the database and set up the adapter
        val meetings = databaseHelper.getAllMeetings()
        meetingAdapter = MeetingAdapter(meetings) { id -> deleteMeeting(id) }
        recyclerView.adapter = meetingAdapter

        // Set up CalendarView to listen for date changes
        val calendarView: CalendarView = view.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            currentSelectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            showAddMeetingDialog() // Show dialog to add a meeting
        }

        return view // Return the inflated view
    }

    // Method to show a dialog for adding a new meeting
    private fun showAddMeetingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.popup_layout, null)
        val dateEditText: EditText = dialogView.findViewById(R.id.dateEditText)
        val timeEditText: EditText = dialogView.findViewById(R.id.timeEditText)
        val agendaEditText: EditText = dialogView.findViewById(R.id.agendaEditText)

        dateEditText.setText(currentSelectedDate) // Set the selected date in the dialog

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Add Meeting") { _, _ ->
                val time = timeEditText.text.toString() // Get time from input
                val agenda = agendaEditText.text.toString() // Get agenda from input

                // Create a meeting associated with the correct fullname
                val meeting = Meeting(0, currentSelectedDate, time, agenda, currentFullName!!, currentFullName!!)
                databaseHelper.addMeeting(meeting) // Store meeting in the database
                updateRecyclerView() // Refresh RecyclerView
            }
            .setNegativeButton("Cancel", null)
            .show() // Show the dialog
    }

    // Method to delete a meeting by ID
    private fun deleteMeeting(id: Long) {
        databaseHelper.deleteMeeting(id) // Delete the meeting from the database
        updateRecyclerView() // Refresh RecyclerView
        Toast.makeText(requireContext(), "Meeting deleted", Toast.LENGTH_SHORT).show() // Show toast message
    }

    // Method to update the RecyclerView with the latest meetings
    private fun updateRecyclerView() {
        val meetings = databaseHelper.getAllMeetings() // Retrieve all meetings
        meetingAdapter.updateMeetings(meetings) // Update the adapter with new meetings
    }

    // Method to get meetings by client name
    fun getMeetingsByClient(clientName: String?): List<Meeting> {
        return databaseHelper.getAllMeetings().filter { it.clientName == clientName } // Filter meetings by client name
    }

    companion object {
        private const val ARG_FULLNAME = "fullname" // Argument key for fullname

        // Factory method to create a new instance of FragmentHome
        @JvmStatic
        fun newInstance(fullname: String) =
            FragmentHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_FULLNAME, fullname) // Put fullname into arguments
                }
            }
    }
}

//Android Developers. (2024). Fragments. Available at: https://developer.android.com/guide/fragments [Accessed 21 Oct. 2024].
//Android Developers. (2024). RecyclerView. Available at: https://developer.android.com/guide/topics/ui/layout/recyclerview [Accessed 21 Oct. 2024].