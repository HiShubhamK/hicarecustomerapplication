package com.ab.hicareservices.data.model.servicesmodule

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    ) {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
    }

    companion object CREATOR : Parcelable.Creator<GetServicePlanResponseData> {
        override fun createFromParcel(parcel: Parcel): GetServicePlanResponseData {
            return GetServicePlanResponseData(parcel)
        }

        override fun newArray(size: Int): Array<GetServicePlanResponseData?> {
            return arrayOfNulls(size)
        }
    }
}
