package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName
import com.hc.hicareservices.data.model.orders.OrdersData

class ComplaintResponse(
    @SerializedName("IsSuccess") val isSuccess: Boolean?,
    @SerializedName("Data") val data: List<ComplaintsData>?,
    @SerializedName("ResponseMessage") val responseMessage: String?
)