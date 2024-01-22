package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityMainBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.handler.PaymentListener
import com.ab.hicareservices.ui.view.fragments.AccountFragment
import com.ab.hicareservices.ui.view.fragments.HomeFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragmentNew
import com.ab.hicareservices.ui.viewmodel.HomeActivityViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

import android.provider.Settings
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.NotificationManagerCompat
import com.ab.hicareservices.ui.view.fragments.PestServiceFragment
import com.ab.hicareservices.utils.AppUpdater
import com.ab.hicareservices.utils.ConnectivityChangeListener
import com.ab.hicareservices.utils.ConnectivityReceiver
import com.ab.hicareservices.utils.DesignToast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.mixpanel.android.mpmetrics.MixpanelAPI

class HomeActivity : AppCompatActivity(), PaymentResultWithDataListener,ConnectivityChangeListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    var lat = 0.0
    var lng = 0.0
    var paymentListener: PaymentListener? = null
    var titles: String? = null
    private val viewModel: OtpViewModel by viewModels()
    var token: String? = null
    lateinit var datalist: ArrayList<String>
    private val requestCall = 1
    private val viewModels: HomeActivityViewModel by viewModels()
    private val viewProductModel: ProductViewModel by viewModels()
    private val viewModelss: OtpViewModel by viewModels()
    var activiyname=""
    val REQUEST_CODE_PERMISSIONS=101
    var customerid: String = ""
    var pincode: String? = null



    private lateinit var connectivityReceiver: ConnectivityReceiver
    private val notificationPermissionRequestCode = 1001


    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
//            Toast.makeText(this,"Hello akshay",Toast.LENGTH_SHORT).show()
        } else {
            AppUtils2.ISChecklocationpermission=true
//            Toast.makeText(this,"Hello akshay fails ",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get Mixpanel instance
        // Get Mixpanel instance


        AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

        if (
//            ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // When permission is granted
            // Call method
//            getCurrentLocations()

        } else {
            enableLoc()

            // When permission is not granted
            // Call method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    100
                )
            }
        }



        connectivityReceiver = ConnectivityReceiver(this)
        registerReceiver(
            connectivityReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        viewModels.currentapp.observe(this, Observer {
            if (it != null) {
                it.IsUpdated?.let { it1 -> SharedPreferenceUtil.setData(this, "IsUpdated", it1) }
                val updateManager = AppUpdater(this,it.Versioncode.toString(),it.IsUpdated)
                updateManager.checkForUpdate(it.Versioncode.toString(),it.IsUpdated)
                AppUtils2.versionname=it.Versionname.toString()
            }
        })

        viewModels.getcurretnapversioncode(AppUtils2.mobileno)

        binding.addFab.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment.newInstance()).commitAllowingStateLoss()

//        binding.bottomheadertext.text=AppUtils2.order_number

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    if(AppUtils2.isNetworkAvailable(this)==true){

                    }else{
                        DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

                    }
                    binding.addFab.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .addToBackStack("HomeFragment").commitAllowingStateLoss()
                    true
                }
                R.id.nav_bookings -> {
                    if(AppUtils2.isNetworkAvailable(this)==true){

                    }else{
                        DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                        Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                    }
                    binding.addFab.visibility = View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProductFragment.newInstance())
                        .addToBackStack("HomeFragment").commitAllowingStateLoss()
                    true
                }
                R.id.nav_account -> {
                    if(AppUtils2.isNetworkAvailable(this)==true){

                    }else{
                        DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                        Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                    }
                    binding.addFab.visibility = View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance())
                        .addToBackStack("AccountFragment").commitAllowingStateLoss()
                    true
                }
                R.id.nav_cart -> {
                    if(AppUtils2.isNetworkAvailable(this)==true){

                    }else{
                        DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                        Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance()).addToBackStack("Tag")
                        .commitAllowingStateLoss()

                    true
                }
                R.id.nav_orders -> {

                    binding.addFab.visibility = View.GONE
//                    AppUtils2.fromdasboardmenu = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, PestServiceFragment.newInstance())
                        .addToBackStack("PestServiceFragment").commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.nav_home






//        viewProductModel.customerlogininfo.observe(this, Observer {
//            Toast.makeText(this,it.Data!!.Id.toString(),Toast.LENGTH_LONG).toString()
//            customerid= it.Data!!.Id!!
//        })

//        viewProductModel.getCustomerid("7208408308")
//
//        viewProductModel.getProductlist("400601")
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            getCustomerAddress()
//        }, 1500)

    }

    private fun enableLoc() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000
        locationRequest.fastestInterval = 5 * 1000
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.status.resolution!!!!).build()
                        launcher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
