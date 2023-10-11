package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityLoginBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.otpless.dto.OtplessResponse
import com.otpless.views.OtplessManager
import com.otpless.views.WhatsappLoginButton
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val viewModel: OtpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var data: String = ""
    val REQUEST_CODE_PERMISSIONS = 101
    var client: FusedLocationProviderClient? = null
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        MyLocationListener(this)

        client = LocationServices.getFusedLocationProviderClient(this)


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // When permission is granted
            // Call method
            getCurrentLocations()

        } else {
//            Toast.makeText(requireActivity(),"Not Ok",Toast.LENGTH_LONG).show()

            // When permission is not granted
            // Call method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    100
                )
            }
        }

        if (available("com.whatsapp")) {
            binding.lnrTitle1.visibility=View.VISIBLE
            binding.whatsappLogin.visibility=View.VISIBLE
        }
        else {

            binding.lnrTitle1.visibility=View.GONE
            binding.whatsappLogin.visibility=View.GONE
        }

        checkUserStatus()

        OtplessManager.getInstance().init(this)

        val button = findViewById<View>(R.id.whatsapp_login) as WhatsappLoginButton
        button.setResultCallback { data: OtplessResponse ->
            if (data?.waId != null) {
                val waid = data.waId
                getwhatapplogin(waid, progressDialog)
            }
        }

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.signInBtn.setOnClickListener {
            val mobileNo = binding.mobileNoEt.text.toString()
            if (binding.mobileNoEt.text.toString().equals("0000000000")) {
                Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show()
            } else if (mobileNo.length != 10) {
                Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show()
//                binding.mobileNoEt.setError("Invalid Phone Number")
                return@setOnClickListener
            } else {
                progressDialog.show()
                binding.signInBtn.isEnabled = false
                getOtp(mobileNo, progressDialog)
            }
        }
    }


    private fun getwhatapplogin(waid: String?, progressDialog: ProgressDialog) {
        viewModel.whatsResponse.observe(this, Observer {
            if (it.IsSuccess == true) {
                data = it.Data?.waNumber?.substring(2).toString()

                viewModel.validateAccounts(data, this)

                SharedPreferenceUtil.setData(this, "mobileNo", data)
                SharedPreferenceUtil.setData(this, "phoneNo", data)
                SharedPreferenceUtil.setData(this, "IsLogin", true)
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        viewModel.getWhatappToken(waid.toString())
    }

    override fun onResume() {
        //      binding.signInBtn.isEnabled = true
        super.onResume()
    }

    private fun getOtp(mobileNo: String, progressDialog: ProgressDialog) {
        if (AppUtils2.isNetworkAvailable(this) == true) {
            viewModel.otpResponse.observe(this, Observer {
                if (it.isSuccess == true) {
                    progressDialog.dismiss()
//                    Toast.makeText(this, it.responseMessage.toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, OTPActivity::class.java)
                    intent.putExtra("mobileNo", mobileNo)
                    intent.putExtra("otp", it.data)
                    startActivity(intent)
                    finish()
                } else {
                    binding.signInBtn.isEnabled = true
                    Toast.makeText(this, it.responseMessage.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            })
            viewModel.getOtp(mobileNo)
        } else {
            binding.signInBtn.isEnabled = true
            progressDialog.dismiss()
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
        }
        progressDialog.dismiss()
    }

    private fun checkUserStatus() {
        val mobileNo = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
//        AppUtils2.TOKEN = SharedPreferenceUtil.getData(this, "bToken", "").toString()
        if (mobileNo != "-1") {
            val i = Intent(this, HomeActivity::class.java)
            //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocations() {
        try {
            val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            // Check condition
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
            ) {
                try {
                    client!!.lastLocation.addOnCompleteListener(
                        object : OnCompleteListener<Location?> {

                            override fun onComplete(
                                task: Task<Location?>
                            ) {


                                // Initialize location
                                val location: Location? = task.result
                                // Check condition
                                if (location != null) {
                                    // When location result is not
                                    // null set latitude
//                            Toasty.success(
//                                this@Checkin_Out_Home,
//                                "Lat: " + location.getLatitude() + "long: " + location.getLongitude()
//                            )
                                    lat = location.latitude.toString()
                                    longg = location.longitude.toString()

//                            tvLatitude.setText(java.lang.String.valueOf(location.getLatitude()))
//                            // set longitude
//                            tvLongitude.setText(java.lang.String.valueOf(location.getLongitude()))
                                } else {
                                    // When location result is null
                                    // initialize location request
                                    val locationRequest =
                                        LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(10000).setFastestInterval(1000)
                                            .setNumUpdates(1)

                                    // Initialize location call back
                                    val locationCallback: LocationCallback =
                                        object : LocationCallback() {
                                            fun voidonLocationResult(
                                                locationResult: LocationResult
                                            ) {
                                                // Initialize
                                                // location
                                                val location1: Location = locationResult.lastLocation
                                                // Set latitude
//                                    Toasty.success(
//                                        this@Checkin_Out_Home,
//                                        "Lat: " + location1.getLatitude() + "long: " + location1.getLongitude()
//                                    )
                                                lastlat = location1.latitude.toString()
                                                lastlongg = location1.longitude.toString()
                                                val mGeocoder =
                                                    Geocoder(this@LoginActivity, Locale.getDefault())
                                                if (mGeocoder != null) {
                                                    var postalcode: MutableList<Address>? =
                                                        mGeocoder.getFromLocation(
                                                            lastlat!!.toDouble(),
                                                            lastlongg!!.toDouble(),
                                                            5
                                                        )
                                                    if (postalcode != null && postalcode.size > 0) {
                                                        for (i in 0 until postalcode.size) {
                                                            AppUtils2.pincode =
                                                                postalcode.get(i).postalCode.toString()
                                                            SharedPreferenceUtil.setData(
                                                                this@LoginActivity,
                                                                "pincode",
                                                                postalcode.get(i).postalCode.toString()
                                                            )
                                                            break
                                                        }
                                                    }
                                                }
//                                    tvLatitude.setText(java.lang.String.valueOf(location1.getLatitude()))
//                                    // Set longitude
//                                    tvLongitude.setText(java.lang.String.valueOf(location1.getLongitude()))
                                            }
                                        }

                                    // Request location updates
                                    if (ActivityCompat.checkSelfPermission(
                                            this@LoginActivity,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                            this@LoginActivity,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return
                                    }
                                    Looper.myLooper()?.let {
                                        client!!.requestLocationUpdates(
                                            locationRequest,
                                            locationCallback,
                                            it
                                        )
                                    }
                                }
                            }
                        })
                }catch (e:Exception){
                    e.printStackTrace()
                }
                // When location service is enabled
                // Get last location

            } else {
                enableLoc()

//                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this,
                                REQUEST_CODE_PERMISSIONS
                            )

                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    // if app is available or not
    private fun available(name: String): Boolean {
        var available = true
        try {
            // check if available
            packageManager.getPackageInfo(name, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            // if not available set
            // available as false
            available = false
        }
        return available
    }

}