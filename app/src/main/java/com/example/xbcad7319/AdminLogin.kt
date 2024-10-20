package com.example.xbcad7311

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.xbcad7311.R
import com.example.xbcad7319.AdminMainActivity
import com.google.firebase.auth.FirebaseAuth

class AdminLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var toggleButton: ToggleButton
    private lateinit var mainLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        toggleButton = findViewById(R.id.toggleButton)
        mainLayout = findViewById(R.id.adminlogin)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.btnLogin)

        val registration = findViewById<TextView>(R.id.register)

        // Set OnClickListener for the "Register" TextView
        registration.setOnClickListener {
            // Navigate to AdminRegister Activity
            val intent = Intent(this@AdminLogin, AdminRegister::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for the login button
        loginButton.setOnClickListener {
            loginAdmin()
        }

        // ToggleButton logic to switch between layouts
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Navigate to User Login Activity (standard login)
                val intent = Intent(this@AdminLogin, Login::class.java)
                startActivity(intent)
                finish() // Optional: Call finish() if you don't want to return to this activity
            }
        }
    }

    private fun loginAdmin() {
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        // Basic input validation
        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Sign in using Firebase Authentication
        auth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Navigate to the main admin dashboard or home screen
                    val intent = Intent(this@AdminLogin, AdminMainActivity::class.java)
                    startActivity(intent)
                    finish() // Finish the login activity
                } else {
                    // If login fails, display a message to the user
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
