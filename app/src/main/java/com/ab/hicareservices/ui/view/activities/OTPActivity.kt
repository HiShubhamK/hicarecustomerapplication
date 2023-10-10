package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityOtpactivityBinding
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
class OTPActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        mobileNo = intent.getStringExtra("mobileNo").toString()
//        mOtp = intent.getStringExtra("otp").toString()
        binding.mobileNoTv.text = "$mobileNo"

        AppUtils2.mobileno = mobileNo

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        viewModel.otpResponse.observe(this, Observer {
            if (it.isSuccess == true) {
                mOtp=it.data.toString()
            } else {
                Toast.makeText(this, it.responseMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getOtp(mobileNo)

        startCounter()
        binding.resendCodeTv.setOnClickListener {
            if (binding.resendCodeTv.text == "Resend code") {
                resendOtp(mobileNo)
                //startCounter()
            }
        }
        binding.backIv.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

//        if(AppUtils2.isNetworkAvailable(this)==true){

        binding.continueBtn.setOnClickListener {

            if (AppUtils2.isNetworkAvailable(this) == true) {
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
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG)
                    .show()
            }


        }
//        }else{
//            Toast.makeText(this,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show()
//        }

    }

    private fun validateAccount(mobileNo: String) {

        viewModel.validateAccounts(mobileNo, this)
    }

    private fun resendOtp(mobileNo: String) {
        if (AppUtils2.isNetworkAvailable(this) == true) {
            viewModel.otpResponse.observe(this) {
                if (it.isSuccess == true) {
//                binding.resentSuccessTv.visibility = View.VISIBLE
                    mOtp = it.data.toString()
                    startCounter()
                } else {
                    binding.resentSuccessTv.visibility = View.GONE
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.getOtp(mobileNo)
        } else {
            Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show()
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

    //    override fun onSuccess(data: String) {
//        binding.continueBtn.isEnabled = true
//        progressDialog.dismiss()
//        if (data != "") {
//            AppUtils2.TOKEN = data
//            SharedPreferenceUtil.setData(this, "bToken", data)
//            SharedPreferenceUtil.setData(this, "mobileNo", mobileNo)
//            SharedPreferenceUtil.setData(this, "phoneNo", mobileNo)
//            SharedPreferenceUtil.setData(this, "IsLogin", true)
//
////            takePermissionForLocation()
//
//            binding.otpView.showSuccess()
//            progressDialog.dismiss()
//            val home = Intent(this, HomeActivity::class.java)
//            startActivity(home)
//            finish()
//        }else{
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    override fun onError(message: String) {
//        binding.continueBtn.isEnabled = true
//        progressDialog.dismiss()
//        Toast.makeText(this, "Authorization Error", Toast.LENGTH_SHORT).show()
//    }
    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()

    }
}
