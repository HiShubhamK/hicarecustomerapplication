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
import com.ab.hicareservices.data.model.servicesmodule.OrderPayments
import com.ab.hicareservices.databinding.ActivityBokingServiceDetailsBinding
import com.ab.hicareservices.databinding.ActivityBookingPaymentBinding
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import kotlin.math.roundToInt

class BookingPaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {

    private lateinit var binding: ActivityBookingPaymentBinding
    lateinit var options: JSONObject
    private val viewProductModel: ServiceBooking by viewModels()
    lateinit var orderPaymentlist: ArrayList<OrderPayments>

    private val viewProductModels: ProductViewModel by viewModels()
    var razorpayorderid = ""
    val bookingdiscountedprice = 1
    var ordervalues = "1"
    var vouchercode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_payment)

        binding = ActivityBookingPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fname = SharedPreferenceUtil.getData(this, "Fname", "").toString()

        val intent = intent
        ordervalues = intent.getStringExtra("Finalamount").toString()
        vouchercode = intent.getStringExtra("Vouchercode").toString()

//        ordervalues = "1"

        orderPaymentlist = ArrayList()

        viewProductModels.razorpayOrderIdResponse.observe(this, Observer {
            if (it.IsSuccess == true) {
                razorpayorderid = it.Data.toString()
                AppUtils2.razorpayorderid = it.Data.toString()
            } else {

            }
        })
        viewProductModels.CreateRazorpayOrderId(bookingdiscountedprice.toDouble(), 12342)

        val notes = prepareNotes(
            "accountId",
            "order_no",
            "serviceType",
            "payment",
            "service",
            "stdvalues"
        )
        options = prepareOption(
            notes,
            "Service | Customer Mobile App",
            "1"
        )

        try {
            val co = Checkout()
//                co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
            co.setKeyID("rzp_live_2QlgSaiHhGkoo8")
            co.open(this, options)
        } catch (e: Exception) {
            Log.d("TAG", "$e")
        }
    }

    private fun prepareNotes(
        address: String,
        orderNo: String,
        cutomername: String,
        orderValue: String,
        service: String,
        stdvalues: String
    ): JSONObject {
        val notes = JSONObject()
        notes.put("address", AppUtils2.addresscode)
        notes.put("appointment_end_date_time", AppUtils2.AppointmentEnd)
        notes.put("appointment_start_date_time", AppUtils2.AppointmentStart)
        notes.put("customer_name", "Akshay")
        notes.put("merchant_order_id", "7738753827")
        notes.put("merchant_quote_id", AppUtils2.customerid)
        notes.put("mobile", orderValue)
        notes.put("pincode", AppUtils2.pincode)
        notes.put("service_plan_name", AppUtils2.ServicePlanName)
        notes.put("service_type", "pest")
        notes.put("spcode", AppUtils2.spcode)
        return notes
    }

    private fun prepareOption(notes: JSONObject, description: String, amount: String): JSONObject {
        val options = JSONObject()
        options.put("name", "HiCare Services")
        options.put("description", "Service | Customer Mobile App")
        options.put("image", "https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
        options.put("theme.color", "#2BB77A")
        options.put("currency", "INR")
        options.put("amount", "${ordervalues.toDouble().roundToInt()}00")
        options.put("notes", notes)
        options.put("order_id", AppUtils2.razorpayorderid)
        val prefill = JSONObject()
        prefill.put("email", AppUtils2.customeremail)
        prefill.put("contact", AppUtils2.mobileno)
        options.put("prefill", prefill)
        return options
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {

        try {

            var VoucherDiscount="0"
            val sharedPreferencesManager = SharedPreferencesManager(this)
            val retrievedUserData = sharedPreferencesManager.getUserData()
            var orderpaymet = OrderPayments()
            orderpaymet.PaymentMode = "Mobile"
            orderpaymet.PaymentMethod = "RAZORPAY"
            orderpaymet.Amount = ordervalues
            orderpaymet.TransactionId = p1!!.paymentId.toString()
            orderpaymet.Type = ""
            orderPaymentlist.add(orderpaymet)

            VoucherDiscount=SharedPreferenceUtil.getData(this, "VoucherDiscount", "").toString()

            viewProductModel.paymentsuceess.observe(this, Observer {
                if (it.IsSuccess == true) {

                    SharedPreferenceUtil.setData(this, "User_Id", 0)
                    SharedPreferenceUtil.setData(this, "Address_Id", 0)
                    SharedPreferenceUtil.setData(this, "Fname", "")
                    SharedPreferenceUtil.setData(this, "Lname", "")
                    SharedPreferenceUtil.setData(this, "Email", "")
                    SharedPreferenceUtil.setData(this, "MobileNo", "")
                    SharedPreferenceUtil.setData(this, "Pincode", "")
                    SharedPreferenceUtil.setData(this, "Remarks", "")
                    SharedPreferenceUtil.setData(this, "BHK", "")
                    SharedPreferenceUtil.setData(this, "MRP", "")
                    SharedPreferenceUtil.setData(this, "DiscountValue", "")
                    SharedPreferenceUtil.setData(this, "AppointmentStartDateTime", "")
                    SharedPreferenceUtil.setData(this, "AppointmentEndDateTime", "")
                    SharedPreferenceUtil.setData(this, "FlatNo", "")
                    SharedPreferenceUtil.setData(this, "BuildingName", "")
                    SharedPreferenceUtil.setData(this, "Locality", "")
                    SharedPreferenceUtil.setData(this, "Landmark", "")
                    SharedPreferenceUtil.setData(this, "Street", "")
                    SharedPreferenceUtil.setData(this, "City", "")
                    SharedPreferenceUtil.setData(this, "State", "")
                    SharedPreferenceUtil.setData(this, "Lat", "")
                    SharedPreferenceUtil.setData(this, "Long", "")
                    SharedPreferenceUtil.setData(this, "ServiceCode", "")
                    SharedPreferenceUtil.setData(this, "Spcode", "")
                    SharedPreferenceUtil.setData(this, "AppointmentStartDateTime", "")
                    SharedPreferenceUtil.setData(this, "VoucherDiscount", "")

                    binding.imgOffer.visibility = View.VISIBLE
                    binding.txtpayment.visibility = View.VISIBLE
                    binding.imgOffererror.visibility = View.GONE

                    val intent = Intent(this@BookingPaymentActivity, HomeActivity::class.java)
                    startActivity(intent)

                } else {
                    binding.imgOffer.visibility = View.GONE
                    binding.imgOffererror.visibility = View.VISIBLE
                    binding.txtpayment.visibility = View.VISIBLE
                    binding.txtpayment.text = "Payment Failed"
                    Handler(Looper.getMainLooper()).postDelayed({
                        onBackPressed()
                        val intent = Intent(this@BookingPaymentActivity, MyOrderActivityNew::class.java)
                        startActivity(intent)
                    }, 500)

                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()

                }
            })


            var data = HashMap<String, Any>()
//
            data["User_Id"] = SharedPreferenceUtil.getData(this, "User_Id", 0)
            data["Address_Id"] = SharedPreferenceUtil.getData(this, "Address_Id", 0)
            data["WebId"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
            data["WebCustId"] =SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
            data["Fname"] = SharedPreferenceUtil.getData(this, "Fname", "").toString()
            data["LastName"] = "."
            data["Email"] = SharedPreferenceUtil.getData(this, "Email", "").toString()
            data["MobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
            data["CommunicationMobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
            data["AltMobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
            data["Pincode"] = SharedPreferenceUtil.getData(this, "Pincode", "").toString()
            data["Remarks"] = SharedPreferenceUtil.getData(this, "Remarks", "").toString()
            data["BHK"] = SharedPreferenceUtil.getData(this, "BHK", "").toString()
            data["ServiceArea"] = "Home Type"
            data["OrderCreatedDatetime"] = AppUtils2.getCurrentDateTime().toString()
            data["LeadSource"] = ""
            data["CampaignCode"] = ""
            data["PaymentStatus"]="Online"
            data["PGResponse"] = ""
            data["MRP"] = SharedPreferenceUtil.getData(this, "MRP", "").toString()
            data["DiscountValue"] = "0.00"  // SharedPreferenceUtil.getData(this, "DiscountValue", "").toString()
            data["DiscountPercent"] = VoucherDiscount
            data["OrderValue"] = ordervalues
            data["VoucherCode"] = vouchercode
            data["OnlinePaidAmount"] = ordervalues
            data["PaybackPaidAmount"] = ""
            data["AppointmentStartDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentStartDateTime", "").toString()
            data["AppointmentEndDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentEndDateTime", "").toString()
            data["FlatNo"] = SharedPreferenceUtil.getData(this, "FlatNo", "").toString()
            data["BuildingName"] = SharedPreferenceUtil.getData(this, "BuildingName", "").toString()
            data["Locality"] = SharedPreferenceUtil.getData(this, "Locality", "").toString()
            data["Landmark"] = SharedPreferenceUtil.getData(this, "Landmark", "").toString()
            data["Street"] = SharedPreferenceUtil.getData(this, "Street", "").toString()
            data["City"] = SharedPreferenceUtil.getData(this, "City", "").toString()
            data["State"] = SharedPreferenceUtil.getData(this, "State", "").toString()
            data["Lat"] = SharedPreferenceUtil.getData(this, "Lat", "").toString()
            data["Long"] = SharedPreferenceUtil.getData(this, "Long", "").toString()
            data["ServiceCode"] = SharedPreferenceUtil.getData(this, "Spcode", "").toString()
            data["Param1"] = ""
            data["Param2"] = ""
            data["Param3"] = ""
            data["Source"] = "MobileApp"
            data["SubSource"] = ""
            data["UtmSource"] = ""
            data["UtmSubsource"] = ""
            data["OrderCreatedFrom"] = "Website"  //Customer App
            data["Subscription"] = ""
            data["ServiceType"] = "pest"
            data["Customer_Type"] = "Home"
            data["Created_On"] = AppUtils2.getCurrentDateTime().toString()
            data["Hygiene_Point_Percentage"] = ""
            data["Redeem_Hygine_Points"] = ""
            data["Is_Subscription_Order"] = false
            data["Subscription_Id"] = ""
            data["Subscription_Plan_Id"] = ""
            data["Cust_InstructionforService"]= SharedPreferenceUtil.getData(this, "Instructions", "").toString()
            data["OrderPayments"] = orderPaymentlist
            data["Campaign_Url"] = "https://hicare.in/"

            viewProductModel.AddOrderAsync(data)

        } catch (e: Exception) {

            Log.d("Paymentlist", "Failed")

        }
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {

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
        val intent=Intent(this@BookingPaymentActivity,BookingServiceCheckout::class.java)
        startActivity(intent)
        finish()
    }

}