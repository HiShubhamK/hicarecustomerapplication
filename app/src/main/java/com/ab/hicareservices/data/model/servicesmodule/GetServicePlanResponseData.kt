package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class GetServicePlanResponseData(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("PincodeId") var PincodeId: Int? = null,
    @SerializedName("Pincode") var Pincode: String? = null,
    @SerializedName("BHKId") var BHKId: Int? = null,
    @SerializedName("No_Of_BHK") var NoOfBHK: String? = null,
    @SerializedName("PlanId") var PlanId: Int? = null,
    @SerializedName("Service_Plan_Name") var ServicePlanName: String? = null,
    @SerializedName("Service_Plan_Description" ) var ServicePlanDescription : String?  = null,
    @SerializedName("SPCode") var SPCode: String? = null,
    @SerializedName("Price") var Price: Int? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null
)
