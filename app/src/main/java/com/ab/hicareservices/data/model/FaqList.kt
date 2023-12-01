package com.ab.hicareservices.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FaqList(

    @SerializedName("FAQ_Id"       ) var FAQId       : Int?     = null,
    @SerializedName("Service_Name" ) var ServiceName : String?  = null,
    @SerializedName("FAQ_Title"    ) var FAQTitle    : String?  = null,
    @SerializedName("FAQ_Type"     ) var FAQType     : String?  = null,
    @SerializedName("FAQ_Detail"   ) var FAQDetail   : String?  = null,
    @SerializedName("Sequence_No"  ) var SequenceNo  : Int?     = null,
    @SerializedName("Is_Active"    ) var IsActive    : Boolean? = null,
    @SerializedName("Created_By"   ) var CreatedBy   : Int?     = null,
    @SerializedName("Created_On"   ) var CreatedOn   : String?  = null,
    @SerializedName("Updated_By"   ) var UpdatedBy   : Int?     = null,
    @SerializedName("Updated_On"   ) var UpdatedOn   : String?  = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeValue(FAQId)
        p0.writeString(ServiceName)
        p0.writeString(FAQTitle)
        p0.writeString(FAQType)
        p0.writeString(FAQDetail)
        p0.writeValue(SequenceNo)
        p0.writeValue(IsActive)
        p0.writeValue(CreatedBy)
        p0.writeString(CreatedOn)
        p0.writeValue(UpdatedBy)
        p0.writeString(UpdatedOn)
    }

    companion object CREATOR : Parcelable.Creator<FaqList> {
        override fun createFromParcel(parcel: Parcel): FaqList {
            return FaqList(parcel)
        }

        override fun newArray(size: Int): Array<FaqList?> {
            return arrayOfNulls(size)
        }
    }
}
