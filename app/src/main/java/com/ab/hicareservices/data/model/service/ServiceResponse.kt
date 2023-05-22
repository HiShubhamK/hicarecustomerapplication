package com.ab.hicareservices.data.model.service

import com.google.gson.annotations.SerializedName

data class ServiceResponse(
    @SerializedName("IsSuccess") val isSuccess : Boolean,
    @SerializedName("Data") val data : List<ServiceData>,
    @SerializedName("ResponseMessage") val responseMessage : String
)
