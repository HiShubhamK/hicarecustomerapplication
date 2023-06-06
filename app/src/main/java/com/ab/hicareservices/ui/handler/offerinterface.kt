package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.dashboard.OfferData

interface offerinterface {
    fun onOfferClick(
        position: Int,
        offers: ArrayList<OfferData>
    )

}