package com.ab.hicareservices.data.model.getslots

import com.google.gson.annotations.SerializedName

data class GetSlots(

    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: Data? = Data(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)