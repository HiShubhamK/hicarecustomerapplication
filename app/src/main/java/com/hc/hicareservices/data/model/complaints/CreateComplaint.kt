package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName

data class CreateComplaint (
    @SerializedName("IsSuccess") val isSuccess : Boolean?,
    @SerializedName("Data") val data : Data?,
    @SerializedName("ResponseMessage") val responseMessage : String?
)