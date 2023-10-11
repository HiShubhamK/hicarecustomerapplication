package com.ab.hicareservices.data.model

import com.ab.hicareservices.data.model.complaints.Data
import com.google.gson.annotations.SerializedName

data class CurrentApiVersion(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") val data : AppversionData?,
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)
