package com.ab.hicareservices.data.model.bookslot

import com.google.gson.annotations.SerializedName

class Data(
    @SerializedName("IsSuccess")
    var IsSuccess: Boolean? = null,

    @SerializedName("Data")
    var Data: String? = null,

    @SerializedName("ResponseMessage")
    var ResponseMessage: String? = null

)

