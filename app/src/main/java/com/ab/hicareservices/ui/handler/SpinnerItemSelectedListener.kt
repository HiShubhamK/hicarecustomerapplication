package com.ab.hicareservices.ui.handler

interface SpinnerItemSelectedListener {
    fun onItemSelected(
        position: Int,
        ProductDisplayName: String,
        OrderDate: String,
        ProductId: String,
        OrderNumber: String,
        id: String,
        orderDate: String?,
        orderValuePostDiscount: Double,
        orderStatus: String,

    )

}