package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductTestimonialList(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("Product_Id") var ProductId: Int? = null,
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("Temp_CustName") var TempCustName: String? = null,
    @SerializedName("User_Id") var UserId: Int? = null,
    @SerializedName("Title") var Title: String? = null,
    @SerializedName("Product_Name") var ProductName: String? = null,
    @SerializedName("Product_Code") var ProductCode: String? = null,
    @SerializedName("Product_Display_Name") var ProductDisplayName: String? = null,
    @SerializedName("Temp_CustCity") var TempCustCity: String? = null,
    @SerializedName("Rating") var Rating: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Image_Attachmnet_url") var ImageAttachmnetUrl: String? = null,
    @SerializedName("ReviewRating") var ReviewRating: String? = null,
    @SerializedName("Is_Approved") var IsApproved: Boolean? = null
) {}
