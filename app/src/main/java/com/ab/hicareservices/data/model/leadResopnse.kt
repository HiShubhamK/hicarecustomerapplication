package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class leadResopnse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ArrayList<String> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
) {}
