package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class RelatedProducts(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("Product_Id") var ProductId: Int? = null,
    @SerializedName("Related_Id") var RelatedId: Int? = null,
    @SerializedName("Related_Product_Id") var RelatedProductId: Int? = null,
    @SerializedName("Product_Name") var ProductName: String? = null,
    @SerializedName("Related_Product_Name") var RelatedProductName: String? = null,
    @SerializedName("Related_Product_Code") var RelatedProductCode: String? = null,
    @SerializedName("Product_Code") var ProductCode: String? = null,
    @SerializedName("Created_By") var CreatedBy: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Product_Thumbnail") var ProductThumbnail: String? = null,
    @SerializedName("Price_Per_Quantity") var PricePerQuantity: Int? = null,
    @SerializedName("Discount_Type") var DiscountType: String? = null,
    @SerializedName("Discount") var Discount: Int? = null,
    @SerializedName("Discount_StartDate") var DiscountStartDate: String? = null,
    @SerializedName("Discount_EndDate") var DiscountEndDate: String? = null,
    @SerializedName("Product_Rating") var ProductRating: Double? = null,
    @SerializedName("Is_HotProduct") var IsHotProduct: Boolean? = null,
    @SerializedName("Is_ToSellingProduct") var IsToSellingProduct: Boolean? = null,
    @SerializedName("Is_Stock_Available") var IsStockAvailable: Boolean? = null,
    @SerializedName("Discounted_Amount") var DiscountedAmount: Int? = null,
    @SerializedName("Discounted_Price") var DiscountedPrice: Int? = null,
    @SerializedName("Stock_Status") var StockStatus: Boolean? = null,
    @SerializedName("StockCount") var StockCount: Int? = null,
    @SerializedName("Product_SEO_Name") var ProductSEOName: String? = null,
    @SerializedName("Product_SEOCategory") var ProductSEOCategory: String? = null,
    @SerializedName("Custom_Rating_Message") var CustomRatingMessage: String? = null

) {}
