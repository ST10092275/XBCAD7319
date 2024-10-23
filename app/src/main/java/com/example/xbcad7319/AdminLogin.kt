package com.example.xbcad7311

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.xbcad7311.R
import com.example.xbcad7319.AdminMainActivity
import com.example.xbcad7319.ForgotPasswordActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var toggleButton: ToggleButton
    private lateinit var mainLayout: FrameLayout
    private lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        toggleButton = findViewById(R.id.toggleButton)
        mainLayout = findViewById(R.id.adminlogin)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.btnLogin)
        errorMessage = findViewById(R.id.errorMessage)

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
        val forgotPasswordTextView: TextView = findViewById(R.id.forgotpassword)
        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loginAdmin() {
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            errorMessage.text = "Please fill in all fields"
            errorMessage.visibility = View.VISIBLE
            return
        }

        auth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.getString("role") == "admin") {
                                errorMessage.visibility = View.GONE // Hide error message
                                val intent = Intent(this@AdminLogin, AdminMainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                auth.signOut()
                                errorMessage.visibility = View.VISIBLE
                                errorMessage.text = "Not an admin account" // Show error message

                            }
                        }
                } else {
                    errorMessage.text = "Login failed: ${task.exception?.message}"
                    errorMessage.visibility = View.VISIBLE
                }
            }
    }
}