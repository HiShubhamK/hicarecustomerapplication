package com.ab.hicareservices.data.model.savesalesmodel

import com.google.gson.annotations.SerializedName

data class HomeProduct(

    @SerializedName("Product_Id") var ProductId: Int? = null,
    @SerializedName("Product_Name") var ProductName: String? = null,
    @SerializedName("Product_Code") var ProductCode: String? = null,
    @SerializedName("Product_Display_Name") var ProductDisplayName: String? = null,
    @SerializedName("Product_Thumbnail") var ProductThumbnail: String? = null,
    @SerializedName("Product_SEOCategory") var ProductSEOCategory: String? = null,
    @SerializedName("Discount_Type") var DiscountType: String? = null,
    @SerializedName("Discount") var Discount: Int? = null,
    @SerializedName("Price_Per_Quantity") var PricePerQuantity: Int? = null,
    @SerializedName("Discounted_Price") var DiscountedPrice: Int? = null,
    @SerializedName("Discount_StartDate") var DiscountStartDate: String? = null,
    @SerializedName("Discount_EndDate") var DiscountEndDate: String? = null,
    @SerializedName("Installation_Charges") var InstallationCharges: Int? = null,
    @SerializedName("Product_Weight") var ProductWeight: Int? = null,
    @SerializedName("Is_HotProduct") var IsHotProduct: Boolean? = null,
    @SerializedName("Is_Stock_Available") var IsStockAvailable: Boolean? = null,
    @SerializedName("Is_ToSellingProduct") var IsToSellingProduct: Boolean? = null,
    @SerializedName("StockStatus") var StockStatus: Boolean? = null,
    @SerializedName("Product_Rating") var ProductRating: Int? = null,
    @SerializedName("StockCount") var StockCount: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Counter") var Counter: Int? = null,
    @SerializedName("StockCounterAmount") var StockCounterAmount: Int? = null,
    @SerializedName("Product_SEO_Name") var ProductSEOName: String? = null,
    @SerializedName("Custom_Rating_Message") var CustomRatingMessage: String? = null
)