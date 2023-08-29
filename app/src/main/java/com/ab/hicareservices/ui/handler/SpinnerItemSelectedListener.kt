package com.ab.hicareservices.ui.handler

interface SpinnerItemSelectedListener {
    fun onItemSelected(
        ProductDisplayName: Int,
        OrderDate: String,
        ProductId: String,
        OrderNumber: String,
        id: String,
        orderDate: String?,
        orderValuePostDiscount: String,
        orderStatus: String?,
        position: String
    )

}