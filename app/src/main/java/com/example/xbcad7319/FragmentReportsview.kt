package com.example.xbcad7319

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.xbcad7311.R

    class FragmentReportsview(private val title: String, private val details: String) : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_reportsview, container, false)
            val titleTextView: TextView = view.findViewById(R.id.contentTextView1)
            val detailsTextView: TextView = view.findViewById(R.id.contentTextView2)

            titleTextView.text = title
            detailsTextView.text = details

            return view
        }
    }