package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class OrderServiceArea_r(

    @SerializedName("Id") var Id: String? = null,
    @SerializedName("SP_Code__c") var SPCode_c: String? = null,
    @SerializedName("Number_of_Treatments__c") var NumberOfTreatments_c: Int? = null,
    @SerializedName("Service_Group_Code__c") var ServiceGroupCode_c: String? = null,
    @SerializedName("Unit__c") var Unit_c: String? = null,
    @SerializedName("Quantity__c") var Quantity_c: Int? = null

) {

}
