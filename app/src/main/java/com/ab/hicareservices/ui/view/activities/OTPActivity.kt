package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
            progressDialog.show()
            if (binding.otpView.otp.toString().equals("")) {
                Toast.makeText(this, "Please enter code", Toast.LENGTH_LONG).show()
            } else if (mOtp.equals(binding.otpView.otp.toString())) {

                validateAccount(mobileNo)
                progressDialog.dismiss()
                SharedPreferenceUtil.setData(this, "mobileNo", mobileNo)
                SharedPreferenceUtil.setData(this, "phoneNo", mobileNo)
                SharedPreferenceUtil.setData(this, "IsLogin", true)

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                progressDialog.dismiss()
                Toast.makeText(this, "Invalid Otp", Toast.LENGTH_LONG).show()
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
                mOtp=""
                mOtp = it.data.toString()
                Toast.makeText(this, "Otp Resend Successfully", Toast.LENGTH_SHORT).show()
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
                var millisUntilFinisheds=millisUntilFinished/1000
                binding.resendCodeTv.text = "Resend code in 00: " + millisUntilFinished / 1000


                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                binding.resendCodeTv.setTypeface(null, Typeface.BOLD)
                binding.resendCodeTv.text = "Resend code"
                mOtp=""
            }
        }.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)

    }

}
