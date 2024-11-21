package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.xbcad7311.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentAdminService : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_service, container, false)

        // When Recent Requests button is clicked, show Pending requests
        val recentRequestsButton: Button = view.findViewById(R.id.recentRequestsButton)
        recentRequestsButton.setOnClickListener {
            val fragment = AdminViewRequests.newInstance("Pending", "SomeOtherValue")
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // When Requests History button is clicked, show Accepted/Denied requests
        val requestsHistoryButton: Button = view.findViewById(R.id.requestsHistoryButton)
        requestsHistoryButton.setOnClickListener {
            val fragment = AdminViewRequests.newInstance("History", "SomeValueForParam2")
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

}

