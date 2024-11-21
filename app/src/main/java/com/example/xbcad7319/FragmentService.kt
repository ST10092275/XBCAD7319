package com.example.xbcad7319


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xbcad7311.R
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class FragmentService : Fragment() {

    // Model class for a quote, holding description and image
    class Quote(private var description: String, private var image: Int) {
        fun getDescription(): String = description  // Getter for description
        fun getImage(): Int = image  // Getter for image resource
    }

    // Custom adapter class for displaying quotes in a GridView
    class CustomAdapter(
        private var context: Context,  // Context to access system resources
        private var quotes: ArrayList<Quote>,  // List of quotes to display
        private val fullName: String  // Full name of the user (used for saving data)
    ) : BaseAdapter() {

        // Returns the total number of items in the list
        override fun getCount(): Int = quotes.size

        // Returns the quote object at a particular position
        override fun getItem(position: Int): Any = quotes[position]

        // Returns the item ID (position as long)
        override fun getItemId(position: Int): Long = position.toLong()

        // Provides the view for each item in the GridView
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            // Inflate the layout for each item in the GridView (or reuse if available)
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.rows, parent, false)

            // Get the quote object at the current position
            val quote = getItem(position) as Quote

            // Find the image view and text view in the item layout
            val img = view.findViewById<ImageView>(R.id.imageView)
            val nameTxt = view.findViewById<TextView>(R.id.textView)

            // Set the text and image for the current quote
            nameTxt.text = quote.getDescription()
            img.setImageResource(quote.getImage())

            // Set a click listener for the item
            view.setOnClickListener {
                // Create a new instance of FragmentServiceRequest and pass the required arguments
                val fragment = FragmentServiceRequest.newInstance(fullName, quote.getDescription())

                // Begin a fragment transaction to replace the current fragment with the new one
                (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragment_container, fragment)  // Replace the fragment container with the new fragment
                    addToBackStack(null)  // Add the transaction to the back stack
                    commit()  // Commit the transaction
                }

                // Save the selected service to Firestore
                saveQuoteToFirestore(fullName, quote.getDescription())
            }

            // Return the view for the item
            return view
        }

        // Function to save the selected quote to Firestore
        private fun saveQuoteToFirestore(userName: String, description: String) {
            val firestore = FirebaseFirestore.getInstance()

            // Create a map of the service data to save
            val serviceData = hashMapOf(
                "userName" to userName,  // Store the user's name
                "description" to description  // Store the service description
            )

            // Add the service data to the Firestore collection
            firestore.collection("service_request")  // Replace with your desired collection name
                .add(serviceData)  // Add a new document to the collection
                .addOnSuccessListener {
                    // Handle success - show a Toast to inform the user
                    Toast.makeText(context, "Service saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle failure - show a Toast with the error message
                    Toast.makeText(context, "Error saving service: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Initialize the adapter and GridView
    private lateinit var adapter: CustomAdapter
    private lateinit var gv: GridView

    // Provide the sample data for the quotes (services)
    private val data: ArrayList<Quote>
        get() {
            val quotes = ArrayList<Quote>()
            quotes.add(Quote("ICT support services", R.drawable.helpdesk))
            quotes.add(Quote("WIFI", R.drawable.wifi))
            quotes.add(Quote("Telephones", R.drawable.telephone))
            quotes.add(Quote("Cartridges and Stationery", R.drawable.stationery))
            return quotes
        }

    // onCreateView function to set up the fragment layout and view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_service, container, false)

        // Get the full name passed as an argument to the fragment
        val fullName = arguments?.getString(ARG_FULL_NAME) ?: "User"

        // Initialize the GridView and set the custom adapter
        gv = view.findViewById(R.id.myGridView)
        adapter = CustomAdapter(requireContext(), data, fullName)
        gv.adapter = adapter  // Set the adapter for the GridView

        return view
    }

    companion object {
        private const val ARG_FULL_NAME = "full_name"

        // Static function to create a new instance of the fragment with arguments
        @JvmStatic
        fun newInstance(param1: String, param2: String, fullName: String) =
            FragmentService().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)  // Pass the first parameter
                    putString(ARG_PARAM2, param2)  // Pass the second parameter
                    putString(ARG_FULL_NAME, fullName)  // Pass the user's full name
                }
            }
    }

}


