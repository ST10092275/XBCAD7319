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
    private var param1: String? = null
    private var param2: String? = null



    // Model class for a quote
    class Quote(private var description: String, private var image: Int) {
        fun getDescription(): String = description
        fun getImage(): Int = image
    }

    // Custom adapter class derived from BaseAdapter
    class CustomAdapter(
        private var context: Context,
        private var quotes: ArrayList<Quote>,
        private val fullName: String
    ) : BaseAdapter() {
        override fun getCount(): Int = quotes.size
        override fun getItem(position: Int): Any = quotes[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.rows, parent, false)
            val quote = getItem(position) as Quote

            val img = view.findViewById<ImageView>(R.id.imageView)
            val nameTxt = view.findViewById<TextView>(R.id.textView)

            nameTxt.text = quote.getDescription()
            img.setImageResource(quote.getImage())

            view.setOnClickListener {
                // Create a new instance of FragmentServiceRequest and pass the required arguments
                val fragment = FragmentServiceRequest.newInstance(fullName, quote.getDescription())

                // Get the FragmentManager from the context and begin a transaction
                (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragment_container, fragment)
                    addToBackStack(null)
                    commit()
                }

                // Save to Firestore
                saveQuoteToFirestore(fullName, quote.getDescription())
            }
            return view
        }

        private fun saveQuoteToFirestore(userName: String, description: String) {
            val firestore = FirebaseFirestore.getInstance()
            val serviceData = hashMapOf(
                "userName" to userName,
                "description" to description
            )

            firestore.collection("service_request") // Replace "services" with your desired collection name
                .add(serviceData)
                .addOnSuccessListener {
                    // Handle success, e.g., show a Toast
                    Toast.makeText(context, "Service saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    Toast.makeText(context, "Error saving service: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private lateinit var adapter: CustomAdapter
    private lateinit var gv: GridView

    private val data: ArrayList<Quote>
        get() {
            val quotes = ArrayList<Quote>()
            quotes.add(Quote("ICT support services", R.drawable.helpdesk))
            quotes.add(Quote("WIFI", R.drawable.wifi))
            quotes.add(Quote("Telephones", R.drawable.telephone))
            quotes.add(Quote("Cartridges and Stationery", R.drawable.stationery))
            return quotes
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_service, container, false)
        val fullName = arguments?.getString(ARG_FULL_NAME) ?: "User"

        // Initialize the GridView and set the adapter
        gv = view.findViewById(R.id.myGridView)
        adapter = CustomAdapter(requireContext(), data, fullName)
        gv.adapter = adapter

        return view
    }

    companion object {
        private const val ARG_FULL_NAME = "full_name"

        @JvmStatic
        fun newInstance(param1: String, param2: String, fullName: String) =
            FragmentService().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_FULL_NAME, fullName)
                }
            }
    }
}