//                when (exception.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
//                        // user a dialog.
//                        try {
//                            // Cast to a resolvable exception.
//                            val resolvable = exception as ResolvableApiException
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            resolvable.startResolutionForResult(
//                                this,
//                                REQUEST_CODE_PERMISSIONS
//                            )
//
//                        } catch (e: IntentSender.SendIntentException) {
//                            // Ignore the error.
//                        } catch (e: ClassCastException) {
//                            // Ignore, should be an impossible error.
//                        }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
//                }
            }
        }
    }


//    private fun requestNotificationPermission() {
//        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
//            // Notifications are enabled, but permission is missing
//            // You can show a custom dialog to explain the need for notification permission
//            // and request the permission using ActivityCompat.requestPermissions()
//
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                notificationPermissionRequestCode
//            )
//        } else {
//            // Notifications are disabled completely, handle accordingly
//            // You might want to guide the user to enable notifications in settings
//        }
//
//    }




    private fun isNotificationPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return notificationManager.areNotificationsEnabled()
        } else {
            // For versions prior to Android O, notification permission is granted by default
            return true
        }
    }

//    private fun requestNotificationPermission() {
//        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
//            // Notifications are enabled, but permission is missing
//            // You can show a custom dialog to explain the need for notification permission
//            // and request the permission using ActivityCompat.requestPermissions()
//
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.RECEIVE_NOTIFICATIONS),
//                notificationPermissionRequestCode
//            )
//        } else {
//            // Notifications are disabled completely, handle accordingly
//            // You might want to guide the user to enable notifications in settings
//        }
//    }











