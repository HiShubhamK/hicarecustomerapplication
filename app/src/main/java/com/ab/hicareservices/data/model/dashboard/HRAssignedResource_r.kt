package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class HRAssignedResource_r(
    @SerializedName("Id") var Id: String? = null,
    @SerializedName("Name") var Name: String? = null,
    @SerializedName("Technician_Mobile__c") var TechnicianMobile_c: String? = null,
    @SerializedName("Latitude") var Latitude: Int? = null,
    @SerializedName("Longitude") var Longitude: Int? = null,
    @SerializedName("SyncTime") var SyncTime: String? = null,
    @SerializedName("Employee_Code__c") var EmployeeCode_c: String? = null,
    @SerializedName("Is_Deaf__c") var IsDeaf_c: Boolean? = null,
    @SerializedName("HR_User__c") var HRUser_c: String? = null,
    @SerializedName("Technician__c") var Technician_c: String? = null,
    @SerializedName("HR_Region__c") var HRRegion_c: String? = null,
    @SerializedName("IsManager") var IsManager: Boolean? = null
) {

}
