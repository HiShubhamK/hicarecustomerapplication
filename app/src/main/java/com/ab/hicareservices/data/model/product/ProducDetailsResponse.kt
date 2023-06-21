package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ProducDetailsResponse(

    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ProducDetailsData? = ProducDetailsData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null

) {}
