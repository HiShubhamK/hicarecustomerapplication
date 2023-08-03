package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.ProductListResponseData

interface OnProductClickedHandler {

    fun onProductClickedHandler(
        position: Int,
        productid: Int,
    )
    fun onProductView(
        position: Int,
        productid: OrderSummeryData,
    )
    fun onNotifyMeclick(
        position: Int,
        productid: ProductListResponseData,
    )

}