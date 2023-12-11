package com.ab.hicareservices.ui.handler

interface OnServiceclicklistner {

    fun onClickListnerdata(
        position: Int,
        id: Int?,
        serviceName: String?,
        serviceCode: String?,
        serviceThumbnail: String?,
        shortDescription: String?,
        detailDescription: String?
    ) {
    }

}