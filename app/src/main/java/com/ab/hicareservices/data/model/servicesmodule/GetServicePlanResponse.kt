package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class GetServicePlanResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ArrayList<GetServicePlanResponseData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
