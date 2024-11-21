package com.example.xbcad7319


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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


class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var fullname: EditText
    private lateinit var cpassword: EditText
    private lateinit var number: EditText
    private lateinit var toggleButton: ToggleButton
    private lateinit var mainLayout: FrameLayout
    private lateinit var btnRegister: Button

    private val registeredEmails = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        FirebaseApp.initializeApp(this)
        Log.d("FirebaseInit", "Firebase has been initialized")

        auth = FirebaseAuth.getInstance()
        toggleButton = findViewById(R.id.toggleButton)
        mainLayout = findViewById(R.id.mainLayout)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        number = findViewById(R.id.number)
        cpassword = findViewById(R.id.cpassword)
        fullname = findViewById(R.id.fullname)
        btnRegister = findViewById(R.id.btnRegister)
        val registration = findViewById<TextView>(R.id.rLogin)

        btnRegister.setOnClickListener {
            registerUser()
        }

        // Set OnClickListener for the "Register" TextView
        registration.setOnClickListener {
            // Navigate to Login Activity
            val intent = Intent(this@Register, Login::class.java)
            startActivity(intent)
        }

        // Set up the toggle button to switch between layouts
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Navigate to AdminRegister Activity
                val intent = Intent(this@Register, AdminRegister::class.java)
                startActivity(intent)
                finish() // Optional: Call finish() if you don't want to return to this activity
            }
        }
    }
    private fun registerUser() {
        val emailInput = email.text.toString().trim()
        val passwordInput = password.text.toString().trim()
        val confirmPasswordInput = cpassword.text.toString().trim()
        val fullNameInput = fullname.text.toString().trim()
        val numberInput = number.text.toString().trim()

        // Validate the inputs
        if (emailInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty() ||
            fullNameInput.isEmpty() || numberInput.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        } else if (registeredEmails.contains(emailInput)) {
            Toast.makeText(this, "Email already used in this session", Toast.LENGTH_SHORT).show()
        } else if (passwordInput != confirmPasswordInput) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            // Proceed with Firebase registration
            auth.createUserWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid ?: ""

                        // Store user info in Firestore
                        storeUserInfo(userId, fullNameInput, numberInput)

                        // Save full name to SharedPreferences
                        val sharedPreferences: SharedPreferences = getSharedPreferences("User Prefs", MODE_PRIVATE)
                        sharedPreferences.edit().putString("FULL_NAME", fullNameInput).apply()

                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                        // Pass the full name to the Login activity
                        val intent = Intent(this, Login::class.java)
                        intent.putExtra("FULL_NAME", fullNameInput)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun storeUserInfo(userId: String, fullName: String, number: String) {
        val userData = hashMapOf(
            "fullname" to fullName,
            "number" to number,
            "role" to "client" // Set role as client
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).set(userData)
            .addOnSuccessListener {
                // User info successfully stored
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving user info: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

