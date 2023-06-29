package com.ab.hicareservices.data.model.ordersummery

import com.google.gson.annotations.SerializedName

data class OrderSummeryResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var orderSummeryData: ArrayList<OrderSummeryData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)