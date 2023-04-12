package com.hc.hicareservices.data.model.referral

import com.google.gson.annotations.SerializedName

data class ReferralResponse(
    @SerializedName("IsSuccess") val isSuccess: Boolean?,
    @SerializedName("Data") val data: String?,
    @SerializedName("ResponseMessage") val responseMessage: String?,
)
