package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
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
import com.ab.hicareservices.ui.handler.onCheckoutinstruciton
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import java.math.BigDecimal
import kotlin.math.round
import kotlin.math.roundToInt

class BookingServiceCheckout : AppCompatActivity(){

    lateinit var progressDialog: ProgressDialog
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
    private var lastClickTimes: Long = 0
    var data=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_service_checkout)

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)


        binding = ActivityBookingServiceCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppUtils2.paymentcheckbutton = false

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
        binding.txtfinaltext.text = "\u20B9" + round( AppUtils2.bookingdiscountedprice.toInt().toDouble())
        binding.txttoalamount.text = "\u20B9" + round(AppUtils2.bookingdiscountedprice.toInt().toDouble())


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

        mAdapter.setTextChangedata(object : onCheckoutinstruciton{
            override fun onItemClick(position: String) {

                SharedPreferenceUtil.setData(
                    this@BookingServiceCheckout,
                    "Instructions",
                    ""
                )

                data = position

                SharedPreferenceUtil.setData(
                    this@BookingServiceCheckout,
                    "Instructions",
                    data
                )
            }
        })

        mAdapter.setServiceList(AppUtils2.getServicePlanResponseData, this@BookingServiceCheckout)




        binding.txtcoupon.setText("")

        binding.txtplcaeorder.setOnClickListener {

            Handler(Looper.getMainLooper()).postDelayed({

                progressDialog.show()

//            Toast.makeText(this@BookingServiceCheckout,data,Toast.LENGTH_SHORT).show()

                var finalamountsdata = finalamount.toDouble().roundToInt()

                if (AppUtils2.paymentcheckbutton == false) {

                    progressDialog.dismiss()

                    AppUtils2.paymentcheckbutton = true

                    SharedPreferenceUtil.setData(this, "razorpayorderid", "")

                    viewProductModels.razorpayOrderIdResponse.observe(this, Observer {

                        progressDialog.dismiss()

                        if (it.IsSuccess == true) {

                            if (AppUtils2.voucherdiscounts.equals("")) {
                                finalamount = AppUtils2.bookingdiscountedprice
                            } else {
                                finalamount = AppUtils2.finalamounts
                            }
                            AppUtils2.razorpayorderid = ""
                            AppUtils2.razorpayorderid = it.Data.toString()
                            SharedPreferenceUtil.setData(
                                this,
                                "razorpayorderid",
                                it.Data.toString()
                            )
                            val intent =
                                Intent(
                                    this@BookingServiceCheckout,
                                    BookingPaymentActivity::class.java
                                )
                            intent.putExtra("Finalamount", finalamountsdata.toDouble().toString())
                            intent.putExtra("Vouchercode", binding.txtcoupon.text.toString())
                            startActivity(intent)
                            finish()
                        } else {
                            progressDialog.dismiss()
                        }
                    })

                    viewProductModels.CreateRazorpayOrderId(finalamountsdata.toDouble(), 12342)

                } else {
                    AppUtils2.paymentcheckbutton = false

                }
                progressDialog.dismiss()
            },1000)
        }

//        binding.btnappiledcoupon.setOnClickListener {
//
//            viewProductModel.errorMessagevoucher.observe(this, Observer {
//                if(AppUtils2.checkerrormessage==true) {
//                    AppUtils2.checkerrormessage=false
//                    if (it.equals("false")) {
//                        Toast.makeText(this@BookingServiceCheckout, "Wrong", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//            })
//
//            viewProductModel.validatevoucher.observe(this, Observer {
//                if(AppUtils2.checkvoucheer==true) {
//                    AppUtils2.checkvoucheer=false
//                    if (it.IsSuccess == true) {
//                        Toast.makeText(this@BookingServiceCheckout, "Success", Toast.LENGTH_LONG)
//                            .show()
//                    } else {
//                        Toast.makeText(this@BookingServiceCheckout, "failed", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//            })
//
//            viewProductModel.PostVoucherValidationcode(
//                binding.txtcoupon.text.toString(),
//                SharedPreferenceUtil.getData(
//                    this@BookingServiceCheckout,
//                    "Plan_Id",
//                    0
//                ) as Int,
//                AppUtils2.bookingdiscountedprice.toFloat(),
//                this@BookingServiceCheckout
//            )
//        }

        binding.btnappiledcoupon.setOnClickListener {
            Toast.makeText(this@BookingServiceCheckout, SharedPreferenceUtil.getData(this@BookingServiceCheckout, "Instructions",
                "").toString(),Toast.LENGTH_SHORT)

            Log.d("instuftion",SharedPreferenceUtil.getData(
                this@BookingServiceCheckout,
                "Instructions",
                ""
            ).toString())

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
                AppUtils2.finalamounts=""
                binding.coupunname.text = "Apply"
                binding.txtcoupon.setText("")
                binding.txtfinaltext.text = "\u20B9" + round( AppUtils2.bookingdiscountedprice.toInt().toDouble())
                binding.txttoalamount.text = "\u20B9" + round(AppUtils2.bookingdiscountedprice.toInt().toDouble())
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

                        if (AppUtils2.checkvoucheer == true) {
                            AppUtils2.checkvoucheer = false
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

                                val currentTimes = System.currentTimeMillis()
                                if (currentTimes - lastClickTime > clickTimeThreshold) {
                                    DesignToast.makeText(
                                        this,
                                        "Coupon code applied successfully!",
                                        Toast.LENGTH_SHORT,
                                        DesignToast.TYPE_SUCCESS
                                    ).show()
                                    lastClickTime = currentTimes
                                }
                                var ranges=finalamount.toDouble().roundToInt()    //roundString(finalamount)
                                binding.txtfinaltext.text = "\u20B9" + ranges.toDouble().toString()
                                binding.txttoalamount.text = "\u20B9" + ranges.toDouble().toString()
                                binding.voucherdiscount.text = "-" + "\u20B9" + voucherdiscount

                            } else {
                            }
                        }
                    })

                    viewProductModel.errorMessagevoucher.observe(this, Observer {

                        Log.d("checkcoupon", "Wrongtoast")

                        if (AppUtils2.checkerrormessage == true) {
                            AppUtils2.checkerrormessage = false
                            if (it.equals("false")) {
                                DesignToast.makeText(
                                    this,
                                    "Invalid Coupon code",
                                    Toast.LENGTH_SHORT,
                                    DesignToast.TYPE_ERROR
                                ).show()
                            }
                        }

                        binding.txtfinaltext.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                        binding.txttoalamount.text = "\u20B9" + AppUtils2.bookingdiscountedprice
                        binding.txtdiscount.text = "-"+"\u20B9" + AppUtils2.bookingdiscount
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

    fun roundString(input: String): String {
        try {
            // Convert the string to a BigDecimal
            val bigDecimalValue = BigDecimal(input)

            // Round the BigDecimal value
            val roundedValue = bigDecimalValue.setScale(2, BigDecimal.ROUND_HALF_UP)

            // Convert the rounded value back to a string
            return roundedValue.toString()
        } catch (e: NumberFormatException) {
            // Handle the exception or simply return the original string
            return input
        }
    }
}