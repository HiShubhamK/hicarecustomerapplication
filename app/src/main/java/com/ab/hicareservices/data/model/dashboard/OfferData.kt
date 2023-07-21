package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class OfferData(

    @SerializedName("OfferTitle") var OfferTitle: String? = null,
    @SerializedName("SecondaryOfferTitle") var SecondaryOfferTitle: String? = null,
    @SerializedName("VoucherCode") var VoucherCode: String? = null,
    @SerializedName("TermsnConditions") var TermsnConditions: List<String>? = null,
    @SerializedName("IsCopyEnabled") var IsCopyEnabled: Boolean? = null,
    @SerializedName("IsRedeemEnabled") var IsRedeemEnabled: Boolean? = null,
    @SerializedName("IsAppLink") var IsAppLink: Boolean? = null,
    @SerializedName("IsInAppBrowserLink") var IsInAppBrowserLink: Boolean? = null,
    @SerializedName("IsExternalAppBrowserLink") var IsExternalAppBrowserLink: Boolean? = null,
    @SerializedName("PageLink") var PageLink: String? = null,
    @SerializedName("SequenceNo") var SequenceNo: Int? = null


)