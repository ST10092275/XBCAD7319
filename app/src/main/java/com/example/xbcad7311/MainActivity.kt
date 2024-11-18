package com.example.xbcad7311

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.xbcad7311.R
import com.example.xbcad7319.FragmentMessage
import com.example.xbcad7319.FragmentPricelist
import com.example.xbcad7319.FragmentService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import javax.net.ssl.SSLContext
import java.security.NoSuchAlgorithmException
import com.google.firebase.FirebaseApp


class MainActivity : AppCompatActivity() {

    // Drawer layout for handling the side menu navigation
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize Firebase when the activity starts
        FirebaseApp.initializeApp(this)

        // Setting up the toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Assign the drawer layout
        drawerLayout = findViewById(R.id.drawer)

        // Setting up bottom navigation to switch between fragments
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.services -> loadFragment(FragmentService())
                R.id.pricelist -> loadFragment(FragmentPricelist())
                R.id.logout -> startLoginActivity() // Fixed this part
                R.id.services -> loadFragment(FragmentService())      // Load the Services fragment
                R.id.pricelist -> loadFragment(FragmentPricelist())    // Load the Pricelist fragment
                R.id.messages -> loadFragment(FragmentMessage())       // Load the Messages fragment
                R.id.logout -> startLoginActivity()                    // Trigger logout and navigate to login screen:app/src/main/java/com/example/xbcad7311/MainActivity.kt
            }
            true
        }

       // Set up TLS 1.2
        try {
            val context = SSLContext.getInstance("TLSv1.2")
            context.init(null, null, null)
            SSLContext.setDefault(context)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        // Load the default fragment when the activity starts


        // Load the default fragment (Services) when the activity starts for the first time

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentPricelist())
                .commit()
            loadFragment(FragmentService())
        }
    }

    // Replaces the current fragment in the FrameLayout with a new fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Override onBackPressed to close the navigation drawer if it's open, otherwise perform the usual back action
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Starts the Login activity and finishes the MainActivity (useful for logging out)
    private fun startLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Close the current activity to prevent returning on back press
    }
}
