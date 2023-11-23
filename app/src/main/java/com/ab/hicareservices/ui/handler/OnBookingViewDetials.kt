package com.ab.hicareservices.ui.handler

interface OnBookingViewDetials {
    fun onViewDetails(
        position: Int,
        productid: Int,
    )

    abstract fun onClickAddButton(position: Int, id1: Int?, id: String, noOfBHK: String?)
}