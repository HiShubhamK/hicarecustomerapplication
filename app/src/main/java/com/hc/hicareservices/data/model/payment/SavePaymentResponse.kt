package com.hc.hicareservices.data.model.payment

import com.google.gson.annotations.SerializedName

data class SavePaymentResponse (
    @SerializedName("IsSuccess") val isSuccess: Boolean?,
    @SerializedName("Data") val data: String?,
    @SerializedName("ResponseMessage") val responseMessage: String?
)