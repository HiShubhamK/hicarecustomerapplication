package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class WhatappResponseData(
    @SerializedName("waId") var waId: String? = null,
    @SerializedName("waNumber") var waNumber: String? = null,
    @SerializedName("waName") var waName: String? = null,
    @SerializedName("timestamp") var timestamp: String? = null
) {}
