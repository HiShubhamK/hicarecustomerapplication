package com.ab.hicareservices.ui.view.activities

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
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityMainBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.handler.PaymentListener
import com.ab.hicareservices.ui.view.fragments.AccountFragment
import com.ab.hicareservices.ui.view.fragments.HomeFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.google.android.gms.tasks.OnCompleteListener
import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.weeks.WeekModel
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.ServiceRequestAdapter
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.adapter.WeeksAdapter
import com.ab.hicareservices.ui.handler.OnRescheduleClickHandler
import com.ab.hicareservices.ui.handler.OnServiceRequestClickHandler
import com.ab.hicareservices.ui.view.fragments.MyServiceDetailsFragment
import com.ab.hicareservices.ui.viewmodel.HomeActivityViewModel
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils
import com.ab.hicareservices.utils.AppUtils2
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class HomeActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    var lat = 0.0
    var lng = 0.0
    var paymentListener: PaymentListener? = null
    var titles: String? = null
    private val viewModel: OtpViewModel by viewModels()
    var token:String?=null
    lateinit var datalist: ArrayList<String>
    private val requestCall = 1
    private val viewModels: HomeActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        datalist=ArrayList()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUserStatus()
        takePermissionForLocation()

        binding.addFab.setColorFilter(Color.WHITE);

        viewModel.validateAccount("9967994682")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->

            if(!task.isSuccessful) {
                return@OnCompleteListener
            }

            token = task.result

            Log.e("Token",token.toString())

            var clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text",token)
            clipboardManager.setPrimaryClip(clipData)

        })


        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getNotificationtoken(token.toString())
        }, 1500)



//        binding.bottomheadertext.text=AppUtils2.order_number

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    binding.addFab.visibility=View.VISIBLE
//                    setContent("Home")
//                    binding.title.text = "Home"
//                    binding.help.visibility = View.GONE
//                    binding.bottomheadertext.visibility = View.GONE
//                    titles = "Home"
//                    binding.title.text="Welcome To Hicare"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance()).commit();
                    true

                }
                R.id.nav_account -> {
//                    binding.title.text = "Account"
//                    binding.help.visibility = View.GONE
//                    binding.bottomheadertext.visibility = View.GONE
//                    titles = "Account"
//                    binding.title.text="Account"

                    binding.addFab.visibility=View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance()).commit();
                    true
                }
                R.id.nav_cart -> {
//                    binding.title.text = "Home"
//                    binding.help.visibility = View.GONE
                    binding.title.text="Welcome To Hicare"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance()).commit();

                    true
                }
                R.id.nav_orders -> {
//                    binding.help.visibility = View.VISIBLE
//                    binding.bottomheadertext.visibility = View.GONE
//                    binding.title.text = "Orders"
//                    titles = "Order"
//                    binding.title.text="Order"
                    binding.addFab.visibility=View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, OrdersFragment.newInstance()).commit();
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.nav_home;

        binding.addFab.setOnClickListener{
            showLeadDialog()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            getLeadMethod()
        }, 1500)

    }

    private fun getLeadMethod() {
        viewModels.spinnerList.observe(this, Observer{
            datalist.addAll(it)
        })

        viewModels.getleaderspinner("pest")
    }


    private fun showLeadDialog() {
        var selectedLocation = ""
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_lead, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
//        val today: DateTime = DateTime().withTimeAtStartOfDay()
        val spinner = promptsView.findViewById<View>(R.id.spinner_lead) as AppCompatSpinner
        val lnrcall = promptsView.findViewById<View>(R.id.getcall) as LinearLayoutCompat
//        val btnSubmit = promptsView.findViewById<View>(R.id.btnSubmit) as Button

        lnrcall.setOnClickListener {
            makePhoneCall()
        }

        val arrayAdapter =
            object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, datalist) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }
        arrayAdapter.setDropDownViewResource(R.layout.spinner_popup)
        spinner.adapter = arrayAdapter



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedLocation = spinner.selectedItem.toString()
                if (selectedLocation != "Select Type") {

                } else {
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        alertDialog.show()
    }

    private fun makePhoneCall() {
        var number:String="8976399055"
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
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


