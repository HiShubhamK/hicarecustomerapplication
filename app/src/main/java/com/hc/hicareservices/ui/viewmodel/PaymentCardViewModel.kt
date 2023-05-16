package com.hc.hicareservices.ui.viewmodel


// on below line we are creating a modal class.
data class PaymentCardViewModel(
    // we are creating a modal class with 2 member
    // one is course name as string and
    // other course img as int.
    val title: String,
    val description: String,
    val courseImg: Int,
    val ButtonTitle:String,
    val isButtonCancel:Boolean

)

