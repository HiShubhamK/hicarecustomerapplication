package com.ab.hicareservices.data.model.bookslot

import com.google.gson.annotations.SerializedName

data class BookSlotResponce(

    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: Data? = Data(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)