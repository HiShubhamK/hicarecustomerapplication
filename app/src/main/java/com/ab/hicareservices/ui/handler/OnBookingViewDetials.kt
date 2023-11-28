package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData

interface OnBookingViewDetials {
    fun onViewDetails(
        position: Int,
        productid: Int,
        servicePlanDescription: String?,
        price: Int?,
        discountedPrice: Int?,
    )

    fun onClickAddButton(
        position: Int,
        id1: Int?,
        id: String,
        noOfBHK: String?,
        getServicePlanResponseData: ArrayList<GetServicePlanResponseData>,
        price: Int?,
        discountedAmount: Int?,
        discountedPrice: Int?
    )
}