package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class MenuData(
    @SerializedName("ImageUrl") var ImageUrl: String? = null,
    @SerializedName("Title") var Title: String? = null,
    @SerializedName("IsAppLink") var IsAppLink: Boolean? = null,
    @SerializedName("IsIcon") var IsIcon: Boolean? = null,
    @SerializedName("IsInAppBrowserLink") var IsInAppBrowserLink: Boolean? = null,
    @SerializedName("IsExternalAppBrowserLink") var IsExternalAppBrowserLink: Boolean? = null,
    @SerializedName("PageLink") var PageLink: String? = null,
    @SerializedName("SequenceNo") var SequenceNo: Int? = null

)