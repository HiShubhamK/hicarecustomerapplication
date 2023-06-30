package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.HomeProduct
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.ActivityPaymentBinding
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.joda.time.DateTime
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {

    private lateinit var binding: ActivityPaymentBinding
    var cartlist = mutableListOf<CartlistResponseData>()
    lateinit var datalist: ArrayList<CartlistResponseData>
    lateinit var homeproduct: ArrayList<HomeProduct>

    private val viewProductModel: ProductViewModel by viewModels()
    var payment = ""
    var order_no = ""
    lateinit var options: JSONObject
    var accountId = ""
    var service = ""
    var serviceType = ""
    var orderValueWithTax = ""
    var orderValueWithTaxAfterDiscount = ""
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    var stdvalues = ""
    var product = false
    var shippingdata = ""
    var billingdata = ""
    var flat = ""
    var street = ""
    var landmark = ""
    var locality = ""
    var builingname = ""
    var pincode = ""
    var totaldiscount = ""
    var actualvalue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shippingdata = SharedPreferenceUtil.getData(this, "Shippingdata", "").toString()

        billingdata = SharedPreferenceUtil.getData(this, "Billingdata", "").toString()

        getSummarydata()

        homeproduct = ArrayList()

        val intent = intent

        datalist = ArrayList()

        datalist = AppUtils2.leaderlist

        var calendar = Calendar.getInstance()
        var simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss aaa z")


//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
//        val current = LocalDateTime.now().format(formatter)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val current = formatter.format(date)

        for (i in 0 until datalist.size) {
            homeproduct.add(
                HomeProduct(
                    datalist.get(i).ProductId,
                    datalist.get(i).ProductName,
                    datalist.get(i).ProductCode,
                    datalist.get(i).ProductDisplayName,
                    datalist.get(i).ProductThumbnail,
                    "",
                    datalist.get(i).DiscountType,
                    datalist.get(i).Discount?.toDouble(),
                    datalist.get(i).PricePerQuantity?.toDouble(),
                    datalist.get(i).DiscountedPrice?.toDouble(),
                    "",
                    "",
                    0.0,
                    datalist.get(i).ProductWeight,
                    false,
                    false,
                    false,
                    false,
                    0.0,
                    0,
                    "",
                    datalist.get(i).Quantity,
                    0,
                    "",
                    ""
                )
            )
        }

        Toast.makeText(this, datalist.size.toString(), Toast.LENGTH_LONG).show()

        Toast.makeText(this, homeproduct.size.toString(), Toast.LENGTH_LONG).show()


        order_no = intent.getStringExtra("ORDER_NO").toString()
        accountId = intent.getStringExtra("ACCOUNT_NO").toString()
        service = intent.getStringExtra("SERVICETYPE_NO").toString()
        serviceType = intent.getStringExtra("SERVICE_TYPE").toString()
        payment = intent.getDoubleExtra("PAYMENT", Double.MIN_VALUE).toDouble().toString()
        stdvalues =
            intent.getDoubleExtra("Standard_Value__c", Double.MIN_VALUE).toDouble().toString()

        product = intent.getBooleanExtra("Product", false)

        if (product == true) {

//            getproductlist()

            getAddressforbilling()

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

        } else {

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

    fun getSummarydata() {

        viewProductModel.getsummarydata.observe(this, Observer {

            AppUtils2.productamount = it.FinalAmount.toString()
            totaldiscount = it.TotalDiscount.toString()
            actualvalue = it.TotalAmount.toString()


        })


        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(), AppUtils2.pincode, "")

    }


    private fun getproductlist() {
        viewProductModel.cartlist.observe(this, Observer {

//            for (i in 0 until it.size){
//                datalist.add(HomeProduct(it.get(i).ProductId,
//                    it.get(i).ProductName,
//                    it.get(i).ProductCode,
//                    it.get(i).ProductDisplayName,
//                    it.get(i).ProductThumbnail,
//                    "",
//                    it.get(i).DiscountType,
//                    it.get(i).Discount,
//                    it.get(i).PricePerQuantity,
//                    it.get(i).DiscountedPrice,
//                    "",
//                    "",
//                    0,
//                    it.get(i).ProductWeight,
//                    false,
//                    false,
//                    false,
//                    false,
//                    0,
//                    0,
//                    "",
//                    it.get(i).Quantity,
//                    0,
//                    "",
//                    ""
//                    ))
//            }

        })

        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt())
    }


    private fun getAddressforbilling() {
        viewProductModel.getaddressbydetailid.observe(this, Observer {

            flat = it.FlatNo.toString()
            builingname = it.BuildingName.toString()
            street = it.Street.toString()
            locality = it.Locality.toString()
            landmark = it.Landmark.toString()
            pincode = it.Pincode.toString()
        })
        viewProductModel.getAddressDetailbyId(billingdata!!.toInt())
    }

    private fun prepareOptionforProduct(
        notesproduct: JSONObject,
        productamount: String
    ): JSONObject {
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

        Toast.makeText(this, "data json" + datalist.size.toString(), Toast.LENGTH_LONG).show()

        Toast.makeText(this, "data json" + AppUtils2.productamount, Toast.LENGTH_LONG).show()

        if (product == true) {


            var data = HashMap<String, Any>()

            data["HomeProduct"] = homeproduct
            data["AddressId"] = shippingdata.toInt()
            data["BillToAddressId"] = billingdata.toInt()
            data["Pincode"] = pincode
            data["CartAmount"] = AppUtils2.actualvalue.toDouble()
            data["PayableAmount"] = AppUtils2.productamount.toDouble()
            data["DiscountAmount"] = AppUtils2.totaldiscount.toDouble()
            data["DelieveryCharges"] = 0.0
            data["InstallationCharges"] = 0.0
            data["VoucherCode"] = ""
            data["SFDC_OrderNo"] = ""
            data["PaymentId"] = response!!.paymentId
            data["PayMethod"] = ""
            data["PayStatus"] = ""
            data["PayAmount"] = 0.0
            data["Booking_Source"] = ""
            data["Referred_By_Technician"] = ""
            data["Order_Source"] = ""
            data["Payment_LinkId"] = ""
            data["Razorpay_Payment_Id"] = response!!.paymentId
            data["User_Id"] = AppUtils2.customerid

            viewProductModel.postSaveSalesOrder(data)

        } else {
            orderDetailsViewModel.savePaymentResponse.observe(this, Observer {
                if (it.isSuccess == true) {
                    binding.imgOffer.visibility = View.VISIBLE
                    binding.txtpayment.visibility = View.VISIBLE
                    binding.imgOffererror.visibility = View.GONE

                } else {

                    binding.imgOffer.visibility = View.GONE
                    binding.imgOffererror.visibility = View.VISIBLE
                    binding.txtpayment.visibility = View.VISIBLE
                    binding.txtpayment.text = "Payment Failed"

                }
            })


            var data = HashMap<String, Any>()


            data["razorpay_payment_id"] = response?.paymentId.toString()
            data["razorpay_order_id"] = order_no
            data["razorpay_signature"] = response?.signature.toString()

            orderDetailsViewModel.saveAppPaymentDetails(data)

        }


    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
        try {
            binding.imgOffer.visibility = View.GONE
            binding.imgOffererror.visibility = View.VISIBLE
            binding.txtpayment.visibility = View.VISIBLE
            binding.txtpayment.text = "Payment Failed"
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


    private fun prepareNotesProducts(): JSONObject {
        val notes = JSONObject()
        notes.put("Name", AppUtils2.cutomername)
        notes.put("Contact", AppUtils2.customermobile)
        notes.put("Email", AppUtils2.customeremail)
        notes.put("InvoiceNo", "Product")
        notes.put("ActualAmount", AppUtils2.productamount)
        notes.put("UserId", AppUtils2.customerid)
        notes.put("AddressId", shippingdata)
        notes.put("FlatNo", flat)
        notes.put("BuildingName", builingname)
        notes.put("Street", street)
        notes.put("Landmark", landmark)
        notes.put("Pincode", pincode)
        notes.put("City", "")
        notes.put("Locality", locality)
        return notes
    }

}