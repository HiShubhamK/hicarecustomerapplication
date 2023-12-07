package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class GetServicePincodeDetailResponseData(
    @SerializedName("name") var name: String? = null,
    @SerializedName("city_name__c") var cityName_c: String? = null,
    @SerializedName("state_name__c") var stateName_c: String? = null
)
