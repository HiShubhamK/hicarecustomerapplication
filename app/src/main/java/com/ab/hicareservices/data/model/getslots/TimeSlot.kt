package com.ab.hicareservices.data.model.getslots

import com.google.gson.annotations.SerializedName

data class TimeSlot(

    @SerializedName("Start") var Start: String? = null,
    @SerializedName("StartTime") var StartTime: String? = null,
    @SerializedName("Finish") var Finish: String? = null,
    @SerializedName("FinishTime") var FinishTime: String? = null,
    @SerializedName("Grade") var Grade: String? = null,
    @SerializedName("DisplayText") var DisplayText: String? = null

)