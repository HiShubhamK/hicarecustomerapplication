package com.hc.hicareservices.data.model.orders

import com.google.gson.annotations.SerializedName

data class OrdersResponse(
    @SerializedName("IsSuccess") val isSuccess : Boolean?,
    @SerializedName("Data") val data : List<OrdersData>?,
    @SerializedName("ResponseMessage") val responseMessage : String?
)

