package com.ab.hicareservices.data.model

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

)
