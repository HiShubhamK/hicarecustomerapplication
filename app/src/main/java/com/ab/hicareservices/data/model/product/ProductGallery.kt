package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductGallery(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("Product_Id") var ProductId: Int? = null,
    @SerializedName("Product_Name") var ProductName: String? = null,
    @SerializedName("Gallery_Image") var GalleryImage: String? = null,
    @SerializedName("Video_Url") var VideoUrl: String? = null,
    @SerializedName("Sequence_No") var SequenceNo: Int? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null,
    @SerializedName("Created_By") var CreatedBy: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Updated_By") var UpdatedBy: Int? = null,
    @SerializedName("Updated_On") var UpdatedOn: String? = null
) {}
