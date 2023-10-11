package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class AppversionData(
    @SerializedName("IsUpdated") var IsUpdated: Boolean? = false,
    @SerializedName("Versioncode") var Versioncode: String? = null,
    @SerializedName("Versionname") var Versionname: String? = null,
)
