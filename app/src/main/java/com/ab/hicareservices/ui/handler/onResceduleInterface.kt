package com.ab.hicareservices.ui.handler

import com.ab.hicareservices.data.model.dashboard.CODOrders
import com.ab.hicareservices.data.model.dashboard.OfferData
import com.ab.hicareservices.data.model.dashboard.UpcomingService

interface onResceduleInterface {
    fun onRecheduleClick(
        position: Int,
        offers: ArrayList<UpcomingService>
    )

    fun onPaymentClick(
        position: Int,
        offers: ArrayList<CODOrders>
    )

    fun onPaymentitemsClick(
    position: Int,
    offers: ArrayList<CODOrders>
    )

    fun onToadaysClick(
        position: Int,
        offers: ArrayList<UpcomingService>
    )
}