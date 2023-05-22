package com.ab.hicareservices.data.model.service

import com.google.gson.annotations.SerializedName

data class ServiceTaskData(
    @SerializedName("Id") val id : String,
    @SerializedName("Name") val name : String,
    @SerializedName("Amount_to_collect__c") val amount_to_collect__c : String,
    @SerializedName("Advance_Payment_Amount__c") val advance_Payment_Amount__c : String,
    @SerializedName("Order__c") val order__c : String,
    @SerializedName("HR_Status__c") val hR_Status__c : String,
    @SerializedName("IsMegatask__c") val isMegatask__c : Boolean,
    @SerializedName("IsDoubleCheckConfirmed__c") val isDoubleCheckConfirmed__c : Boolean,
    @SerializedName("Service_Sequence_Number__c") val service_Sequence_Number__c : String
)
