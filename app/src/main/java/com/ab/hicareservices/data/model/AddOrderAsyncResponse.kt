package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class AddOrderAsyncResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: AddOrderAsyncResponseData? = AddOrderAsyncResponseData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
