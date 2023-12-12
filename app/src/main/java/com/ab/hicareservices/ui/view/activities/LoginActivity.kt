package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.otpless.dto.OtplessResponse
import com.otpless.views.OtplessManager
import com.otpless.views.WhatsappLoginButton
import com.truecaller.android.sdk.ITrueCallback
import com.truecaller.android.sdk.TrueError
import com.truecaller.android.sdk.TrueProfile
import com.truecaller.android.sdk.TruecallerSDK
import com.truecaller.android.sdk.TruecallerSdkScope


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val viewModel: OtpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var data: String = ""
    val REQUEST_CODE_PERMISSIONS =101
    val LOCATION_PERMISSION_REQUEST_CODE=1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val sdkCallback: ITrueCallback = object : ITrueCallback {
            override fun onSuccessProfileShared(trueProfile: TrueProfile) {
                AppUtils2.mobileno=trueProfile.phoneNumber
                SharedPreferenceUtil.setData(this@LoginActivity,"mobileNo",trueProfile.phoneNumber)
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()

            }
            override fun onFailureProfileShared(trueError: TrueError) {
                Toast.makeText(this@LoginActivity, ""+trueError.errorType, Toast.LENGTH_LONG).show()

                if (trueError.errorType == TrueError.ERROR_PROFILE_NOT_FOUND ) {

                    // Handle unauthorized partner error
                    // For example, display a message to the user or take corrective action
                    Log.e("TruecallerError", "Unauthorized partner error")
                    // Perform necessary actions like notifying the user or handling the error scenario
                } else {
                    // Handle other error types if needed
                    Log.e("TruecallerError", "Other error: ${trueError.errorType}")
                }

            }
            override fun onVerificationRequired(trueError: TrueError) {
                Toast.makeText(this@LoginActivity, ""+trueError.errorType, Toast.LENGTH_LONG).show()

            }
        }

        val trueScope = TruecallerSdkScope.Builder(this, sdkCallback)
            .consentMode(TruecallerSdkScope.CONSENT_MODE_BOTTOMSHEET)
            .buttonColor(Color.parseColor("#2bb77a"))
            .buttonTextColor(Color.WHITE)
            .loginTextPrefix(TruecallerSdkScope.LOGIN_TEXT_PREFIX_TO_GET_STARTED)
            .loginTextSuffix(TruecallerSdkScope.LOGIN_TEXT_SUFFIX_PLEASE_VERIFY_MOBILE_NO)
            .ctaTextPrefix(TruecallerSdkScope.CTA_TEXT_PREFIX_USE)
            .buttonShapeOptions(TruecallerSdkScope.BUTTON_SHAPE_ROUNDED)
            .privacyPolicyUrl("http://connect.hicare.in/privacy_policy.html")
            .termsOfServiceUrl("https://hicare.in/terms-conditions")
            .footerType(TruecallerSdkScope.FOOTER_TYPE_NONE)
            .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_LOG_IN)
            .sdkOptions(TruecallerSdkScope.SDK_OPTION_WITHOUT_OTP)
            .build()

        TruecallerSDK.init(trueScope)

        if (TruecallerSDK.getInstance().isUsable){
            TruecallerSDK.getInstance().getUserProfile(this)

        }else{
//            try {
//                TruecallerSDK.getInstance().requestVerification(
//                    "IN",
//                    "",
//                    apiCallback,
//                    this
//                )
//            } catch (e: RuntimeException) {
//                Log.i("TAG", e.message!!)
//            }

        }







        if (checkLocationPermissions()) {
            // Permissions are already granted, enable location
            enableLoc()
        } else {
            // Request location permissions
            requestLocationPermission()
        }


//
//        if (ContextCompat.checkSelfPermission(
//                this@LoginActivity,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(
//                this@LoginActivity,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(
//                this@LoginActivity,
//                Manifest.permission.POST_NOTIFICATIONS
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // When permission is granted
//            // Call method
//            enableLoc()
//
//        } else {
//            // When permission is not granted
//            // Call method
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.POST_NOTIFICATIONS
//                    ),
//                    100
//                )
//                enableLoc()
//            }
//
//        }







//        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
//        var gps_enabled = false
//        var network_enabled = false
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        } catch (ex: Exception) {
//        }
//
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        } catch (ex: Exception) {
//        }
//        if (!gps_enabled && !network_enabled) {
//            if(AppUtils2.ISChecklocationpermission==true){
//
//            }else{
//                enableLoc()
//            }
//        }else if (!gps_enabled){
//            if(AppUtils2.ISChecklocationpermission==true){
//
//            }else{
//                enableLoc()
//            }
//        }else if(!network_enabled){
//            if(AppUtils2.ISChecklocationpermission==true){
//
//            }else{
//                enableLoc()
//            }
//        }else{
//            enableLoc()
//        }

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
    override fun onDestroy() {
        super.onDestroy()
        TruecallerSDK.clear();
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TruecallerSDK.SHARE_PROFILE_REQUEST_CODE) {
            TruecallerSDK.getInstance()
                .onActivityResultObtained(this, requestCode, resultCode, data)
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location
                enableLoc()
            } else {
                // Permission denied, handle accordingly (e.g., show a message, disable location features)
            }
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
                                this@LoginActivity,
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



    private fun getwhatapplogin(waid: String?, progressDialog: ProgressDialog) {
        viewModel.getWhatappToken(waid.toString())
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
    }

    override fun onResume() {
        //      binding.signInBtn.isEnabled = true
        super.onResume()
    }
//    private fun fetchTruecallerProfile(phoneNumber: String) {
//        TruecallerSDK.getInstance().getUserProfile(this@LoginActivity, object : ITrueCallback {
//            override fun onSuccessProfileShared(trueProfile: TrueProfile) {
//                // Handle successful profile retrieval
//                // Access profile details via trueProfile object
//            }
//
//            override fun onFailureProfileShared(trueError: TrueError) {
//                // Handle failure to fetch profile
//                // Check trueError.getErrorType() for the error type
//            }
//
//            override fun onVerificationRequired(p0: TrueError?) {
//            }
//        })
//    }

    private fun getOtp(mobileNo: String, progressDialog: ProgressDialog) {
        if (AppUtils2.isNetworkAvailable(this) == true) {
            viewModel.otpResponse.observe(this, Observer {
                if (it.isSuccess == true) {
                    progressDialog.dismiss()
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

//    private fun isNetworkAvailable(): Boolean {
//        val connectivityManager =
//            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
//        return activeNetwork?.isConnected == true
//    }


}



