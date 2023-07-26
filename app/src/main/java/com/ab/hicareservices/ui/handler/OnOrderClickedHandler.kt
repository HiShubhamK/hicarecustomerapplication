package com.ab.hicareservices.ui.handler

interface OnOrderClickedHandler {
    fun onOrderItemClicked(
        position: Int,
        orderNo: String,
        serviceType: String,
        toString: String,
        locationLatitudeS: Double?,
        locationLongitudeS: Double?,
        toString1: String
    )
    fun onOrderPaynowClicked(
        position: Int,
        orderNumberC: String,
        customerIdC: String,
        servicePlanNameC: String,
        orderValueWithTaxC: Double,
        serviceType: String,
        standardValueC: Double?
    )
    fun onNotifyMeclick(
        position: Int,
        orderNumberC: String,
        customerIdC: String
    )
}