package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.servicesmodule.OrderPayments
import com.ab.hicareservices.databinding.ActivityBookingServiceCheckoutBinding
import com.ab.hicareservices.databinding.ActivityMyOrderBinding
import com.ab.hicareservices.ui.adapter.BookingServiceCheckoutAdapter
import com.ab.hicareservices.ui.adapter.BookingServiceListAdapter
import com.ab.hicareservices.ui.adapter.BookingServicePlanListAdapter
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager

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
        binding.txtshipping.text=SharedPreferenceUtil.getData(this, "AppointmentStartDateTime", "").toString()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this@BookingServiceCheckout, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceCheckoutAdapter()
        binding.recycleviewproduct.adapter = mAdapter
        mAdapter.setServiceList(AppUtils2.getServicePlanResponseData, this@BookingServiceCheckout)

        binding.txtplcaeorder.setOnClickListener {

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
}