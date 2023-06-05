package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class SocialMediadata(

    @SerializedName("ImageUrl"   ) var ImageUrl   : String? = null,
    @SerializedName("Title"      ) var Title      : String? = null,
    @SerializedName("PageUrl"    ) var PageUrl    : String? = null,
    @SerializedName("Message"    ) var Message    : String? = null,
    @SerializedName("SequenceNo" ) var SequenceNo : Int?    = null
)