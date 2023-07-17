package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ClearCacheResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: String? = null,
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
