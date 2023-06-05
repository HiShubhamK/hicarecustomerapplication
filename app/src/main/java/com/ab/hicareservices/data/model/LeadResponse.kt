package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class LeadResponse(
    @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean? = null,
    @SerializedName("Data"            ) var Data            : LeaderData?    = LeaderData(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?  = null
){}
