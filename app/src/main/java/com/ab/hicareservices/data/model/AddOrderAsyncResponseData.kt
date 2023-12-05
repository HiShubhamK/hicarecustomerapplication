package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class AddOrderAsyncResponseData(
    @SerializedName("Id") var Id: String? = null,
    @SerializedName("OrderNo") var OrderNo: String? = null,
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null

)
