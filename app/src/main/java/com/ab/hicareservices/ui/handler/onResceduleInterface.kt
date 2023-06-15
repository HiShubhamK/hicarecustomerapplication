package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.dashboard.OfferData
import com.ab.hicareservices.data.model.dashboard.UpcomingService

interface onResceduleInterface {
    fun onRecheduleClick(
        position: Int,
        offers: ArrayList<UpcomingService>
    )

}