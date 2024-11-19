package com.example.xbcad7319

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xbcad7311.R

// In your PaymentSuccessActivity
class PaymentSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        // You can pass payment details through Intent extras if needed
        val paymentInfo = intent.getStringExtra("payment_info")
        // Display payment info or success message
    }
}
