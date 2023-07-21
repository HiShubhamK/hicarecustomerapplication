package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityLoginBinding
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.otpless.dto.OtplessResponse
import com.otpless.views.OtplessManager
import com.otpless.views.WhatsappLoginButton

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val viewModel: OtpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var data:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkUserStatus()

        OtplessManager.getInstance().init(this)

        val button = findViewById<View>(R.id.whatsapp_login) as WhatsappLoginButton
        button.setResultCallback { data: OtplessResponse ->
            if (data?.waId != null) {
                val waid = data.waId
                getwhatapplogin(waid,progressDialog)
            }
        }

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.signInBtn.setOnClickListener {
            val mobileNo = binding.mobileNoEt.text.toString()
            if(binding.mobileNoEt.text.toString().equals("0000000000")){
                Toast.makeText(this,"Please Enter Valid Mobile Number",Toast.LENGTH_LONG).show()
            }else if (mobileNo.length != 10){
                Toast.makeText(this,"Please Enter Valid Mobile Number",Toast.LENGTH_LONG).show()
//                binding.mobileNoEt.setError("Invalid Phone Number")
                return@setOnClickListener
            }else{
                progressDialog.show()
                binding.signInBtn.isEnabled = false
                getOtp(mobileNo,progressDialog)
            }
        }
    }

    private fun getwhatapplogin(waid: String?, progressDialog: ProgressDialog) {
        viewModel.getWhatappToken(waid.toString())
        viewModel.whatsResponse.observe(this,Observer{
            if(it.IsSuccess==true){
                data= it.Data?.waNumber?.substring(2).toString()


                viewModel.validateResponses.observe(this, Observer {
                    if(it.IsSuccess==true){
                        progressDialog.dismiss()
                        AppUtils2.TOKEN=it.Data?.Token.toString()
                        AppUtils2.customerid= it?.Data?.ProductCustomerData?.Id.toString()
                        SharedPreferenceUtil.setData(this, "bToken",it.Data?.Token.toString())
                        if(it?.Data?.PestCustomerData?.BillingPostalCode==null){
                            SharedPreferenceUtil.setData(this, "pincode","")
                        }else{
                            SharedPreferenceUtil.setData(this, "pincode",it?.Data?.PestCustomerData?.BillingPostalCode.toString())
                        }
                        SharedPreferenceUtil.setData(this, "customerid",it?.Data?.ProductCustomerData?.Id.toString())
                        SharedPreferenceUtil.setData(this, "FirstName",it?.Data?.ProductCustomerData?.FirstName.toString())
                        SharedPreferenceUtil.setData(this, "MobileNo",it?.Data?.ProductCustomerData?.MobileNo.toString())
                        SharedPreferenceUtil.setData(this, "EMAIL",it?.Data?.ProductCustomerData?.Email.toString())

                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        progressDialog.dismiss()
                    }
                })


                viewModel.validateAccounts(data,this)

                SharedPreferenceUtil.setData(this, "mobileNo", data)
                SharedPreferenceUtil.setData(this, "phoneNo", data)
                SharedPreferenceUtil.setData(this, "IsLogin", true)
                progressDialog.dismiss()
//                val intent=Intent(this,HomeActivity::class.java)
//                startActivity(intent)
//                finish()
            }
        })
    }

    override fun onResume() {
  //      binding.signInBtn.isEnabled = true
        super.onResume()
    }

    private fun getOtp(mobileNo: String, progressDialog: ProgressDialog){
        viewModel.getOtp(mobileNo)
        viewModel.otpResponse.observe(this, Observer {
            if (it.isSuccess == true) {
                progressDialog.dismiss()
                val intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("mobileNo", mobileNo)
                intent.putExtra("otp", it.data)
                startActivity(intent)
                finishAffinity()
            }else{
            }
        })
    }

    private fun checkUserStatus(){
        val mobileNo = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
//        AppUtils2.TOKEN = SharedPreferenceUtil.getData(this, "bToken", "").toString()
        if (mobileNo != "-1"){
            val i = Intent(this, HomeActivity::class.java)
            //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
    }
}