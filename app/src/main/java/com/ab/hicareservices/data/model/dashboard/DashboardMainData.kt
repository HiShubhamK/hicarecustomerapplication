package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardMainData(
    @SerializedName("BannerData") var BannerData: ArrayList<BannerData> = arrayListOf(),
    @SerializedName("MenuData") var MenuData: ArrayList<MenuData> = arrayListOf(),
    @SerializedName("OfferData") var OfferData: ArrayList<OfferData> = arrayListOf(),
    @SerializedName("BrandData") var BrandData: ArrayList<BrandData> = arrayListOf(),
    @SerializedName("SocialMediadata") var SocialMediadata: ArrayList<SocialMediadata> = arrayListOf(),
    @SerializedName("VideoData") var VideoData: ArrayList<VideoData> = arrayListOf(),
    @SerializedName("CODOrders") var CODOrders: ArrayList<CODOrders> = arrayListOf(),
    @SerializedName("TodaysService") var TodaysService: ArrayList<UpcomingService> = arrayListOf(),
    @SerializedName("UpcomingService") var UpcomingService: ArrayList<UpcomingService> = arrayListOf()
)