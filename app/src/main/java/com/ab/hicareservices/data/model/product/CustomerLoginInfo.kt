package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class CustomerLoginInfo(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: CustomerLoginInfoData? = CustomerLoginInfoData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
) {}
