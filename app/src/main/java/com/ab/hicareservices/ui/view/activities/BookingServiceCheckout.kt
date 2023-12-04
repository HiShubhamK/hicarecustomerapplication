package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.servicesmodule.OrderPayments
import com.ab.hicareservices.databinding.ActivityBookingServiceCheckoutBinding
import com.ab.hicareservices.databinding.ActivityMyOrderBinding
import com.ab.hicareservices.ui.adapter.BookingServiceCheckoutAdapter
import com.ab.hicareservices.ui.adapter.BookingServiceListAdapter
import com.ab.hicareservices.ui.adapter.BookingServicePlanListAdapter
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager

class BookingServiceCheckout : AppCompatActivity() {

    private lateinit var mAdapter: BookingServiceCheckoutAdapter
    private lateinit var binding: ActivityBookingServiceCheckoutBinding
    lateinit var orderPaymentlist: ArrayList<OrderPayments>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_service_checkout)
        binding = ActivityBookingServiceCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferencesManager = SharedPreferencesManager(this)

        val retrievedUserData = sharedPreferencesManager.getUserData()

        orderPaymentlist= ArrayList()

        binding.txttotoalvalue.text = AppUtils2.bookingserviceprice.toString()
        binding.txtfinaltext.text = "\u20B9" + AppUtils2.bookingdiscountedprice
        binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice
        binding.txtdiscount.text="\u20B9" + AppUtils2.bookingdiscount
        binding.txtbilling.text=AppUtils2.bookingserviceaddress

        binding.recycleviewproduct.layoutManager = LinearLayoutManager(this@BookingServiceCheckout, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceCheckoutAdapter()
        binding.recycleviewproduct.adapter = mAdapter
        mAdapter.setServiceList(AppUtils2.getServicePlanResponseData,this@BookingServiceCheckout)

        binding.txtplcaeorder.setOnClickListener {


            val sharedPreferencesManager = SharedPreferencesManager(this)
            val retrievedUserData = sharedPreferencesManager.getUserData()
            var orderpaymet= OrderPayments()
            orderpaymet.PaymentMode="Mobile"
            orderpaymet.PaymentMethod="RAZORPAY"
            orderpaymet.Amount= retrievedUserData!!.DiscountValue
            orderpaymet.TransactionId= "200"
            orderpaymet.Type=""
            orderPaymentlist.add(orderpaymet)


            var data = HashMap<String, Any>()

//                data["User_Id"] = SharedPreferenceUtil.getData(this, "User_Id", "").toString()
//                data["Address_Id"] = SharedPreferenceUtil.getData(this, "Address_Id", "").toString()
                data["WebId"] = ""
                data["WebCustId"] = ""
                data["Fname"] = SharedPreferenceUtil.getData(this, "Fname", "").toString()
                data["LastName"] = SharedPreferenceUtil.getData(this, "Lname", "").toString()
                data["Email"] = SharedPreferenceUtil.getData(this, "Email", "").toString()
                data["MobileNo"] =  SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
                data["CommunicationMobileNo"] =  SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
                data["AltMobileNo"] = ""
                data["Pincode"] =  SharedPreferenceUtil.getData(this, "Pincode", "").toString()
                data["Remarks"] =  SharedPreferenceUtil.getData(this, "Remarks", "").toString()
//                data["BHK"] =  SharedPreferenceUtil.getData(this, "BHK", "").to()
                data["ServiceArea"] = retrievedUserData.ServiceArea
                data["OrderCreatedDatetime"] = ""
                data["LeadSource"] = ""
                data["CampaignCode"] = ""
                data["PGResponse"] = ""
                data["MRP"] =  SharedPreferenceUtil.getData(this, "MRP", "").toString()
                data["DiscountValue"] = SharedPreferenceUtil.getData(this, "DiscountValue", "").toString()
                data["DiscountPercent"] = ""
                data["OrderValue"] =  SharedPreferenceUtil.getData(this, "DiscountValue", "").toString()
                data["VoucherCode"] = ""
                data["OnlinePaidAmount"] = ""
                data["PaybackPaidAmount"] = ""
                data["AppointmentStartDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentStartDateTime", "").toString()
                data["AppointmentEndDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentEndDateTime", "").toString()
                data["FlatNo"] =  SharedPreferenceUtil.getData(this, "FlatNo", "").toString()
                data["BuildingName"] = SharedPreferenceUtil.getData(this, "BuildingName", "").toString()
                data["Locality"] = SharedPreferenceUtil.getData(this, "Locality", "").toString()
                data["Landmark"] =  SharedPreferenceUtil.getData(this, "Landmark", "").toString()
                data["Street"] = SharedPreferenceUtil.getData(this, "Street", "").toString()
                data["City"] = SharedPreferenceUtil.getData(this, "City", "").toString()
                data["State"] = SharedPreferenceUtil.getData(this, "State", "").toString()
                data["Lat"] = SharedPreferenceUtil.getData(this, "Lat", "").toString()
                data["Long"] =  SharedPreferenceUtil.getData(this, "Long", "").toString()
                data["ServiceCode"] =  SharedPreferenceUtil.getData(this, "ServiceCode", "").toString()
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


            Log.d("Paymentlist",data.toString())


            val intent=Intent(this@BookingServiceCheckout,BookingPaymentActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}