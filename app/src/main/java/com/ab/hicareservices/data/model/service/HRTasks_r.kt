package com.ab.hicareservices.data.model.service

import com.google.gson.annotations.SerializedName

data class HRTasks_r(

    @SerializedName("nextRecordsUrl" ) var nextRecordsUrl : String?            = null,
    @SerializedName("totalSize"      ) var totalSize      : Int?               = null,
    @SerializedName("done"           ) var done           : Boolean?           = null,
    @SerializedName("records"        ) var records        : ArrayList<Records> = arrayListOf()
)
