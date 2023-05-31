package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityPaymentBinding
import com.ab.hicareservices.ui.handler.PaymentListener
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.airbnb.lottie.LottieDrawable
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import kotlin.math.roundToInt

class PaymentActivity : AppCompatActivity(), PaymentListener, PaymentResultWithDataListener {

    private lateinit var binding:ActivityPaymentBinding
    var payment = ""
    var order_no = ""
    lateinit var options: JSONObject
    var accountId = ""
    var service = ""
    var serviceType = ""
    var orderValueWithTax = ""
    var orderValueWithTaxAfterDiscount = ""
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val intent = intent

        order_no = intent.getStringExtra("ORDER_NO").toString()
        accountId = intent.getStringExtra("ACCOUNT_NO").toString()
        serviceType = intent.getStringExtra("SERVICETYPE_NO").toString()
        payment = intent.getDoubleExtra("PAYMENT", Double.MIN_VALUE).toDouble().toString()

        Toast.makeText(
            this,
            "payment: " + payment + "  " + "orderno: " + order_no,
            Toast.LENGTH_SHORT
        ).show()

        val notes = prepareNotes(
            accountId,
            order_no,
            serviceType,
            payment
        )
        options = prepareOption(
            notes,
            serviceType,
            payment
        )

        try {
            val co = Checkout()
            co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
            co.open(this, options)
        } catch (e: Exception) {
            Log.d("TAG", "$e")
        }

    }

    private fun prepareNotes(
        accountId: String,
        orderNo: String,
        serviceType: String,
        orderValue: String
    ): JSONObject {

        val notes = JSONObject()
        notes.put("Account_Id", accountId)
        notes.put("InvoiceNo", "")
        notes.put("OrderNo", orderNo)
        notes.put("Service", serviceType)
        notes.put("OrderValue", orderValue)
        notes.put("OrderValueAfterDiscount", orderValue)
        notes.put("ETDiscount", 5)
        notes.put("ETPaidAmount", orderValue)
        notes.put("Param1", "")
        notes.put("Param2", "")
        notes.put("Param3", "")
        notes.put("IsEditOrder", true)
        notes.put("IsGeneralLink", false)
        notes.put("OrdersAmountMapping", "")
        notes.put("Service_Type", serviceType)
        return notes
    }

    private fun prepareOption(notes: JSONObject, description: String, amount: String): JSONObject {
        val options = JSONObject()
        options.put("name", "HiCare Services")
        options.put("description", description)
        options.put("image", "https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
        options.put("theme.color", "#2BB77A")
        options.put("currency", "INR")
        options.put("amount", "${amount.toDouble().roundToInt()}00")
        options.put("notes", notes)
        return options
    }


    override fun onPaymentSuccess(s: String?, response: PaymentData?) {

        try {


            if (response != null) {

                var data = HashMap<String, Any>()
                data["razorpay_payment_id"] = response?.paymentId.toString()
                data["razorpay_order_id"] = order_no
                data["razorpay_signature"] = response?.signature.toString()
                orderDetailsViewModel.saveAppPaymentDetails(data)
                val data1 = Intent()
                data1.putExtra("title", AppUtils2.paymentsucess)
                finish()
                binding.imgOffer.visibility= View.VISIBLE
                binding.txtpayment.visibility=View.VISIBLE

            }

        } catch (e: Exception) {

        }
    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
        try {
            binding.imgOffer.visibility= View.GONE
            binding.imgOffererror.visibility=View.VISIBLE
            binding.txtpayment.visibility=View.VISIBLE
            binding.txtpayment.text="Payment Failed"
        } catch (e: Exception) {

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val data1 = Intent()
        data1.putExtra("title", AppUtils2.paymentsucess)
        finish()
    }
}