//    notificaitons
    private fun requestNotificationPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            startActivity(intent)
        } else {
            // For versions prior to Android O, no explicit permission is required
            // You can explain the importance of notifications to the user here
        }
    }

    // Handle the result of the notification permission request
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == notificationPermissionRequestCode) {
            if (isNotificationPermissionGranted()) {
                // Permission granted, proceed with your logic
            } else {
                // Permission denied, you can handle this case accordingly
            }
        }
    }


    private fun getCustomerAddress() {
        viewProductModel.cutomeraddress.observe(this, Observer {
        })

        viewProductModel.getCustomerAddress(1)

    }

    private fun getLeadMethod() {
        try {
            viewModels.spinnerList.observe(this, Observer {
                if (it != null) {
                    datalist.addAll(it)
                    AppUtils2.servicetype.addAll(it)
                }
            })

//            viewModels.requestcode.observe(this, Observer {
//                Toast.makeText(this,"Session Expired", Toast.LENGTH_LONG).show()
//
//                SharedPreferenceUtil.setData(this, "mobileNo", "-1")
//                SharedPreferenceUtil.setData(this, "bToken", "")
//                SharedPreferenceUtil.setData(this, "IsLogin", false)
//                SharedPreferenceUtil.setData(this, "pincode", "")
//                SharedPreferenceUtil.setData(this, "customerid", "")
//                SharedPreferenceUtil.setData(this, "FirstName", "")
//                SharedPreferenceUtil.setData(this, "MobileNo", "")
//
//                val intent= Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            })

            viewModels.getleaderspinner("pest")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showLeadDialog() {
        var selectedLocation = ""
        var dateTime = ""
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_lead, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
//        val today: DateTime = DateTime().withTimeAtStartOfDay()
        val edtname = promptsView.findViewById<View>(R.id.edtname) as EditText
        val edtmobile = promptsView.findViewById<View>(R.id.edtmobile) as EditText
        val edtpincode = promptsView.findViewById<View>(R.id.edtpincode) as EditText
        val spinner = promptsView.findViewById<View>(R.id.spinner_lead) as AppCompatSpinner
        val lnrcall = promptsView.findViewById<View>(R.id.getcall) as LinearLayoutCompat
        val btnSubmit = promptsView.findViewById<View>(R.id.btnlead) as Button
        val email = promptsView.findViewById<View>(R.id.textemail) as TextView
        val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView

        alertDialog.setCancelable(false)

        imgcancels.setOnClickListener { alertDialog.cancel() }

        email.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "message/rfc822"
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("me@somewhere.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "My subject")

            startActivity(Intent.createChooser(intent, "Email via..."))
        }

        AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss aaa z")
        dateTime = simpleDateFormat.format(calendar.time).toString()


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

        btnSubmit.setOnClickListener {

            if (edtname.text.toString().trim().equals("")) {
                edtname.setError("Enter name")
            } else if (edtmobile.text.toString().trim().equals("")) {
                edtmobile.setError("Enter mobile number")
            } else if (edtmobile.text.toString().trim().length < 10) {
                edtmobile.setError("Enter correct mobile number")
            } else if (edtmobile.text.toString().trim().equals("0000000000")) {
                edtmobile.setError("Enter correct mobile number")
            } else if (edtpincode.text.toString().trim().equals("")) {
                edtpincode.setError("Enter pincode")
            } else if (edtpincode.text.toString().length < 6) {
                edtpincode.setError("Enter correct pincode")
            } else if (edtpincode.text.toString().trim().equals("000000")) {
                edtpincode.setError("Enter correct pincode")
            } else if (selectedLocation.toString().trim().equals("Select Type")) {
                Toast.makeText(this, "Please select Service type", Toast.LENGTH_SHORT).show()
            } else {
                var data = HashMap<String, Any>()
                data["LMSId"] = ""
                data["SFDCId"] = ""
                data["CallCenterId"] = ""
                data["ServiceType"] = "pest"
                data["Batch_Name"] = ""
                data["Original_Batch_Name"] = ""
                data["Created_On"] = ""
                data["LeadType"] = ""
                data["Salutation"] = ""
                data["FirstName"] = edtname.text.toString()
                data["LastName"] = "."
                data["Mobile"] = edtmobile.text.toString()
                data["AltMobile"] = ""
                data["Email"] = ""
                data["Company"] = ""
                data["EmployeeCount"] = 0
                data["Service"] = selectedLocation.toString()
                data["ServiceCategory"] = ""
                data["ServiceSubCategory"] = ""
                data["FlatNo"] = ""
                data["Building"] = ""
                data["Street"] = ""
                data["Locality"] = ""
                data["Landmark"] = ""
                data["City"] = ""
                data["State"] = ""
                data["Pincode"] = edtpincode.text.toString()
                data["Lat"] = ""
                data["Long"] = ""
                data["Priority"] = 0
                data["Agency"] = ""
                data["Utm_Campaign"] = "Mobile app"
                data["Utm_Source"] = "Mobile app"
                data["Utm_Sub_Source"] = "Mobile app"
                data["BHK"] = ""
                data["Status"] = ""
                data["Service_Value"] = ""
                data["PaymentMode"] = ""
                data["Lead_Source"] = "Mobile app"
                data["Lead_Sub_Source"] = "Mobile app"
                data["Remark"] = ""
                data["Gclid"] = ""
                data["Utm_Medium"] = "Mobile app"
                data["Utm_Content"] = "Mobile app"
                data["Utm_Term"] = "Mobile app"
                data["Campaign_Url"] = ""

                viewModels.leadResponse.observe(this, Observer {
                    if (it.IsSuccess == true) {
                        if(AppUtils2.checkerrormessage==true) {
                            AppUtils2.checkerrormessage=false
                            DesignToast.makeText(
                                this,
                                "Thanks for your call request. Our team will contact you soon",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_SUCCESS
                            ).show();
                            alertDialog.cancel()
                            AppUtils2.eventCall(this, "Get a call dashboard: " + data)
                        }
                    } else {
                        alertDialog.cancel()
                        DesignToast.makeText(this,"Something went to wrong", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                        Toast.makeText(this, "Something went to wrong", Toast.LENGTH_LONG).show()
                    }
                })
                viewModels.postleaderdata(data)
            }

        }


        alertDialog.show()
    }

    private fun makePhoneCall() {
        var number: String = "8828333888"
        if (number.trim { it <= ' ' }.isNotEmpty()) {
//
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
//
        } else {
            DesignToast.makeText(this,"Enter Phone Number", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkUserStatus() {
        val mobileNo = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        if (mobileNo == "-1") {
//            val i = Intent(this, LoginActivity::class.java)
//            //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(i)
//            finish()
        }
    }

    override fun onPaymentSuccess(p0: String?, response: PaymentData?) {
        if (response != null) {
            DesignToast.makeText(this,"${response.paymentId}", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//            Toast.makeText(this, "${response.paymentId}", Toast.LENGTH_SHORT).show()
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

    override fun onBackPressed() {
        super.onBackPressed()

        if (binding.bottomNavigation.getSelectedItemId() === R.id.nav_home) {
            finish()
            finishAffinity()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }
    }

    override fun onConnectivityChanged(isConnected: Boolean) {
        try {
            if (isConnected) {
                datalist = ArrayList()
                datalist.add("Select Type")
                AppUtils2.servicetype.clear()
                AppUtils2.servicetype.add("Select Type")

                val intent = intent
                activiyname = intent.getStringExtra("Externallink").toString()


                if (!AppUtils2.Activityname.equals("") && AppUtils2.Activityname.startsWith("http")) {
                    try {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(AppUtils2.Activityname)
                        )
                        startActivity(intent)
                    } catch (e: Exception) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(activiyname)
                            )
                        )
                    }
                }

                MyLocationListener(this)

                AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
//        viewModel.validateAccount(AppUtils2.mobileno)
                customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
                pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()

                Handler(Looper.getMainLooper()).postDelayed({
                    checkUserStatus()
                }, 3000)


            viewModelss.validateAccounts(AppUtils2.mobileno, this)


            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->

                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                token = task.result

                Log.e("Token", token.toString())

//            var clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clipData = ClipData.newPlainText("text", token)
//            clipboardManager.setPrimaryClip(clipData)

            })

            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.getNotificationtoken(token.toString(), this, AppUtils2.mobileno)
            }, 500)

            binding.addFab.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance()).commitAllowingStateLoss();

//        binding.bottomheadertext.text=AppUtils2.order_number

            binding.bottomNavigation.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.nav_home -> {
                        if(AppUtils2.isNetworkAvailable(this)==true){

                        }else{
                            DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                            Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                        }
                        binding.addFab.visibility = View.VISIBLE
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment.newInstance())
                            .addToBackStack("HomeFragment").commitAllowingStateLoss()
                        true
                    }
                    R.id.nav_bookings -> {
                        if(AppUtils2.isNetworkAvailable(this)==true){

                        }else{
                            DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                            Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                        }
                        binding.addFab.visibility = View.GONE
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ProductFragment.newInstance())
                            .addToBackStack("HomeFragment").commitAllowingStateLoss()
                        true
                    }
                    R.id.nav_account -> {
                        if(AppUtils2.isNetworkAvailable(this)==true){

                        }else{
                            DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                            Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                        }
                        binding.addFab.visibility = View.GONE
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, AccountFragment.newInstance())
                            .addToBackStack("AccountFragment").commitAllowingStateLoss()
                        true
                    }
                    R.id.nav_cart -> {
                        if(AppUtils2.isNetworkAvailable(this)==true){

                        }else{
                            DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                            Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, HomeFragment.newInstance()).addToBackStack("Tag")
                            .commitAllowingStateLoss()

                        true
                    }
                    R.id.nav_orders -> {
                        binding.addFab.visibility = View.GONE
                        AppUtils2.fromdasboardmenu = false
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, PestServiceFragment.newInstance())
                            .addToBackStack("PestServiceFragment").commitAllowingStateLoss()
                        true
                    }
                    else -> false
                }
            }
            binding.bottomNavigation.selectedItemId = R.id.nav_home

                binding.addFab.setOnClickListener {
                    showLeadDialog()
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    getLeadMethod()
                }, 1500)


            } else {
                DesignToast.makeText(this,"Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG)
//                    .show()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }

}