package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class LeaderData(
    @SerializedName("SFDCId") var SFDCId: String? = null,
    @SerializedName("CCId") var CCId: String? = null,
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
) {}
