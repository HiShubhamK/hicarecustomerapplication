package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import io.realm.RealmResults


//import com.truecaller.android.sdk.ITrueCallback
//import com.truecaller.android.sdk.TrueError
//import com.truecaller.android.sdk.TrueProfile
//import com.truecaller.android.sdk.TruecallerSDK
//import com.truecaller.android.sdk.TruecallerSdkScope


class LoginActivity : AppCompatActivity() {

    private val RC_PENDING_INTENT = 123

    private val requestPhoneNumberLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val credential: Credential? = data.getParcelableExtra(Credential.EXTRA_KEY)
                    val phoneNumber: String? = credential?.id
                    Toast.makeText(this@LoginActivity, phoneNumber, Toast.LENGTH_LONG).show()
                    binding.mobileNoEt.setText(phoneNumber!!.substring(3,13))
                    val mobileNo = binding.mobileNoEt.text.toString()
                    Log.d("Logincheckmobileno",mobileNo)
                    if (binding.mobileNoEt.text.toString().equals("0000000000")) {
                        Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_LONG)
                            .show()
                    } else if (mobileNo.length != 10) {
                        Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        progressDialog.show()
                        binding.signInBtn.isEnabled = false
                        getOtp(mobileNo, progressDialog)
                    }
                }
            } else {
//                Toast.makeText(this@LoginActivity,"Failed",Toast.LENGTH_LONG).show()
            }
        }


    lateinit var binding: ActivityLoginBinding
    private val viewModel: OtpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var data: String = ""
    val REQUEST_CODE_PERMISSIONS = 101
    val LOCATION_PERMISSION_REQUEST_CODE = 1001

    private val RC_HINT = 123

    private val RESOLVE_HINT = 1001
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




        if (checkLocationPermissions()) {
            // Permissions are already granted, enable location
            enableLoc()
        } else {
            // Request location permissions
            requestLocationPermission()
        }

        googleApiClient = GoogleApiClient.Builder(this@LoginActivity)
            .addApi(Auth.CREDENTIALS_API)
            .build()

        binding.mobileNoEt.setOnClickListener {
            requestHint(googleApiClient!!)
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

}





