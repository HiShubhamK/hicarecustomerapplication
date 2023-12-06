package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.servicesmodule.OrderPayments
import com.ab.hicareservices.databinding.ActivityBookingServiceCheckoutBinding
import com.ab.hicareservices.ui.adapter.BookingServiceCheckoutAdapter
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2

class BookingServiceCheckout : AppCompatActivity() {

    private lateinit var mAdapter: BookingServiceCheckoutAdapter
    private lateinit var binding: ActivityBookingServiceCheckoutBinding
    lateinit var orderPaymentlist: ArrayList<OrderPayments>
    private val viewProductModel: ServiceBooking by viewModels()
    var voucherdiscount = ""
    var finalamount = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_service_checkout)
        binding = ActivityBookingServiceCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgLogo.setOnClickListener{
            onBackPressed()
        }
        orderPaymentlist = ArrayList()

        binding.txttotoalvalue.text = AppUtils2.bookingserviceprice.toString()
        binding.txtfinaltext.text = "\u20B9" + AppUtils2.bookingdiscountedprice
        binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice
        binding.txtdiscount.text = "\u20B9" + AppUtils2.bookingdiscount
        binding.txtbilling.text = AppUtils2.bookingserviceaddress
        finalamount=AppUtils2.bookingdiscountedprice
        binding.txtshipping.text=AppUtils2.Appointmentdataforcheckout //SharedPreferenceUtil.getData(this, "AppointmentStartDateTime", "").toString()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this@BookingServiceCheckout, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceCheckoutAdapter()
        binding.recycleviewproduct.adapter = mAdapter
        mAdapter.setServiceList(AppUtils2.getServicePlanResponseData, this@BookingServiceCheckout)

        binding.txtplcaeorder.setOnClickListener {

//            var data = HashMap<String, Any>()
//
//            data["User_Id"] = SharedPreferenceUtil.getData(this, "User_Id", 0)
//            data["Address_Id"] = SharedPreferenceUtil.getData(this, "Address_Id", 0)
//            data["WebId"] = ""
//            data["WebCustId"] =SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["Fname"] = SharedPreferenceUtil.getData(this, "Fname", "").toString()
//            data["LastName"] = "."
//            data["Email"] = SharedPreferenceUtil.getData(this, "Email", "").toString()
//            data["MobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["CommunicationMobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["AltMobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["Pincode"] = SharedPreferenceUtil.getData(this, "Pincode", "").toString()
//            data["Remarks"] = SharedPreferenceUtil.getData(this, "Remarks", "").toString()
//            data["BHK"] = SharedPreferenceUtil.getData(this, "BHK", "").toString()
//            data["ServiceArea"] = "Home Type"
//            data["OrderCreatedDatetime"] = AppUtils2.getCurrentDateTime().toString()
//            data["LeadSource"] = ""
//            data["CampaignCode"] = ""
//            data["PaymentStatus"]="Online"
//            data["PGResponse"] = ""
//            data["MRP"] = SharedPreferenceUtil.getData(this, "MRP", "").toString()
//            data["DiscountValue"] = "0.00"  // SharedPreferenceUtil.getData(this, "DiscountValue", "").toString()
//            data["DiscountPercent"] = ""
//            data["OrderValue"] = finalamount
//            data["VoucherCode"] = ""
//            data["OnlinePaidAmount"] = finalamount
//            data["PaybackPaidAmount"] = ""
//            data["AppointmentStartDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentStartDateTime", "").toString()
//            data["AppointmentEndDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentEndDateTime", "").toString()
//            data["FlatNo"] = SharedPreferenceUtil.getData(this, "FlatNo", "").toString()
//            data["BuildingName"] = SharedPreferenceUtil.getData(this, "BuildingName", "").toString()
//            data["Locality"] = SharedPreferenceUtil.getData(this, "Locality", "").toString()
//            data["Landmark"] = SharedPreferenceUtil.getData(this, "Landmark", "").toString()
//            data["Street"] = SharedPreferenceUtil.getData(this, "Street", "").toString()
//            data["City"] = SharedPreferenceUtil.getData(this, "City", "").toString()
//            data["State"] = SharedPreferenceUtil.getData(this, "State", "").toString()
//            data["Lat"] = SharedPreferenceUtil.getData(this, "Lat", "").toString()
//            data["Long"] = SharedPreferenceUtil.getData(this, "Long", "").toString()
//            data["ServiceCode"] = SharedPreferenceUtil.getData(this, "ServiceCode", "").toString()
//            data["Param1"] = ""
//            data["Param2"] = ""
//            data["Param3"] = ""
//            data["Source"] = "MobileApp"
//            data["SubSource"] = ""
//            data["UtmSource"] = ""
//            data["UtmSubsource"] = ""
//            data["OrderCreatedFrom"] = "Customer App"
//            data["Subscription"] = ""
//            data["ServiceType"] = "pest"
//            data["Customer_Type"] = "H"
//            data["Created_On"] = AppUtils2.getCurrentDateTime().toString()
//            data["Hygiene_Point_Percentage"] = ""
//            data["Redeem_Hygine_Points"] = ""
//            data["Is_Subscription_Order"] = false
//            data["Subscription_Id"] = ""
//            data["Subscription_Plan_Id"] = ""
//            data["OrderPayments"] = orderPaymentlist
//            data["Campaign_Url"] = ""
//
//

//            data["User_Id"] = SharedPreferenceUtil.getData(this, "User_Id", 0)
//            data["Address_Id"] = SharedPreferenceUtil.getData(this, "Address_Id", 0)
//            data["WebId"] = ""
//            data["WebCustId"] =SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["Fname"] = SharedPreferenceUtil.getData(this, "Fname", "").toString()
//            data["LastName"] = "."
//            data["Email"] = SharedPreferenceUtil.getData(this, "Email", "").toString()
//            data["MobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["CommunicationMobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["AltMobileNo"] = SharedPreferenceUtil.getData(this, "MobileNo", "").toString()
//            data["Pincode"] = SharedPreferenceUtil.getData(this, "Pincode", "").toString()
//            data["Remarks"] = SharedPreferenceUtil.getData(this, "Remarks", "").toString()
//            data["BHK"] = SharedPreferenceUtil.getData(this, "BHK", "").toString()
//            data["ServiceArea"] = "Home Type"
//            data["OrderCreatedDatetime"] = AppUtils2.getCurrentDateTime().toString()
//            data["LeadSource"] = ""
//            data["CampaignCode"] = ""
//            data["PaymentStatus"]="Online"
//            data["PGResponse"] = ""
//            data["MRP"] = SharedPreferenceUtil.getData(this, "MRP", "").toString()
//            data["DiscountValue"] = "0.00"  // SharedPreferenceUtil.getData(this, "DiscountValue", "").toString()
//            data["DiscountPercent"] = ""
//            data["OrderValue"] = finalamount
//            data["VoucherCode"] = ""
//            data["OnlinePaidAmount"] = finalamount
//            data["PaybackPaidAmount"] = ""
//            data["AppointmentStartDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentStartDateTime", "").toString()
//            data["AppointmentEndDateTime"] = SharedPreferenceUtil.getData(this, "AppointmentEndDateTime", "").toString()
//            data["FlatNo"] = SharedPreferenceUtil.getData(this, "FlatNo", "").toString()
//            data["BuildingName"] = SharedPreferenceUtil.getData(this, "BuildingName", "").toString()
//            data["Locality"] = SharedPreferenceUtil.getData(this, "Locality", "").toString()
//            data["Landmark"] = SharedPreferenceUtil.getData(this, "Landmark", "").toString()
//            data["Street"] = SharedPreferenceUtil.getData(this, "Street", "").toString()
//            data["City"] = SharedPreferenceUtil.getData(this, "City", "").toString()
//            data["State"] = SharedPreferenceUtil.getData(this, "State", "").toString()
//            data["Lat"] = SharedPreferenceUtil.getData(this, "Lat", "").toString()
//            data["Long"] = SharedPreferenceUtil.getData(this, "Long", "").toString()
//            data["ServiceCode"] = SharedPreferenceUtil.getData(this, "Spcode", "").toString()
//            data["Param1"] = "instructions hello"
//            data["Param2"] = ""
//            data["Param3"] = ""
//            data["Source"] = "MobileApp"
//            data["SubSource"] = ""
//            data["UtmSource"] = ""
//            data["UtmSubsource"] = ""
//            data["OrderCreatedFrom"] = "Customer App"
//            data["Subscription"] = ""
//            data["ServiceType"] = "pest"
//            data["Customer_Type"] = "Home"
//            data["Created_On"] = AppUtils2.getCurrentDateTime().toString()
//            data["Hygiene_Point_Percentage"] = ""
//            data["Redeem_Hygine_Points"] = ""
//            data["Is_Subscription_Order"] = false
//            data["Subscription_Id"] = ""
//            data["Subscription_Plan_Id"] = ""
//            data["OrderPayments"] = orderPaymentlist
//            data["Campaign_Url"] = "https://hicare.in/"
//
//            Log.d("paydata",data.toString())

            val intent = Intent(this@BookingServiceCheckout, BookingPaymentActivity::class.java)
            intent.putExtra("Finalamount",finalamount)
            intent.putExtra("Vouchercode",binding.txtcoupon.text.toString())
            startActivity(intent)
            finish()
        }

        binding.coupunname.setOnClickListener {

            if(binding.txtcoupon.text.toString().trim().equals("")){
                Toast.makeText(this, "Please enter valid coupon", Toast.LENGTH_LONG).show()
            }else{
                viewProductModel.validatevoucher.observe(this, Observer {

                    if (it.IsSuccess == true) {

                        voucherdiscount = it.Data?.VoucherDiscount.toString()
                        finalamount = it.Data?.FinalAmount.toString()
                        SharedPreferenceUtil.setData(this, "VoucherDiscount", it.Data?.VoucherDiscount.toString())
                        binding.txtfinaltext.text = "\u20B9" + finalamount
                        binding.txttoalamount.text = "\u20B9" + finalamount
                        binding.voucherdiscount.text="\u20B9" +voucherdiscount

                    } else {
                        Toast.makeText(
                            this@BookingServiceCheckout,
                            "Invalid Coupon code",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })

                viewProductModel.PostVoucherValidationcode(
                    binding.txtcoupon.text.toString(),
                    SharedPreferenceUtil.getData(this@BookingServiceCheckout, "Plan_Id", 0) as Int,
                    AppUtils2.bookingdiscountedprice.toFloat()
                )
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}