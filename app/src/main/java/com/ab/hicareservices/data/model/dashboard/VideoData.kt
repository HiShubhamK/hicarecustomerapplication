package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class VideoData(
    @SerializedName("Title"          ) var Title          : String?  = null,
    @SerializedName("Description"    ) var Description    : String?  = null,
    @SerializedName("ImageUrl"       ) var ImageUrl       : String?  = null,
    @SerializedName("VideoUrl"       ) var VideoUrl       : String?  = null,
    @SerializedName("IsYoutubeVideo" ) var IsYoutubeVideo : Boolean? = null

)