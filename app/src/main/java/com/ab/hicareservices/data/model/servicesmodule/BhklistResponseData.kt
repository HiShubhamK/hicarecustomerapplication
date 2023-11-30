package com.ab.hicareservices.data.model.servicesmodule

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class BhklistResponseData(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("No_Of_BHK") var NoOfBHK: String? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null,
    @SerializedName("Created_By") var CreatedBy: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Sequence_No") var SequenceNo: String? = null,
    @SerializedName("Modified_By") var ModifiedBy: Int? = null,
    @SerializedName("Modified_On") var ModifiedOn: String? = null


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),

        ) {
    }

    override fun describeContents(): Int {
        return 0
    }


    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeValue(Id)
        p0.writeString(NoOfBHK)
        p0.writeValue(IsActive)
        p0.writeValue(CreatedBy)
        p0.writeString(CreatedOn)
        p0.writeString(SequenceNo)
        p0.writeValue(ModifiedBy)
        p0.writeString(ModifiedOn)

    }

    companion object CREATOR : Parcelable.Creator<BhklistResponseData> {
        override fun createFromParcel(parcel: Parcel): BhklistResponseData {
            return BhklistResponseData(parcel)
        }

        override fun newArray(size: Int): Array<BhklistResponseData?> {
            return arrayOfNulls(size)
        }
    }
}
