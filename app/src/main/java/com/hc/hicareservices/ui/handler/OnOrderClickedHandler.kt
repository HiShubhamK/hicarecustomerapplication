package com.hc.hicareservices.ui.handler

interface OnOrderClickedHandler {
    fun onOrderItemClicked(position: Int, orderNo: String, serviceType: String, toString: String)
}