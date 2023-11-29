package com.ab.hicareservices.utils

data class OrderPayment(
    var PaymentMode: String = "",
    var PaymentMethod: String = "",
    var Amount: String = "",
    var TransactionId: String = "",
    var Type: String = ""
)
