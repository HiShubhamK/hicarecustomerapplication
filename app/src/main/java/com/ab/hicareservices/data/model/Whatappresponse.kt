package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class Whatappresponse(

    @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean? = null,
    @SerializedName("Data"            ) var Data            : WhatappResponseData?= WhatappResponseData(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?  = null
) {}
