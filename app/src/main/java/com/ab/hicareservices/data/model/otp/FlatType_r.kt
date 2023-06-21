package com.ab.hicareservices.data.model.otp

import com.google.gson.annotations.SerializedName

data class FlatType_r(

    @SerializedName("Id") var Id: String? = null,
    @SerializedName("Name") var Name: String? = null,
    @SerializedName("Unit_Value__c") var UnitValue_c: String? = null,
    @SerializedName("Service_unit__c") var ServiceUnit_c: String? = null

) {}
