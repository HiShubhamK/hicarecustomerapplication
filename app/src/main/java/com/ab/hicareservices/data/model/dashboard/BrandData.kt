package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class BrandData(

    @SerializedName("ImageUrl") var ImageUrl: String? = null,
    @SerializedName("Title") var Title: String? = null,
    @SerializedName("SequenceNo") var SequenceNo: Int? = null
)