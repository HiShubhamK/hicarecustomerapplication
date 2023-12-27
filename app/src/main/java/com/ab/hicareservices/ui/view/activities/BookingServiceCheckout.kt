package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.servicesmodule.OrderPayments
import com.ab.hicareservices.databinding.ActivityBookingServiceCheckoutBinding
import com.ab.hicareservices.ui.adapter.BookingServiceCheckoutAdapter
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast

class BookingServiceCheckout : AppCompatActivity() {

    private lateinit var mAdapter: BookingServiceCheckoutAdapter
    private lateinit var binding: ActivityBookingServiceCheckoutBinding
    lateinit var orderPaymentlist: ArrayList<OrderPayments>
    private val viewProductModel: ServiceBooking by viewModels()
    var voucherdiscount = ""
    var finalamount = ""
    var checkappliedcoupon = false
    private val viewProductModels: ProductViewModel by viewModels()
    private var lastClickTime: Long = 0
    private val clickTimeThreshold = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_service_checkout)
        binding = ActivityBookingServiceCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtcoupon.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }
        binding.btnEditSlot.setOnClickListener {
            val intent =
                Intent(this@BookingServiceCheckout, BookingSlotComplinceActivity::class.java)
            startActivity(intent)
        }
        orderPaymentlist = ArrayList()

        binding.txttotoalvalue.text = AppUtils2.bookingserviceprice.toString()
//        if (AppUtils2.finalamounts.equals("")) {
            binding.txtfinaltext.text = "\u20B9" + AppUtils2.bookingdiscountedprice
            binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice


