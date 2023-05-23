package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName

class ComplaintResponse(
    @SerializedName("IsSuccess") var Boolean: Boolean?,
    @SerializedName("Data") var Data: ArrayList<ComplaintsData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String?


)