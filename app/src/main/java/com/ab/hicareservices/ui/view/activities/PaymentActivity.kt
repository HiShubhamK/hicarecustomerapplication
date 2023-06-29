package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityPaymentBinding
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import kotlin.math.roundToInt

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {

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
    var stdvalues=""
    var product=false
    var shippingdata=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shippingdata = SharedPreferenceUtil.getData(this, "Shippingdata", "").toString()


        val intent = intent

        order_no = intent.getStringExtra("ORDER_NO").toString()
        accountId = intent.getStringExtra("ACCOUNT_NO").toString()
        service = intent.getStringExtra("SERVICETYPE_NO").toString()
        serviceType=intent.getStringExtra("SERVICE_TYPE").toString()
        payment = intent.getDoubleExtra("PAYMENT", Double.MIN_VALUE).toDouble().toString()
        stdvalues=intent.getDoubleExtra("Standard_Value__c", Double.MIN_VALUE).toDouble().toString()

        product=intent.getBooleanExtra("Product",false)

        if(product==true){

            val notesproduct = prepareNotesProducts()
            options = prepareOptionforProduct(
                notesproduct,
                AppUtils2.productamount
            )
            try {
                val co = Checkout()
                co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
                co.open(this, options)
            } catch (e: Exception) {
                Log.d("TAG", "$e")
            }

        }else{

            val notes = prepareNotes(
                accountId,
                order_no,
                serviceType,
                payment,
                service,
                stdvalues
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
    }

    private fun prepareOptionforProduct(notesproduct: JSONObject, productamount: String): JSONObject {
        val options = JSONObject()
        options.put("name", "HiCare Services")
        options.put("description", "Product")
        options.put("image", "https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
        options.put("theme.color", "#2BB77A")
        options.put("currency", "INR")
        options.put("amount", "${productamount.toDouble().roundToInt()}00")
        options.put("notes", notesproduct)
        return options
    }


    override fun onResume() {
        super.onResume()

    }

    private fun prepareNotes(
        accountId: String,
        orderNo: String,
        serviceType: String,
        orderValue: String,
        service: String,
        stdvalues: String
    ): JSONObject {

        val notes = JSONObject()
        notes.put("Account_Id", accountId)
        notes.put("InvoiceNo", "")
        notes.put("OrderNo", orderNo)
        notes.put("Service", service)
        notes.put("OrderValue", stdvalues)
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
        var data = HashMap<String, Any>()

        orderDetailsViewModel.savePaymentResponse.observe(this, Observer {
            if(it.isSuccess==true){
                binding.imgOffer.visibility= View.VISIBLE
                binding.txtpayment.visibility=View.VISIBLE
                binding.imgOffererror.visibility=View.GONE

            }else{

                binding.imgOffer.visibility= View.GONE
                binding.imgOffererror.visibility=View.VISIBLE
                binding.txtpayment.visibility=View.VISIBLE
                binding.txtpayment.text="Payment Failed"

            }
        })

        data["razorpay_payment_id"] = response?.paymentId.toString()
        data["razorpay_order_id"] = order_no
        data["razorpay_signature"] = response?.signature.toString()

        orderDetailsViewModel.saveAppPaymentDetails(data)


//        orderDetailsViewModel.savePaymentResponse.observe(this, Observer {
//            if(it.isSuccess==true){
//                binding.imgOffer.visibility= View.VISIBLE
//                binding.txtpayment.visibility=View.VISIBLE
//                binding.imgOffererror.visibility=View.GONE
//
//            }else{
//
//                binding.imgOffer.visibility= View.GONE
//                binding.imgOffererror.visibility=View.VISIBLE
//                binding.txtpayment.visibility=View.VISIBLE
//                binding.txtpayment.text="Payment Failed"
//
//            }
//        })

        Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
        }, 500)



//        try {
//
//            if (response != null) {
//
//                Toast.makeText(this, response?.paymentId.toString(),Toast.LENGTH_SHORT).show()
//                var data = HashMap<String, Any>()
//                data["razorpay_payment_id"] = response?.paymentId.toString()
//                data["razorpay_order_id"] = order_no
//                data["razorpay_signature"] = response?.signature.toString()
//                orderDetailsViewModel.saveAppPaymentDetails(data)
//                Toast.makeText(this, AppUtils2.paymentsucess.toString(),Toast.LENGTH_SHORT).show()
//                val data1 = Intent()
//                data1.putExtra("title", AppUtils2.paymentsucess)
//                finish()
//                binding.imgOffer.visibility= View.VISIBLE
//                binding.txtpayment.visibility=View.VISIBLE
//                binding.imgOffererror.visibility=View.GONE
//
//
//            }
//
//        } catch (e: Exception) {
//
//        }
    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
        try {
            binding.imgOffer.visibility= View.GONE
            binding.imgOffererror.visibility=View.VISIBLE
            binding.txtpayment.visibility=View.VISIBLE
            binding.txtpayment.text="Payment Failed"
            Handler(Looper.getMainLooper()).postDelayed({
                onBackPressed()
            }, 500)

        } catch (e: Exception) {

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val data1 = Intent()
        data1.putExtra("title", AppUtils2.paymentsucess)
        finish()
    }



    private fun prepareNotesProducts() :JSONObject{
        val notes = JSONObject()
        notes.put("Name", AppUtils2.cutomername)
        notes.put("Contact", AppUtils2.customermobile)
        notes.put("Email", AppUtils2.customeremail)
        notes.put("InvoiceNo", "Product")
        notes.put("ActualAmount", AppUtils2.productamount)
        notes.put("UserId", AppUtils2.customerid)
        notes.put("AddressId",shippingdata)
//        notes.put("FlatNo", orderValue)
//        notes.put("BuildingName", "")
//        notes.put("Street", "")
//        notes.put("Landmark", "")
//        notes.put("Pincode", true)
//        notes.put("City", false)
//        notes.put("Locality", "")
        return notes
    }

}