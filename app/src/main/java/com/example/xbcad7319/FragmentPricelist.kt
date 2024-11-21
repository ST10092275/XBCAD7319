package com.example.xbcad7319

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.xbcad7311.R
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

// Code adapted from GeeksForGeeks
//How to Integrate PayPal SDK in Android (2021) by chait
//https://www.geeksforgeeks.org/how-to-integrate-paypal-sdk-in-android/
class FragmentPricelist : Fragment() {

    private var paymentTV: TextView? = null

    // Define constants for prices
    companion object {
        const val ITEM1_PRICE = "27.35"
        const val ITEM2_PRICE = "273.55"
        const val ITEM3_PRICE = "43.77"
        const val ITEM4_PRICE = "273.55"

        const val CLIENT_KEY =
            "AVKqnDlb_9KFaK-8LvM28tp06SE69U_nh3oSmHbEShqx65YJm5FQ1F3vfIHpwz1PGN1dyV0saLa4Ar65"

        // Define constant for PayPal request code to avoid magic number
        const val PAYPAL_REQUEST_CODE = 123

        // PayPal Configuration Object
        private val config: PayPalConfiguration = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(CLIENT_KEY)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val rootView = inflater.inflate(R.layout.fragment_pricelist, container, false)

        // Initialize the views
        val btnItem1 = rootView.findViewById<ImageButton>(R.id.btnItem1)
        val btnItem2 = rootView.findViewById<ImageButton>(R.id.btnItem2)
        val btnItem3 = rootView.findViewById<ImageButton>(R.id.btnItem3)
        val btnItem4 = rootView.findViewById<ImageButton>(R.id.btnItem4)

        // Set onClick listeners using constants
        btnItem1.setOnClickListener { startPayment(ITEM1_PRICE) }
        btnItem2.setOnClickListener { startPayment(ITEM2_PRICE) }
        btnItem3.setOnClickListener { startPayment(ITEM3_PRICE) }
        btnItem4.setOnClickListener { startPayment(ITEM4_PRICE) }

        return rootView
    }

    private fun startPayment(amount: String) {
        // Create the PayPal payment object
        val payment = PayPalPayment(
            BigDecimal(amount), "USD", "Course Fees",
            PayPalPayment.PAYMENT_INTENT_SALE
        )

        // Create PayPal Payment activity intent
        val intent = Intent(activity, PaymentActivity::class.java)

        // Putting the PayPal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        // Putting PayPal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)

        // Starting the intent activity for result
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != PAYPAL_REQUEST_CODE) return // Early exit if the request code is not matching

        when (resultCode) {
            Activity.RESULT_OK -> handlePaymentSuccess(data)
            Activity.RESULT_CANCELED -> Log.i("paymentExample", "The user canceled.")
            PaymentActivity.RESULT_EXTRAS_INVALID -> Log.i(
                "paymentExample",
                "An invalid Payment or PayPalConfiguration was submitted."
            )

            else -> Log.i("paymentExample", "Unhandled result code: $resultCode")
        }
    }

    private fun handlePaymentSuccess(data: Intent?) {
        val confirm =
            data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)

        if (confirm != null) {
            try {
                val paymentDetails = confirm.toJSONObject().toString(4)
                val payObj = JSONObject(paymentDetails)
                val payID = payObj.getJSONObject("response").getString("id")
                val state = payObj.getJSONObject("response").getString("state")
                paymentTV?.text = "Payment $state\n with payment ID: $payID"
            } catch (e: JSONException) {
                Log.e("Error", "an unlikely failure occurred: ", e)
            }
        }
    }

}
