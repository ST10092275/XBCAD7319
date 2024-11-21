package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.xbcad7311.R



class EnterR : Fragment() {

    private lateinit var titleEditText: EditText // EditText for report title
    private lateinit var detailsEditText: EditText // EditText for report details
    private lateinit var dbHelper: DatabaseHelper // DatabaseHelper instance to manage database operations

    // Called to create the view hierarchy associated with the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_enter_r, container, false)

        // Initialize the EditText fields
        titleEditText = view.findViewById(R.id.editTextText_titel)
        detailsEditText = view.findViewById(R.id.editTextText2)

        // Initialize the submit button
        val submitButton: Button = view.findViewById(R.id.submit_button)

        // Initialize the DatabaseHelper
        dbHelper = DatabaseHelper(requireContext())

        // Set an OnClickListener on the submit button
        submitButton.setOnClickListener {
            // Get the text from the EditText fields
            val title = titleEditText.text.toString()
            val details = detailsEditText.text.toString()

            // Insert the report into the database
            dbHelper.insertReport(title, details)

            // Navigate back to FragmentReports
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentReports()) // Replace with FragmentReports
                .addToBackStack(null) // Add the transaction to the back stack
                .commit() // Commit the transaction
        }

        return view // Return the view for this fragment
    }
}