package com.ab.hicareservices.utils

interface OtpReceivedInterface {
    fun onOtpReceived(otp: String?)
    fun onOtpTimeout()

}