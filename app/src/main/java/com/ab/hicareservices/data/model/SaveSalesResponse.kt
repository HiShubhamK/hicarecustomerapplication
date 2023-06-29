package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class SaveSalesResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: String? = null,
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
