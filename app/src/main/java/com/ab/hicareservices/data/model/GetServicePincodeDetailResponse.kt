package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class GetServicePincodeDetailResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: GetServicePincodeDetailResponseData? = GetServicePincodeDetailResponseData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
