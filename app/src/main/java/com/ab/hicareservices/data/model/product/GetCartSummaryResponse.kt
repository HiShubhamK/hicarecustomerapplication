package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class GetCartSummaryResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: GetCartSummaryData? = GetCartSummaryData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
) {}
