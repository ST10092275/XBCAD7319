package com.example.xbcad7319

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.xbcad7311.R
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentPricelist.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentPricelist : Fragment() {

    private var paymentTV: TextView? = null

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

        // Set onClick listeners for each ImageButton
        btnItem1.setOnClickListener { startPayment("27.35") }
        btnItem2.setOnClickListener { startPayment("273.55") }
        btnItem3.setOnClickListener { startPayment("43.77") }
        btnItem4.setOnClickListener { startPayment("273.55") }

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

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Getting the payment confirmation
                val confirm = data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)

                if (confirm != null) {
                    try {
                        // Getting the payment details
                        val paymentDetails = confirm.toJSONObject().toString(4)
                        val payObj = JSONObject(paymentDetails)
                        val payID = payObj.getJSONObject("response").getString("id")
                        val state = payObj.getJSONObject("response").getString("state")
                        paymentTV?.text = "Payment $state\n with payment ID: $payID"
                    } catch (e: JSONException) {
                        Log.e("Error", "an unlikely failure occurred: ", e)
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted.")
            }
        }
    }

    companion object {
        const val clientKey: String = "AVKqnDlb_9KFaK-8LvM28tp06SE69U_nh3oSmHbEShqx65YJm5FQ1F3vfIHpwz1PGN1dyV0saLa4Ar65"
        const val PAYPAL_REQUEST_CODE: Int = 123

        // PayPal Configuration Object
        private val config: PayPalConfiguration = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientKey)
    }
}
