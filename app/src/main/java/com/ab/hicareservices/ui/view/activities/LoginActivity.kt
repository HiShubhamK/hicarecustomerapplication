package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityLoginBinding
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.otpless.dto.OtplessResponse
import com.otpless.views.OtplessManager
import com.otpless.views.WhatsappLoginButton

class LoginActivity : AppCompatActivity() {
    private val RC_PENDING_INTENT = 123
    var checkloging=false

    private val requestPhoneNumberLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val credential: Credential? = data.getParcelableExtra(Credential.EXTRA_KEY)
                    val phoneNumber: String? = credential?.id
                    binding.mobileNoEt.setText(phoneNumber!!.substring(3,13))
                    val mobileNo = binding.mobileNoEt.text.toString()
                    Log.d("Logincheckmobileno",mobileNo)
                    if (binding.mobileNoEt.text.toString().equals("0000000000")) {

                        DesignToast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

                    } else if (mobileNo.length != 10) {

                        DesignToast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

                    } else {
                        progressDialog.show()
                        binding.signInBtn.isEnabled = false
                        getOtp(mobileNo, progressDialog)
                    }
                }
            } else {

                checkloging=true

//                Toast.makeText(this@LoginActivity,"Failed",Toast.LENGTH_LONG).show()
            }
        }

    lateinit var binding: ActivityLoginBinding
    private val viewModel: OtpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var data: String = ""
    val REQUEST_CODE_PERMISSIONS =101
    val LOCATION_PERMISSION_REQUEST_CODE=1001
    private val RC_HINT = 123

    private val RESOLVE_HINT = 1001
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        googleApiClient = GoogleApiClient.Builder(this@LoginActivity)
            .addApi(Auth.CREDENTIALS_API)
            .build()

        AppUtils2.checkloging= SharedPreferenceUtil.getData(this@LoginActivity, "getchecklogindata", false) as Boolean



        if (checkLocationPermissions()) {
//            requestHint(this.googleApiClient!!)

            // Permissions are already granted, enable location
            enableLoc()
//            Toast.makeText(this,"Tost last 20",Toast.LENGTH_LONG).show()
            requestHint(this.googleApiClient!!)
        } else {
            // Request location permissions
            requestLocationPermission()
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


                DesignToast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_LONG).show()

            } else if (mobileNo.length != 10) {

                DesignToast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

                //                binding.mobileNoEt.setError("Invalid Phone Number")
                return@setOnClickListener
            } else {
                progressDialog.show()
                binding.signInBtn.isEnabled = false
                getOtp(mobileNo, progressDialog)
            }
        }

    }
//   private val sdkCallback: ITrueCallback = object : ITrueCallback {
//        override fun onSuccessProfileShared(trueProfile: TrueProfile) {
//            val userName = trueProfile.firstName // Retrieve the user's first name
//            val userNumber = trueProfile.phoneNumber // Retrieve the user's phone number
//
//            // Proceed with using the obtained user details
//            // For example:
//            Log.e("TruecallerProfile", "User Name: $userName, Phone Number: $userNumber")
////            Toast.makeText(this@LoginActivity, "User Name: $userName, Phone Number: $userNumber", Toast.LENGTH_LONG).show()
//            DesignToast.makeText(this@LoginActivity, "User Name: $userName, Phone Number: $userNumber", Toast.LENGTH_LONG, DesignToast.TYPE_SUCCESS).show();
//
//            // Perform actions based on this information
//        }
//
//        override fun onFailureProfileShared(p0: TrueError) {
////            DesignToast.makeText(this@LoginActivity, p0?.errorType.toString(), Toast.LENGTH_LONG, DesignToast.TYPE_ERROR).show();
//
//
//        }
//
//        override fun onVerificationRequired(p0: TrueError?) {
////            DesignToast.makeText(this@LoginActivity, p0?.errorType.toString(), Toast.LENGTH_LONG, DesignToast.TYPE_ERROR).show();
//
//        }
//
//        // ... (Other methods like onFailureProfileShared and onVerificationRequired)
//    }
    fun verify(v:View){

    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun requestHint(googleApiClient: GoogleApiClient) {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intentSender = Auth.CredentialsApi.getHintPickerIntent(
            googleApiClient, hintRequest
        ).intentSender

        val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()

        try {
            requestPhoneNumberLauncher.launch(intentSenderRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
//        TruecallerSDK.clear();
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == TruecallerSDK.SHARE_PROFILE_REQUEST_CODE) {
//            TruecallerSDK.getInstance()
//                .onActivityResultObtained(this, requestCode, resultCode, data)
//        }
    }

    private fun checkLocationPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val notification = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationPermission && notification

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
                if(AppUtils2.checkloging==false) {
                    requestHint(this.googleApiClient!!)
                    SharedPreferenceUtil.setData(this@LoginActivity, "getchecklogindata", true)
//                    Toast.makeText(this,"Tost last 2",Toast.LENGTH_LONG).show()
                }else{

                }
            } else {
                requestHint(this.googleApiClient!!)
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

                    DesignToast.makeText(this, it.responseMessage.toString(), Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                    Toast.makeText(this, it.responseMessage.toString(), Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            })
            viewModel.getOtp(mobileNo)
        } else {
            binding.signInBtn.isEnabled = true
            progressDialog.dismiss()

            DesignToast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
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



}



