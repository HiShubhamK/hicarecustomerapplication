package com.ab.hicareservices.data.model.slotcomplaincemodel

import com.google.gson.annotations.SerializedName

data class GetComplaiceResponce(


    @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean?        = null,
    @SerializedName("Data"            ) var Data            : ArrayList<Data> = arrayListOf(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?         = null
)