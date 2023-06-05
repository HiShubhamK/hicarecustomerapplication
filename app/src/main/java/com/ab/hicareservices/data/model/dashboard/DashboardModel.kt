package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardModel(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: DashboardMainData? = DashboardMainData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)