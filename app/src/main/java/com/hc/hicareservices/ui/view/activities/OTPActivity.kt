package com.hc.hicareservices.ui.view.activities

import `in`.aabhasjindal.otptextview.OTPListener
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hc.hicareservices.data.SharedPreferenceUtil
import com.hc.hicareservices.data.api.IRetrofit
import com.hc.hicareservices.data.repository.MainRepository
import com.hc.hicareservices.databinding.ActivityOtpactivityBinding
import com.hc.hicareservices.ui.handler.ValidateAccountListener
import com.hc.hicareservices.ui.viewmodel.OtpViewModel
import com.hc.hicareservices.utils.AppUtils2


class OTPActivity : AppCompatActivity(), ValidateAccountListener {

    lateinit var binding: ActivityOtpactivityBinding
    private val viewModel: OtpViewModel by viewModels()
    var mobileNo = ""
    var mOtp = ""
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressDialog = ProgressDialog(this).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
        //viewModel = ViewModelProvider(this, OtpViewModelFactory(MainRepository(api))).get(OtpViewModel::class.java)
        viewModel.validateAccountListener = this
        val intent = intent
        mobileNo = intent.getStringExtra("mobileNo").toString()
        mOtp = intent.getStringExtra("otp").toString()
        binding.mobileNoTv.text = "$mobileNo"

        AppUtils2.mobileno=mobileNo

        startCounter()
        binding.resendCodeTv.setOnClickListener {
            if (binding.resendCodeTv.text == "Resend code"){
                resendOtp(mobileNo)
                //startCounter()
            }
        }
        binding.backIv.setOnClickListener {
            finish()
        }
        binding.otpView.otpListener = object : OTPListener{
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String?) {
                if (otp == mOtp){
                    binding.otpView.showSuccess()
                }else{
                    binding.otpView.showError()
                }
            }
        }
        binding.continueBtn.setOnClickListener {
            binding.continueBtn.isEnabled = false
            progressDialog.setMessage("Authorizing user...")
            progressDialog.show()
            val otp = binding.otpView.otp.toString().trim()
            if (otp.length != 4){
                binding.continueBtn.isEnabled = true
                progressDialog.dismiss()
                binding.otpView.showError()
                return@setOnClickListener
            }
            if (otp == mOtp){
                validateAccount(mobileNo)
            }
        }

    }

    private fun validateAccount(mobileNo: String){
        viewModel.validateAccount(mobileNo)
    }

    private fun resendOtp(mobileNo: String){
        viewModel.otpResponse.observe(this) {
            if (it.isSuccess == true) {
                binding.resentSuccessTv.visibility = View.VISIBLE
                mOtp = it.data.toString()
                startCounter()
            } else {
                binding.resentSuccessTv.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getOtp(mobileNo)
    }

    private fun startCounter(){
        object : CountDownTimer(30000, 1000) {
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

    override fun onSuccess(data: String) {
        binding.continueBtn.isEnabled = true
        progressDialog.dismiss()
        if (data != "") {
            AppUtils2.TOKEN = data
            SharedPreferenceUtil.setData(this, "bToken", data)
            SharedPreferenceUtil.setData(this, "mobileNo", mobileNo)
            binding.otpView.showSuccess()
            progressDialog.dismiss()
            val home = Intent(this, HomeActivity::class.java)
            startActivity(home)
            finish()
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onError(message: String) {
        binding.continueBtn.isEnabled = true
        progressDialog.dismiss()
        Toast.makeText(this, "Authorization Error", Toast.LENGTH_SHORT).show()
    }
}