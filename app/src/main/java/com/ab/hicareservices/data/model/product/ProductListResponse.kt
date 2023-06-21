package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ArrayList<ProductListResponseData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
) {}
