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
    var razorpayorderid=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_payment)


        binding=ActivityBookingPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)




        viewProductModels.razorpayOrderIdResponse.observe(this, Observer {

            if (it.IsSuccess == true) {
                razorpayorderid = it.Data.toString()
                AppUtils2.razorpayorderid = it.Data.toString()
            } else {

            }

        })
        viewProductModels.CreateRazorpayOrderId(AppUtils2.bookingdiscountedprice.toDouble(), 12342)

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
            "serviceType",
            "payment"
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
        notes.put("address", address)
        notes.put("appointment_end_date_time", "")
        notes.put("appointment_start_date_time","")
        notes.put("customer_name", "")
        notes.put("merchant_order_id", orderValue)
        notes.put("merchant_quote_id", 5)
        notes.put("mobile", orderValue)
        notes.put("pincode", "")
        notes.put("service_plan_name", "")
        notes.put("service_type", "")
        notes.put("spcode", true)
        return notes
    }

    private fun prepareOption(notes: JSONObject, description: String, amount: String): JSONObject {
        val options = JSONObject()
        options.put("name", "HiCare Services")
        options.put("description", description)
        options.put("image", "https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
        options.put("theme.color", "#2BB77A")
        options.put("currency", "INR")
        options.put("amount", "${AppUtils2.bookingdiscountedprice.toDouble().roundToInt()}00")
        options.put("notes", notes)
        options.put("order_id", AppUtils2.razorpayorderid)
        val prefill = JSONObject()
        prefill.put("email", AppUtils2.customeremail)
        prefill.put("contact", AppUtils2.mobileno)
        options.put("prefill",prefill)
        return options
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val sharedPreferencesManager = SharedPreferencesManager(this)
        val retrievedUserData = sharedPreferencesManager.getUserData()
        var orderpaymet=OrderPayments()
        orderpaymet.PaymentMode="Online|Mobile"
        orderpaymet.PaymentMethod="RAZORPAY"
        orderpaymet.Amount= retrievedUserData!!.DiscountValue
        orderpaymet.TransactionId= p1!!.paymentId.toString()
        orderpaymet.Type=""
        orderPaymentlist.add(orderpaymet)


        viewProductModel.paymentsuceess.observe(this, Observer {
            if(it.IsSuccess==true){
            } else {
            }
        })

        var data = HashMap<String, Any>()

        if (retrievedUserData != null) {
            data["User_Id"] = retrievedUserData.User_Id
            data["Address_Id"] = retrievedUserData.Address_Id
            data["WebId"] = retrievedUserData.WebId
            data["WebCustId"] = retrievedUserData.WebCustId
            data["Fname"] = retrievedUserData.Fname
            data["LastName"] = retrievedUserData.LastName
            data["Email"] =retrievedUserData.Email
            data["MobileNo"] = retrievedUserData.MobileNo
            data["CommunicationMobileNo"] = retrievedUserData.MobileNo
            data["AltMobileNo"] = ""
            data["Pincode"] = retrievedUserData.Pincode
            data["Remarks"] = retrievedUserData.Remarks
            data["BHK"] = retrievedUserData.BHK
            data["ServiceArea"] = retrievedUserData.ServiceArea
            data["OrderCreatedDatetime"] = ""
            data["LeadSource"] = ""
            data["CampaignCode"] = ""
            data["PGResponse"] = ""
            data["MRP"] = retrievedUserData.MRP
            data["DiscountValue"] = retrievedUserData.DiscountValue
            data["DiscountPercent"] = ""
            data["OrderValue"] = retrievedUserData.DiscountValue
            data["VoucherCode"] = ""
            data["OnlinePaidAmount"] = ""
            data["PaybackPaidAmount"] = ""
            data["AppointmentStartDateTime"] = retrievedUserData.AppointmentStartDateTime
            data["AppointmentEndDateTime"] = retrievedUserData.AppointmentEndDateTime
            data["FlatNo"] = retrievedUserData.FlatNo
            data["BuildingName"] = retrievedUserData.BuildingName
            data["Locality"] = retrievedUserData.Locality
            data["Landmark"] = retrievedUserData.Landmark
            data["Street"] = retrievedUserData.Street
            data["City"] = retrievedUserData.City
            data["State"] = retrievedUserData.State
            data["Lat"] =retrievedUserData.Lat
            data["Long"] = retrievedUserData.Long
            data["ServiceCode"] = retrievedUserData.ServiceCode
            data["Param1"] = ""
            data["Param2"] = ""
            data["Param3"] = ""
            data["Source"] = "MobileApp"
            data["SubSource"] = ""
            data["UtmSource"] = ""
            data["UtmSubsource"] = ""
            data["OrderCreatedFrom"] = "Customer App"
            data["Subscription"] = ""
            data["ServiceType"] = "pest"
            data["Customer_Type"] = ""
            data["Created_On"] = retrievedUserData.Created_On
            data["Hygiene_Point_Percentage"] = ""
            data["Redeem_Hygine_Points"] = retrievedUserData.Redeem_Hygine_Points
            data["Is_Subscription_Order"] = false
            data["Subscription_Id"] = ""
            data["Subscription_Plan_Id"] = ""
            data["OrderPayments"] = orderPaymentlist
            data["Campaign_Url"] = ""

        }




        viewProductModel.AddOrderAsync(data)

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
}