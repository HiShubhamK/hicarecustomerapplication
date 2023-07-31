package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
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
import com.ab.hicareservices.databinding.ActivityOtpactivityBinding
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
import java.util.Locale


class OTPActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpactivityBinding
    private val viewModel: OtpViewModel by viewModels()
    var mobileNo = ""
    var mOtp = ""
    lateinit var progressDialog: ProgressDialog
    var token: String? = null
    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    var client: FusedLocationProviderClient? = null

    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""
    val REQUEST_CODE_PERMISSIONS = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        client = LocationServices
            .getFusedLocationProviderClient(
                this@OTPActivity
            )

        if (ContextCompat.checkSelfPermission(
                this@OTPActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this@OTPActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            // When permission is granted
            // Call method
            getCurrentLocations()

        } else {
//            Toast.makeText( this@OTPActivity,"Not Ok",Toast.LENGTH_LONG).show()

            // When permission is not granted
            // Call method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    100
                )
            }
        }

        val intent = intent
        mobileNo = intent.getStringExtra("mobileNo").toString()
        mOtp = intent.getStringExtra("otp").toString()
        binding.mobileNoTv.text = "$mobileNo"

//        binding.otpView.setOTP(mOtp)

        AppUtils2.mobileno = mobileNo

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.backIvs.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        startCounter()
        binding.resendCodeTv.setOnClickListener {
            if (binding.resendCodeTv.text == "Resend code") {
                resendOtp(mobileNo)
                //startCounter()
            }
        }
        binding.backIv.setOnClickListener {
            finish()
        }
        binding.continueBtn.setOnClickListener {

            if (binding.otpView.otp.toString().equals("")) {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
            } else if (mOtp.equals(binding.otpView.otp.toString())) {
                viewModel.validateResponses.observe(this, Observer {
                    progressDialog.show()
                    if (it.IsSuccess == true) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            AppUtils2.TOKEN = it.Data?.Token.toString()
                            AppUtils2.customerid = it?.Data?.ProductCustomerData?.Id.toString()
                            SharedPreferenceUtil.setData(this, "bToken", it.Data?.Token.toString())
                            if (it?.Data?.PestCustomerData?.BillingPostalCode == null) {
                                SharedPreferenceUtil.setData(this, "pincode", "")
                            } else {
                                SharedPreferenceUtil.setData(
                                    this,
                                    "pincode",
                                    it?.Data?.PestCustomerData?.BillingPostalCode.toString()
                                )
                            }
                            SharedPreferenceUtil.setData(
                                this,
                                "customerid",
                                it?.Data?.ProductCustomerData?.Id.toString()
                            )
                            SharedPreferenceUtil.setData(
                                this,
                                "FirstName",
                                it?.Data?.ProductCustomerData?.FirstName.toString()
                            )
                            SharedPreferenceUtil.setData(
                                this,
                                "MobileNo",
                                it?.Data?.ProductCustomerData?.MobileNo.toString()
                            )
                            SharedPreferenceUtil.setData(
                                this,
                                "EMAIL",
                                it?.Data?.ProductCustomerData?.Email.toString()
                            )
                            progressDialog.dismiss()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 3000)


                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()

                        }, 3000)
                    }
                })

                validateAccount(mobileNo)
                SharedPreferenceUtil.setData(this, "mobileNo", mobileNo)
                SharedPreferenceUtil.setData(this, "phoneNo", mobileNo)
                SharedPreferenceUtil.setData(this, "IsLogin", true)

                progressDialog.dismiss()

            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateAccount(mobileNo: String) {

        viewModel.validateAccounts(mobileNo, this)
    }

    private fun resendOtp(mobileNo: String) {
        viewModel.otpResponse.observe(this) {
            if (it.isSuccess == true) {
//                binding.resentSuccessTv.visibility = View.VISIBLE
                binding.otpView.setOTP("")
                mOtp = ""
                mOtp = it.data.toString()
                Toast.makeText(this, "OTP Resent Successfully", Toast.LENGTH_SHORT).show()
                startCounter()
            } else {
                binding.resentSuccessTv.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getOtp(mobileNo)
    }

    private fun startCounter() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var millisUntilFinisheds = millisUntilFinished / 1000
                binding.resendCodeTv.text = "Resend code in 00: " + millisUntilFinished / 1000

            }

            override fun onFinish() {
                binding.resendCodeTv.setTypeface(null, Typeface.BOLD)
                binding.resendCodeTv.text = "Resend code"
                mOtp = ""
            }
        }.start()
    }
    @SuppressLint("MissingPermission")
    private fun getCurrentLocations() {
        try {
            val locationManager: LocationManager =
                getSystemService(LOCATION_SERVICE) as LocationManager
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
                                                    Geocoder( this@OTPActivity, Locale.getDefault())
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
                                                                this@OTPActivity,
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
                                             this@OTPActivity,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                             this@OTPActivity,
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
        val result = LocationServices.getSettingsClient( this@OTPActivity).checkLocationSettings(builder.build())
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
                                 this@OTPActivity,
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


    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

}
