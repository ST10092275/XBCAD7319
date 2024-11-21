package com.example.xbcad7319


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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminRegister : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var fullname: EditText
    private lateinit var cpassword: EditText
    private lateinit var number: EditText
    private lateinit var toggleButton: ToggleButton
    private lateinit var mainLayout: FrameLayout
    private lateinit var register: Button

    private val predefinedPin = "91273"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_register)
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        toggleButton = findViewById(R.id.toggleButton)
        mainLayout = findViewById(R.id.main)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        number = findViewById(R.id.number)
        cpassword = findViewById(R.id.cpassword)
        fullname = findViewById(R.id.fullname)
        register = findViewById(R.id.btnRegister)
        val registration = findViewById<TextView>(R.id.rLogin)

        // Set OnClickListener for the "Register" button
        register.setOnClickListener {
            registerAdmin()
        }

        // Set OnClickListener for the "Register" TextView
        registration.setOnClickListener {
            // Navigate to AdminLogin Activity
            val intent = Intent(this@AdminRegister, AdminLogin::class.java)
            startActivity(intent)
        }

        // Toggle button to switch between layouts
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Navigate to User Registration Activity (for regular users)
                val intent = Intent(this@AdminRegister, Register::class.java)
                startActivity(intent)
                finish() // Optional: Call finish() if you don't want to return to this activity
            }
        }
    }

    private fun registerAdmin() {
        val emailText = email.text.toString().trim()
        val passwordText = password.text.toString().trim()
        val confirmPassword = cpassword.text.toString().trim()
        val enteredPin = number.text.toString().trim() // Get entered PIN

        // Check if any of the fields are empty
        if (emailText.isEmpty() || passwordText.isEmpty() || confirmPassword.isEmpty() || enteredPin.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if passwords match
        if (passwordText != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if PIN is correct
        if (enteredPin != predefinedPin) {
            Toast.makeText(this, "Invalid PIN. Registration failed.", Toast.LENGTH_SHORT).show()
            return
        }

        // Proceed with Firebase Authentication registration
        auth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid ?: ""

                    // Store the admin's additional info in Firestore
                    val userData = hashMapOf(
                        "fullname" to fullname.text.toString().trim(),
                        "number" to number.text.toString().trim(), // Corrected this line
                        "role" to "admin" // Set role as admin
                    )

                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(userId).set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Admin registered successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error saving user info: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

