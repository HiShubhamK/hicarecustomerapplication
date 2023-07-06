package com.ab.hicareservices.data.model.productcomplaint

import com.google.gson.annotations.SerializedName

class ProductComplaintListResponse(
    @SerializedName("IsSuccess")
    var IsSuccess: Boolean? = null,
    @SerializedName("Data")
    var Data: ArrayList<ProductComplaintData> = arrayListOf(),
    @SerializedName("ResponseMessage")
    var ResponseMessage: String? = null
)

