package com.ab.hicareservices.data.model.orderdetails

import com.google.gson.annotations.SerializedName

data class OrderDetails (
    @SerializedName("IsSuccess") val isSuccess : Boolean?,
    @SerializedName("Data") val data : List<Data>?,
    @SerializedName("ResponseMessage") val responseMessage : String?
)