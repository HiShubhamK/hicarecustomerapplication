package com.ab.hicareservices.data.model.otp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OtpResponse(
    @SerializedName("IsSuccess") val isSuccess: Boolean?,
    @SerializedName("Data") val data: String?,
    @SerializedName("ResponseMessage") val responseMessage: String?
)
