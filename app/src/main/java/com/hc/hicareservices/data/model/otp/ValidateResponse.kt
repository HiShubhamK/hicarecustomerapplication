package com.hc.hicareservices.data.model.otp

import com.google.gson.annotations.SerializedName

data class ValidateResponse(
    @SerializedName("IsSuccess") val isSuccess: Boolean,
    @SerializedName("Data") val data: String,
    @SerializedName("ResponseMessage") val responseMessage: String
)
