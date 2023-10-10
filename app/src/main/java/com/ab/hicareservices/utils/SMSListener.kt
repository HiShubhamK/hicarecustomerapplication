package com.ab.hicareservices.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ab.hicareservices.ui.view.activities.OTPActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SMSListener : BroadcastReceiver() {
    var otpReceiveInterface: OtpReceivedInterface? = null
    fun setOnOtpListeners(otpReceiveInterface: OTPActivity) {
        this.otpReceiveInterface = otpReceiveInterface
    }
    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS ->   {
                    var  message: String?= extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    otpReceiveInterface?.onOtpReceived(message)
                }        // Get SMS message contents
                CommonStatusCodes.TIMEOUT -> {}
            }
        }
    }



    companion object {
        private const val TAG = "SmsBroadcastReceiver"
    }
}
