package com.ab.hicareservices.data.model.slotcomplaincemodel

import com.ab.hicareservices.data.model.getslots.TimeSlot
import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("NoOfAvailableTechnician" ) var NoOfAvailableTechnician : Int?     = null,
    @SerializedName("ScheduledDate"           ) var ScheduledDate           : String?  = null,
    @SerializedName("ScheduledDate_Text"      ) var ScheduledDateText       : String?  = null,
    @SerializedName("AvailableCompliance"     ) var AvailableCompliance     : Int?     = null,
    @SerializedName("Color"                   ) var Color                   : String?  = null,
    @SerializedName("Title"                   ) var Title                   : String?  = null,
    @SerializedName("IsEnabled"               ) var IsEnabled               : Boolean? = null
)