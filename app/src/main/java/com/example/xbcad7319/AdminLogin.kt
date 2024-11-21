package com.example.xbcad7319


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
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

//Code adapted from Android Developers
//Biometric(2024)
//https://developer.android.com/jetpack/androidx/releases/biometric

//Code Adpted from GeeksforGeeks
// Login and Register in Android using Firebase in Kotlin (2022) by ayus
//https://www.geeksforgeeks.org/login-and-registration-in-android-using-firebase-in-kotlin/
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

        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            promptBiometricAuthentication()
        } else {
            Toast.makeText(this, "Biometric authentication is not available.", Toast.LENGTH_LONG)
                .show()
        }

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
//method to login admin
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
                                startActivity(Intent(this@AdminLogin, AdminMainActivity::class.java))
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
//prompst biometric authentication
    private fun promptBiometricAuthentication() {
    val biometricManager = BiometricManager.from(this)
    if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(
                        Intent(
                            this@AdminLogin,
                            AdminMainActivity::class.java
                        )
                    ) //when fingerprint scan is successful, navigate to admin main activity
                    finish()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for Goal Ignite")
            .setSubtitle("Log in using your fingerprint")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
}

