package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData

interface OnProductClickedHandler {

    fun onProductClickedHandler(
        position: Int,
        productid: Int,
    )
    fun onProductView(
        position: Int,
        productid: OrderSummeryData,
    )

}