package com.example.xbcad7319

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.xbcad7311.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

//Code adapted from Android Developers
//Add menus (2024)
//https://developer.android.com/develop/ui/views/components/menus?authuser=1
class AdminMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Set up NavigationView (sidebar)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Set up the drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> loadFragment(FragmentHome())
                R.id.bottom_person-> loadFragment(FragmentPerson())
                R.id.bottom_reports -> loadFragment(FragmentReports())
                R.id.bottom_message -> loadFragment(FragmentAdminMessage())

            }
            true
        }

        // Load the default fragment when the activity starts
        if (savedInstanceState == null) {
            loadFragment(FragmentHome())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_service -> loadFragment(FragmentAdminService())
            R.id.bottom_logout-> {
                // Clear the user session if applicable, then navigate to Login
                val intent = Intent(this, AdminLogin::class.java)
                startActivity(intent)
                finish() // Optional: Call finish() if you want to close the current activity
            }
        }
        // Close the drawer after selection
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Load the selected fragment into the FrameLayout
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Handle back press to close the drawer if it's open
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
