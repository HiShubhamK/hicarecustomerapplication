package com.ab.hicareservices.data.model.servicesmodule

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BhklistResponseData(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("No_Of_BHK") var NoOfBHK: String? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }
}
