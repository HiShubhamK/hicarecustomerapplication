package com.ab.hicareservices.ui.handler

interface onSlotclick {
    fun onSlotItemclicked(
        position: Int,
        Pincode: String,
        Service_Code: String,
        Service_Date: String,
        Service_Subscription: String?,
        Unit: String?,
        Lat: String,
        Long: String,
        ServiceType: String,
        scheduledate: String
    )

}