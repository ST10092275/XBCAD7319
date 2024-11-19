package com.example.xbcad7319

import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.xbcad7311.R
import com.google.firebase.auth.FirebaseAuth



class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var toggleButton: ToggleButton
    private lateinit var mainLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        toggleButton = findViewById(R.id.toggleButton)
        mainLayout = findViewById(R.id.login)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.btnLogin)

        loginButton.setOnClickListener {
            loginUser()
        }

        val registration = findViewById<TextView>(R.id.register)
        // Set OnClickListener for the "Register" TextView
        registration.setOnClickListener {
            // Navigate to RegisterActivity
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        toggleButton.isChecked = false
        // ToggleButton logic to switch between layouts
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            Log.d("ToggleButton", "Checked: $isChecked")
            if (isChecked) {
                Log.d("ToggleButton", "Navigating to AdminLogin")
                val intent = Intent(this@Login, AdminLogin::class.java)
                startActivity(intent)
                finish()
            }
        }

        val forgotPasswordTextView: TextView = findViewById(R.id.forgotpassword)
        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val emailInput = email.text.toString().trim()
        val passwordInput = password.text.toString().trim()

        if (emailInput.isEmpty() || passwordInput.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Sign in with email and password
        auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, now prompt for biometric authentication
                    promptBiometricAuthentication()
                } else {
                    // Login failed
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun promptBiometricAuthentication() {
        val biometricManager = BiometricManager.from(this)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            val executor = ContextCompat.getMainExecutor(this)
            val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Navigate to MainActivity after successful authentication
                    startActivity(Intent(this@Login, MainActivity::class.java))
                    finish()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("ConsultEase")
                .setSubtitle("Login using your fingerprint")
                .setNegativeButtonText("Use account password")
                .build()

            biometricPrompt.authenticate(promptInfo)
        } else {
            Toast.makeText(this, "Biometric authentication is not available.", Toast.LENGTH_LONG).show()
        }
    }
}
