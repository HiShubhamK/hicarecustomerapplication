package com.hc.hicareservices.data.model.orders

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("Id") val id : String?,
    @SerializedName("Name") val name : String?,
    @SerializedName("Business_Region_Name__c") val business_Region_Name__c : String?,
    @SerializedName("Code__c") val code__c : String?
)