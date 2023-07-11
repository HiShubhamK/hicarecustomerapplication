package com.ab.hicareservices.data.model.getslots

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(

    @SerializedName("IsSuccess"       ) var IsSuccess       : Boolean?             = null,
    @SerializedName("TimeSlots"       ) var TimeSlots       : ArrayList<TimeSlot> = arrayListOf(),
    @SerializedName("ResponseMessage" ) var ResponseMessage : String?              = null
)