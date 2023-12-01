package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName

data class OtherServiceList(

    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("Service_Logo") var ServiceLogo: String? = null,
    @SerializedName("Service_Code") var ServiceCode: String? = null,
    @SerializedName("Service_Name") var ServiceName: String? = null,
    @SerializedName("Service_Thumbnail") var ServiceThumbnail: String? = null,
    @SerializedName("Short_Description") var ShortDescription: String? = null,
    @SerializedName("Detail_Description") var DetailDescription: String? = null,
    @SerializedName("Ratings") var Ratings: String? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null,
    @SerializedName("Created_By") var CreatedBy: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Updated_By") var UpdatedBy: Int? = null,
    @SerializedName("Updated_On") var UpdatedOn: String? = null,
    @SerializedName("FaqList") var FaqList: String? = null,
    @SerializedName("OtherServiceList") var OtherServiceList: String? = null

)
