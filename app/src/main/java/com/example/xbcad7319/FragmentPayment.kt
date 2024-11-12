package com.example.xbcad7319

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.xbcad7311.R
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import org.json.JSONArray
import org.json.JSONObject


class FragmentPayment : Fragment() {
    companion object {
        private const val LOAD_PAYMENT_DATA_REQUEST_CODE = 991
    }
    private lateinit var paymentsClient: PaymentsClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeGooglePayClient()
        checkGooglePayAvailability()
    }

    private fun initializeGooglePayClient() {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // Use ENVIRONMENT_PRODUCTION for live
            .build()
        paymentsClient = Wallet.getPaymentsClient(requireActivity(), walletOptions)
    }

    private fun createPaymentRequest(): JSONObject {
        return JSONObject().apply {
            put("apiVersion", 2)
            put("apiVersionMinor", 0)
            put("allowedPaymentMethods", JSONArray().apply {
                // Stripe Payment Gateway
                put(JSONObject().apply {
                    put("type", "CARD")
                    put("parameters", JSONObject().apply {
                        put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
                        put("allowedCardNetworks", JSONArray(listOf("AMEX", "VISA", "MASTERCARD", "DISCOVER")))
                    })
                    put("tokenizationSpecification", JSONObject().apply {
                        put("type", "PAYMENT_GATEWAY")
                        put("parameters", JSONObject().apply {
                            put("gateway", "stripe") // Stripe as the gateway
                            put("gatewayMerchantId", "acct_1QIYyKHGNnOLE6LR")
                        })
                    })
                })

                // PayPal Payment Gateway
                put(JSONObject().apply {
                    put("type", "CARD")
                    put("parameters", JSONObject().apply {
                        put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
                        put("allowedCardNetworks", JSONArray(listOf("AMEX", "VISA", "MASTERCARD", "DISCOVER")))
                    })
                    put("tokenizationSpecification", JSONObject().apply {
                        put("type", "PAYMENT_GATEWAY")
                        put("parameters", JSONObject().apply {
                            put("gateway", "payfast") // PayPal as the gateway
                            put("gatewayMerchantId", "25931299")
                        })
                    })
                })
            })
            put("transactionInfo", JSONObject().apply {
                put("totalPriceStatus", "FINAL")
                put("totalPrice", "10.00") // Total amount of the transaction
                put("currencyCode", "ZAR")
            })
            put("merchantInfo", JSONObject().apply {
                put("merchantName", "ConsultEase")
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.let {
                            val paymentData = PaymentData.getFromIntent(it)
                            val paymentInfo = paymentData?.toJson()
                            Log.i("PaymentInfo", paymentInfo ?: "No payment data received.")
                            // Handle payment confirmation here (e.g., send payment data to your server)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.i("Payment", "User canceled the payment.")
                    }
                    AutoResolveHelper.RESULT_ERROR -> {
                        val status = AutoResolveHelper.getStatusFromIntent(data)
                        Log.e("PaymentError", "Error code: ${status?.statusCode}")
                    }
                }
            }
        }
    }

    private fun checkGooglePayAvailability() {
        val isReadyToPayRequest = IsReadyToPayRequest.fromJson(createPaymentRequest().toString())
        paymentsClient.isReadyToPay(isReadyToPayRequest).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Google Pay is available
                Log.i("GooglePay", "Google Pay is available")
                setupGooglePayButton()
            } else {
                // Google Pay is not available
                Log.e("GooglePay", "Google Pay is not available on this device.")
            }
        }
    }

    private fun setupGooglePayButton() {
        view?.findViewById<Button>(R.id.googlePayButton)?.setOnClickListener {
            Log.i("GooglePay", "Button clicked")
            requestGooglePayPayment()
        }
    }

    private fun requestGooglePayPayment() {
        val paymentDataRequest = PaymentDataRequest.fromJson(createPaymentRequest().toString())
        if (paymentDataRequest != null) {
            Log.i("GooglePay", "Initiating payment request")
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(paymentDataRequest),
                requireActivity(),
                LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        } else {
            Log.e("GooglePay", "PaymentDataRequest is null.")
        }
    }
}
