package com.hc.hicareservices.ui.handler

import com.razorpay.PaymentData

interface PaymentListener {
    fun onPaymentSuccess(s: String?, response: PaymentData?)
    fun onPaymentError(p0: Int, p1: String?, response: PaymentData?)
}