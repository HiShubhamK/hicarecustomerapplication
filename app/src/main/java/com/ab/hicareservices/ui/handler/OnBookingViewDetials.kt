package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData

interface OnBookingViewDetials {
    fun onViewDetails(
        position: Int,
        productid: Int,
        servicePlanDescription: String?,
    )

    abstract fun onClickAddButton(
        position: Int,
        id1: Int?,
        id: String,
        noOfBHK: String?,
        getServicePlanResponseData: ArrayList<GetServicePlanResponseData>
    )
}