//        binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice
        binding.txtdiscount.text = "-" + "\u20B9" + AppUtils2.bookingdiscount
        binding.txtname.text = SharedPreferenceUtil.getData(this, "Fname", "").toString()
        binding.txtbilling.text =
            AppUtils2.bookingserviceaddress + "\n" + "(" + SharedPreferenceUtil.getData(
                this,
                "BHK",
                ""
            ).toString() + ")"
        finalamount = AppUtils2.bookingdiscountedprice
        binding.txtshipping.text =
            AppUtils2.Appointmentdataforcheckout //SharedPreferenceUtil.getData(this, "AppointmentStartDateTime", "").toString()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this@BookingServiceCheckout, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceCheckoutAdapter()
        binding.recycleviewproduct.adapter = mAdapter
        mAdapter.setServiceList(AppUtils2.getServicePlanResponseData, this@BookingServiceCheckout)

        binding.txtcoupon.setText("")

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

            SharedPreferenceUtil.setData(this, "razorpayorderid", "")

            viewProductModels.razorpayOrderIdResponse.observe(this, Observer {

                if (it.IsSuccess == true) {

                    if (AppUtils2.voucherdiscounts.equals("")) {
                    } else {
                        finalamount = AppUtils2.voucherdiscounts

                    }
                    AppUtils2.razorpayorderid = it.Data.toString()
                    SharedPreferenceUtil.setData(this, "razorpayorderid", it.Data.toString())
                    val intent =
                        Intent(this@BookingServiceCheckout, BookingPaymentActivity::class.java)
                    intent.putExtra("Finalamount", finalamount)
                    intent.putExtra("Vouchercode", binding.txtcoupon.text.toString())
                    startActivity(intent)
                    finish()
                } else {

                }

            })

            viewProductModels.CreateRazorpayOrderId(finalamount.toDouble(), 12342)


        }

        binding.coupunname.setOnClickListener {
            if (binding.coupunname.text.toString().equals("Remove")) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime > clickTimeThreshold) {
                    DesignToast.makeText(
                        this,
                        "Coupon removed",
                        Toast.LENGTH_SHORT,
                        DesignToast.TYPE_ERROR
                    ).show()
                    lastClickTime = currentTime
                }
                binding.coupunname.text = "Apply"
                binding.txtcoupon.setText("")
                binding.txtfinaltext.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                binding.txtdiscount.text = "-" + "\u20B9" + AppUtils2.bookingdiscount
                binding.txtbilling.text = AppUtils2.bookingserviceaddress
                binding.voucherdiscount.text = "0.0"
                finalamount = AppUtils2.bookingdiscountedprice

            } else {
                if (binding.txtcoupon.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Please enter valid coupon", Toast.LENGTH_LONG).show()
                    DesignToast.makeText(
                        this,
                        "Please enter valid coupon",
                        Toast.LENGTH_SHORT,
                        DesignToast.TYPE_ERROR
                    ).show()

                } else {
                    viewProductModel.validatevoucher.observe(this, Observer {

                        if (it.IsSuccess == true) {
                            binding.coupunname.text = "Remove"
                            checkappliedcoupon = true
                            voucherdiscount = it.Data?.VoucherDiscount.toString()
                            finalamount = it.Data?.FinalAmount.toString()
//                            AppUtils2.vouchercodedata = binding.txtcoupon.text.toString()
                            AppUtils2.finalamounts = it.Data?.FinalAmount.toString()
                            AppUtils2.voucherdiscounts = it.Data?.VoucherDiscount.toString()
                            SharedPreferenceUtil.setData(
                                this,
                                "VoucherDiscount",
                                it.Data?.VoucherDiscountInPercentage.toString()
                            )

                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastClickTime > clickTimeThreshold) {
                                DesignToast.makeText(
                                    this,
                                    "Coupon code applied successfully!",
                                    Toast.LENGTH_SHORT,
                                    DesignToast.TYPE_SUCCESS
                                ).show()
                                lastClickTime = currentTime
                            }
//
//                            if(checkappliedcoupon==true) {
//                                checkappliedcoupon=false
//                                val currentTime = System.currentTimeMillis()
//                                if (currentTime - lastClickTime > clickTimeThreshold) {
//                                    DesignToast.makeText(this, "Coupon code applied successfully!", Toast.LENGTH_SHORT, DesignToast.TYPE_SUCCESS).show()
//                                    lastClickTime = currentTime
//                                }
//                            }
                            binding.txtfinaltext.text = "\u20B9" + finalamount
                            binding.txttoalamount.text = "\u20B9" + finalamount
                            binding.voucherdiscount.text = "-" + "\u20B9" + voucherdiscount

                        } else {
                            binding.txtfinaltext.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                            binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                            binding.txtdiscount.text = "-" + "\u20B9" + AppUtils2.bookingdiscount
                            binding.txtbilling.text = AppUtils2.bookingserviceaddress
                            finalamount = AppUtils2.bookingdiscountedprice
//                            Toast.makeText(
//                                this@BookingServiceCheckout,
//                                "Invalid Coupon code",
//                                Toast.LENGTH_LONG
//                            ).show()

//
//                            val currentTime = System.currentTimeMillis()
//                            if (currentTime - lastClickTime > clickTimeThreshold) {
//                                DesignToast.makeText(this, "Invalid Coupon code", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show()
//                                lastClickTime = currentTime
//                            }
                        }
                    })

                    viewProductModel.errorMessagevoucher.observe(this, Observer {

                        val currentTimes = System.currentTimeMillis()
                        if (currentTimes - lastClickTime > clickTimeThreshold) {
                            DesignToast.makeText(
                                this,
                                "Invalid Coupon code",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                            lastClickTime = currentTimes
                        }
                        binding.txtfinaltext.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                        binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                        binding.txtdiscount.text = "\u20B9" + AppUtils2.bookingdiscount
                        binding.txtbilling.text = AppUtils2.bookingserviceaddress
                        finalamount = AppUtils2.bookingdiscountedprice
                    })

                    viewProductModel.PostVoucherValidationcode(
                        binding.txtcoupon.text.toString(),
                        SharedPreferenceUtil.getData(
                            this@BookingServiceCheckout,
                            "Plan_Id",
                            0
                        ) as Int,
                        AppUtils2.bookingdiscountedprice.toFloat(),
                        this@BookingServiceCheckout
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils2.vouchercodedata = ""
        var finalamounts = ""
        var voucherdiscounts = ""
    }


}