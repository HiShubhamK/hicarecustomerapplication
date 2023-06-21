package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class ProductDetails(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("Product_Name") var ProductName: String? = null,
    @SerializedName("Product_Code") var ProductCode: String? = null,
    @SerializedName("Product_Display_Name") var ProductDisplayName: String? = null,
    @SerializedName("Product_SEO_Name") var ProductSEOName: String? = null,
    @SerializedName("Category_Name") var CategoryName: String? = null,
    @SerializedName("Category_Id") var CategoryId: Int? = null,
    @SerializedName("Sub_Category_Id") var SubCategoryId: Int? = null,
    @SerializedName("SubCategory_Name") var SubCategoryName: String? = null,
    @SerializedName("Product_SEOCategory") var ProductSEOCategory: String? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null,
    @SerializedName("Created_By") var CreatedBy: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Updated_By") var UpdatedBy: Int? = null,
    @SerializedName("Updated_On") var UpdatedOn: String? = null
) {}
