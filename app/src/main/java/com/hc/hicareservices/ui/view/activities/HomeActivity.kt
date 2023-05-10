package com.hc.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.hc.hicareservices.R
import com.hc.hicareservices.data.SharedPreferenceUtil
import com.hc.hicareservices.databinding.ActivityMainBinding
import com.hc.hicareservices.location.MyLocationListener
import com.hc.hicareservices.ui.handler.PaymentListener
import com.hc.hicareservices.ui.view.fragments.AccountFragment
import com.hc.hicareservices.ui.view.fragments.HomeFragment
import com.hc.hicareservices.ui.view.fragments.OrdersFragment
import com.hc.hicareservices.utils.AppUtils2
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener

class HomeActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    var lat = 0.0
    var lng = 0.0
    var paymentListener: PaymentListener? = null
    var titles: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUserStatus()
        takePermissionForLocation()

//        binding.bottomheadertext.text=AppUtils2.order_number

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
//                    setContent("Home")
//                    binding.title.text = "Home"
//                    binding.help.visibility = View.GONE
//                    binding.bottomheadertext.visibility = View.GONE
                    titles = "Home"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance()).commit();
                    true

                }
                R.id.nav_account -> {
//                    binding.title.text = "Account"
//                    binding.help.visibility = View.GONE
//                    binding.bottomheadertext.visibility = View.GONE
                    titles = "Account"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance()).commit();
                    true
                }
                R.id.nav_cart -> {
//                    binding.title.text = "Home"
//                    binding.help.visibility = View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance()).commit();

                    true
                }
                R.id.nav_orders -> {
//                    binding.help.visibility = View.VISIBLE
//                    binding.bottomheadertext.visibility = View.GONE
//                    binding.title.text = "Orders"
                    titles = "Order"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, OrdersFragment.newInstance()).commit();
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
//        binding.title.text = titles.toString()
    }


    private fun checkUserStatus() {
        val mobileNo = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        if (mobileNo == "-1") {
            val i = Intent(this, LoginActivity::class.java)
            //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
    }

    private fun takePermissionForLocation() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true) {
                    getLocation()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        }).check()
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        geocoder = Geocoder(this)
        val mLocManager: LocationManager =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mLocListener = MyLocationListener(this)
        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, mLocListener)
        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lat = mLocListener.latitude
            lng = mLocListener.longitude

            address = geocoder.getFromLocation(lat, lng, 1)!!

            if (address.isNotEmpty()) {
                for (i in 0 until address.size) {
                    val adrs = address[i].getAddressLine(0).toString()
                    val city = address[i].locality.toString()
                    val state = address[i].adminArea.toString()
                    val country = address[i].countryName.toString()
//                    binding.addressTv.text = adrs
                    //val postalcode = address[i].postalCode[i]
                    Log.d("TAG", "$adrs,$city, $state, $country")
                }
            }
        }
    }


    override fun onPaymentSuccess(p0: String?, response: PaymentData?) {
        if (response != null) {
            Toast.makeText(this, "${response.paymentId}", Toast.LENGTH_SHORT).show()
            paymentListener?.onPaymentSuccess(p0, response)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        paymentListener?.onPaymentError(p0, p1, response)
    }

    fun setOnPaymentListener(paymentListener: PaymentListener?) {
        this.paymentListener = paymentListener
    }
}