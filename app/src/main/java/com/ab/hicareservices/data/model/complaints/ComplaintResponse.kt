package com.ab.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName

class ComplaintResponse(
    @SerializedName("IsSuccess") val isSuccess: Boolean?,
    @SerializedName("Data") val data: List<ComplaintsData>?,
    @SerializedName("ResponseMessage") val responseMessage: String?
)