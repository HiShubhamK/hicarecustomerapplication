package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class BhklistResponseData(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("No_Of_BHK") var NoOfBHK: String? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null
)
