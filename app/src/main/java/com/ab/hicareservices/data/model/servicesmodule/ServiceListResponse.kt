package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class ServiceListResponse(
    @SerializedName("IsSuccess") var IsSuccess       : Boolean?        = null,
    @SerializedName("Data") var Data: ArrayList<ServiceListResponseData> = arrayListOf(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?         = null
)

