package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductCount(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: Int? = null,
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
