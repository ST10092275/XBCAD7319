package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xbcad7311.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
//Android Developers. (2024). Fragments. Available at: https://developer.android.com/guide/fragments [Accessed 1 Nov. 2024].

//Android Developers. (2024). RecyclerView. Available at: https://developer.android.com/guide/topics/ui/layout/recyclerview [Accessed 1 Nov. 2024].
class FragmentReports : Fragment() {
    private lateinit var recyclerView: RecyclerView // RecyclerView to display reports
    private lateinit var dbHelper: DatabaseHelper // Database helper for SQLite operations
    private lateinit var reportsAdapter: ReportsAdapter // Adapter for the RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports, container, false) // Inflate the layout
        recyclerView = view.findViewById(R.id.recyclerView) // Initialize RecyclerView
        val fab: FloatingActionButton = view.findViewById(R.id.fab_action2) // Initialize FloatingActionButton

        dbHelper = DatabaseHelper(requireContext()) // Initialize the database helper
        setupRecyclerView() // Set up the RecyclerView

// Set click listener for the FloatingActionButton
        fab.setOnClickListener {
            // Navigate to FragmentEnterR
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentEnterR()) // Replace with FragmentEnterR
                .addToBackStack(null) // Add to back stack
                .commit() // Commit the transaction
        }

        return view // Return the inflated view
    }

    // Method to set up the RecyclerView with reports
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context) // Set layout manager
        val reports = dbHelper.getReports() // Fetch reports from the database
        reportsAdapter = ReportsAdapter(reports) { title, details ->
            // Navigate to FragmentReportsView with the selected report's title and details
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentReportsview(title, details)) // Replace with FragmentReportsView
                .addToBackStack(null) // Add to back stack
                .commit() // Commit the transaction
        }
        recyclerView.adapter = reportsAdapter // Set the adapter for the RecyclerView
    }
}
