package com.ab.hicareservices.ui.view.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityOtpactivityBinding
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppSignatureHelper
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.OtpReceivedInterface
import com.ab.hicareservices.utils.SMSListener
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

class OTPActivity : AppCompatActivity(),OtpReceivedInterface, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    lateinit var binding: ActivityOtpactivityBinding
    private val viewModel: OtpViewModel by viewModels()
    var mobileNo = ""
    var mOtp = ""
    lateinit var progressDialog: ProgressDialog
    var token: String? = null
    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    var lat = 0.0
    var lng = 0.0
    var mSmsBroadcastReceiver: SMSListener? = null
    var mGoogleApiClient: GoogleApiClient? = null


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        mobileNo = intent.getStringExtra("mobileNo").toString()
        mOtp = intent.getStringExtra("otp").toString()
        binding.mobileNoTv.text = "$mobileNo"

        AppUtils2.mobileno = mobileNo

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)


        val appSignatureHelper = AppSignatureHelper(this@OTPActivity)
        appSignatureHelper.appSignatures
        mSmsBroadcastReceiver = SMSListener()
        //set google api client for hint request
        //set google api client for hint request
        mGoogleApiClient = GoogleApiClient.Builder(this@OTPActivity)
            .addConnectionCallbacks(this@OTPActivity)
            .enableAutoManage(this@OTPActivity, this)
            .addApi(Auth.CREDENTIALS_API)
            .build()

        mSmsBroadcastReceiver!!.setOnOtpListeners(this@OTPActivity)
        val intentFilter = IntentFilter()
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(mSmsBroadcastReceiver,intentFilter)
        startSMSListener()

        startCounter()
        binding.resendCodeTv.setOnClickListener {
            if (binding.resendCodeTv.text == "Resend code") {
                resendOtp(mobileNo)
                //startCounter()
            }
        }
        binding.backIvs.setOnClickListener {
            val intent=Intent(this@OTPActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

//        if(AppUtils2.isNetworkAvailable(this)==true){

            binding.continueBtn.setOnClickListener {

                if(AppUtils2.isNetworkAvailable(this)==true){
                    progressDialog.show()
                    if (mOtp.equals(binding.otpView.otp.toString())) {
                        validateAccount(mobileNo)
                        Handler(Looper.getMainLooper()).postDelayed({
                            progressDialog.dismiss()
                            SharedPreferenceUtil.setData(this, "mobileNo", mobileNo)
                            SharedPreferenceUtil.setData(this, "phoneNo", mobileNo)
                            SharedPreferenceUtil.setData(this, "IsLogin", true)

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Enter valid otp", Toast.LENGTH_LONG).show()
                    }
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
                }

            }
    }

    private fun startSMSListener() {
        try {
            val mClient = SmsRetriever.getClient(this@OTPActivity)
            val mTask = mClient.startSmsRetriever()
            mTask.addOnSuccessListener { aVoid: Void? ->
                Log.e(
                    "TAG_OTP",
                    "SMS Retriever starts"
                )
            }
            mTask.addOnFailureListener { e: Exception? ->
                Log.e(
                    "TAG_OTP",
                    "Error"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validateAccount(mobileNo: String) {

        viewModel.validateAccounts(mobileNo, this)
    }

    private fun resendOtp(mobileNo: String) {
        if(AppUtils2.isNetworkAvailable(this)==true){
            viewModel.otpResponse.observe(this) {
                if (it.isSuccess == true) {
//                binding.resentSuccessTv.visibility = View.VISIBLE
                    mOtp = it.data.toString()
                    Toast.makeText(this, "OTP Sent Successfully!", Toast.LENGTH_SHORT).show()

                    startCounter()
                } else {
                    binding.resentSuccessTv.visibility = View.GONE
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.getOtp(mobileNo)
        }else{
            Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
        }
    }

    private fun startCounter() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCodeTv.text = "Resend code in 00: " + millisUntilFinished / 1000
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                binding.resendCodeTv.setTypeface(null, Typeface.BOLD)
                binding.resendCodeTv.text = "Resend code"
            }
        }.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent=Intent(this@OTPActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onOtpReceived(otp: String) {
//        Toast.makeText(this, otp.toString(), Toast.LENGTH_SHORT).show()
        binding.otpView.setOTP(otp)
        checkotp(otp)
    }

    override fun onOtpTimeout() {
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        mSmsBroadcastReceiver = null
//        unregisterReceiver(mSmsBroadcastReceiver)

    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private fun checkotp(toString: String) {
        Log.d("checkotp","Hello"+AppUtils2.otp)
        if (mOtp.equals(toString)) {
            Log.d("checkotp","Hello1")
            viewModel.validateResponses.observe(this, Observer {
                if (it.IsSuccess == true) {
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
                    val intent = Intent(this@OTPActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                }
            })
            viewModel.validateAccounts(mobileNo, this@OTPActivity)
            SharedPreferenceUtil.setData(this@OTPActivity, "mobileNo", mobileNo)
            SharedPreferenceUtil.setData(this@OTPActivity, "phoneNo", mobileNo)
            SharedPreferenceUtil.setData(this@OTPActivity, "IsLogin", true)

        }else{
            Log.d("checkotp","Failed")
        }
    }


}