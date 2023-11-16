package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class BookingServiceDetailResponseData(

    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("Service_Logo") var ServiceLogo: String? = null,
    @SerializedName("Service_Code") var ServiceCode: String? = null,
    @SerializedName("Service_Name") var ServiceName: String? = null,
    @SerializedName("Service_Thumbnail") var ServiceThumbnail: String? = null,
    @SerializedName("Short_Description") var ShortDescription: String? = null,
    @SerializedName("Detail_Description") var DetailDescription: String? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null

)
