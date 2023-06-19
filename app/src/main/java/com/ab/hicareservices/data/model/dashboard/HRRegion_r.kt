package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class HRRegion_r(
    @SerializedName("Id") var Id: String? = null,
    @SerializedName("Name") var Name: String? = null,
    @SerializedName("Business_Region_Name__c") var BusinessRegionName_c: String? = null,
    @SerializedName("Code__c") var Code_c: String? = null
) {}