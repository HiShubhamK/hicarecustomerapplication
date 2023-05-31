package com.ab.hicareservices.data.model.service

import com.google.gson.annotations.SerializedName

data class ServiceResponse(

    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ArrayList<ServiceData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
