package com.ab.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName

class ComplaintResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ArrayList<ComplaintsData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)