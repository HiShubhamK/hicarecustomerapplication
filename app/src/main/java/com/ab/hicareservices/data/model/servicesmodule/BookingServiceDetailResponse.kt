package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class BookingServiceDetailResponse(

    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: BookingServiceDetailResponseData? = BookingServiceDetailResponseData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
