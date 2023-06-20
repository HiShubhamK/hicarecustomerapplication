package com.ab.hicareservices.data.model.otp

import com.google.gson.annotations.SerializedName

data class CustomerType_r(
    @SerializedName("Id"   ) var Id   : String? = null,
    @SerializedName("Name" ) var Name : String? = null
){}
