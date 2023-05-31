package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.service.ServiceData

interface OnServiceRequestClickHandler {
    fun onViewServiceClicked(position: Int)
    fun onRescheduleServiceClicked(position: Int, service: ServiceData)
}