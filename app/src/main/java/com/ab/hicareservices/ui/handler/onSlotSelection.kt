package com.ab.hicareservices.ui.handler

interface onSlotSelection {
    fun onSlotBookSelect(
        position: Int,
        TaskId: String,
        AppointmentDate: String,
        AppointmentStart: String,
        AppointmentEnd: String?,
        Source: String?,
        ServiceType: String,
        toString: String

    )

}