package com.ab.hicareservices.data.model.otp

import com.google.gson.annotations.SerializedName

data class ValidateResponse(

    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ValidateResponseData? = ValidateResponseData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
