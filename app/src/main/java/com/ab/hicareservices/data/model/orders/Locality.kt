package com.ab.hicareservices.data.model.orders

import com.google.gson.annotations.SerializedName

data class Locality (
    @SerializedName("Id") val id : String?,
    @SerializedName("Name") val name : String?
)