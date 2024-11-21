package com.example.xbcad7319


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.xbcad7311.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.net.ssl.SSLContext
import java.security.NoSuchAlgorithmException

//Code adapted from Android Developers
//Add menus (2024)
//https://developer.android.com/develop/ui/views/components/menus?authuser=1
class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.services -> loadFragment(FragmentService())
                R.id.pricelist -> loadFragment(FragmentPricelist())
                R.id.messages -> loadFragment(FragmentMessages())
                R.id.logout -> startLoginActivity() // Fixed this part
            }
            true
        }
       // Set up TLS 1.2
        try {
            val context = SSLContext.getInstance("TLSv1.2")
            context.init(null, null, null)
            SSLContext.setDefault(context)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("MainActivity", "TLS setup failed", e)
        }
        // Load the default fragment when the activity starts
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentPricelist())
                .commit()
            loadFragment(FragmentService())
        }
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

    // Starting the Login activity
    private fun startLoginActivity() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish() // Optionally finish the current activity
    }

}

