package com.ab.hicareservices.data.model.attachment

import com.google.gson.annotations.SerializedName

data class GetAttachmentResponse(
    @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean?          = null,
    @SerializedName("Data"            ) var Data            : ArrayList<String> = arrayListOf(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?           = null
)