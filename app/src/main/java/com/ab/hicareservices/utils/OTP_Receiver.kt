package com.ab.hicareservices.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.view.activities.OTPActivity
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import `in`.aabhasjindal.otptextview.OtpTextView

class OTP_Receiver : BroadcastReceiver() {

    fun setEditText(
        otpview: OtpTextView,
        otpActivity: OTPActivity,
        mOtp: String,
        mobileNo: String,
        viewModel: OtpViewModel
    ) {
        Companion.otpview = otpview
        Companion.otpActivity = otpActivity
        Companion.mOtp = mOtp
        Companion.mobileNo = mobileNo
        Companion.viewModel = viewModel
    }

    // OnReceive will keep trace when sms is been received in mobile
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context?, intent: Intent?) {
        //message will be holding complete sms that is received
        val messages: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in messages) {
            val msg: String = sms.getMessageBody()
            // here we are splitting the sms using " : " symbol
            val otp = msg.split(": ").toTypedArray()[0]
            Log.d("Otpreceiver",otp)
            otpview?.setOTP(otp.subSequence(4,8))
            checkotp(otp.subSequence(4,8).toString())
            AppUtils2.checkotptruefalse=true
            break
        }
    }

    private fun checkotp(toString: String) {
        Log.d("checkotp","Hello"+AppUtils2.otp)
        if (mOtp.equals(toString)) {
            Log.d("checkotp","Hello1")
            viewModel.validateResponses.observe(otpActivity, Observer {
                if (it.IsSuccess == true) {
                    AppUtils2.TOKEN = it.Data?.Token.toString()
                    AppUtils2.customerid = it?.Data?.ProductCustomerData?.Id.toString()
                    SharedPreferenceUtil.setData(otpActivity, "bToken", it.Data?.Token.toString())
                    if (it?.Data?.PestCustomerData?.BillingPostalCode == null) {
                        SharedPreferenceUtil.setData(otpActivity, "pincode", "")
                    } else {
                        SharedPreferenceUtil.setData(
                            otpActivity,
                            "pincode",
                            it?.Data?.PestCustomerData?.BillingPostalCode.toString()
                        )
                    }
                    SharedPreferenceUtil.setData(
                        otpActivity,
                        "customerid",
                        it?.Data?.ProductCustomerData?.Id.toString()
                    )
                    SharedPreferenceUtil.setData(
                        otpActivity,
                        "FirstName",
                        it?.Data?.ProductCustomerData?.FirstName.toString()
                    )
                    SharedPreferenceUtil.setData(
                        otpActivity,
                        "MobileNo",
                        it?.Data?.ProductCustomerData?.MobileNo.toString()
                    )
                    SharedPreferenceUtil.setData(
                        otpActivity,
                        "EMAIL",
                        it?.Data?.ProductCustomerData?.Email.toString()
                    )
                    val intent = Intent(otpActivity, HomeActivity::class.java)
                    otpActivity.startActivity(intent)
                    otpActivity.finish()

                } else {
                }
            })
            viewModel.validateAccounts(mobileNo, otpActivity)
            SharedPreferenceUtil.setData(otpActivity, "mobileNo", mobileNo)
            SharedPreferenceUtil.setData(otpActivity, "phoneNo", mobileNo)
            SharedPreferenceUtil.setData(otpActivity, "IsLogin", true)

        }else{
            Log.d("checkotp","Failed")
        }
    }

    companion object {
        private var otpview: OtpTextView? = null
        private lateinit var otpActivity: OTPActivity
        private var mOtp:String=""
        var getotp=""
        private var mobileNo:String=""
        private lateinit var viewModel: OtpViewModel
    }
}