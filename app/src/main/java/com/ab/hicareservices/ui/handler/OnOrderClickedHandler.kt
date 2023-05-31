package com.ab.hicareservices.ui.handler

interface OnOrderClickedHandler {
    fun onOrderItemClicked(
        position: Int,
        orderNo: String,
        serviceType: String,
        toString: String,
        locationLatitudeS: Double?,
        locationLongitudeS: Double?
    )
    fun onOrderPaynowClicked(
        position: Int,
        orderNumberC: String,
        customerIdC: String,
        servicePlanNameC: String,
        orderValueWithTaxC: Double
    )
}