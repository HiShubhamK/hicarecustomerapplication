package com.hc.hicareservices.data.model.orders

import com.google.gson.annotations.SerializedName

data class House(
    @SerializedName("Id") val id : String?,
    @SerializedName("Name") val name : String?
)