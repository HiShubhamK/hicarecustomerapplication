package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class CreateEventNotificationResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: Boolean? = null,
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
