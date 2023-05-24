package com.ab.hicareservices.data.model.referral

import com.google.gson.annotations.SerializedName

data class ReferralResponse(
    @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean? = null,
    @SerializedName("Data"            ) var Data            : RefData?    = RefData(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?  = null
)
