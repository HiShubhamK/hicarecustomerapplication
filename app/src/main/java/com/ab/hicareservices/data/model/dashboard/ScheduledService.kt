package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class ScheduledService(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var UpcommingService: ArrayList<UpcomingService> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
){}
