package com.ab.hicareservices.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SMSListener:BroadcastReceiver() {
    private val TAG = "SmsBroadcastReceiver"
    var otpReceiveInterface: OtpReceivedInterface? = null

    fun setOnOtpListeners(otpReceiveInterface: OtpReceivedInterface?) {
        this.otpReceiveInterface = otpReceiveInterface
    }

    override fun onReceive(context: Context?, intent: Intent) {
        try {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
                when (status!!.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get SMS message contents
                        val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                        // Extract one-time code as per the message
                        if (otpReceiveInterface != null) {
//                        String otp = message.replace("<#> is Your HiCare verification code.", "");
                            val otp = message!!.substring(4, 10)
                            otpReceiveInterface!!.onOtpReceived(otp)
                        }
                    }

                    CommonStatusCodes.TIMEOUT ->                         // Waiting for SMS timed out (5 minutes)
                        //Send your message to the respected activity
                        if (otpReceiveInterface != null) {
                            otpReceiveInterface!!.onOtpTimeout()
                        }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}