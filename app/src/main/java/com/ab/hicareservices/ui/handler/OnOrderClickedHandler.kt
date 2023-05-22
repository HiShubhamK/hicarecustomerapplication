package com.ab.hicareservices.ui.handler

interface OnOrderClickedHandler {
    fun onOrderItemClicked(position: Int, orderNo: String, serviceType: String, toString: String)
}