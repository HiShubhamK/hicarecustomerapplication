package com.ab.hicareservices.data.model

import com.google.gson.annotations.SerializedName


data class HomeProduct(

    @SerializedName("Product_Id") var ProductId: Int? = null,
    @SerializedName("Product_Name") var ProductName: String? = null,
    @SerializedName("Product_Code") var ProductCode: String? = null,
    @SerializedName("Product_Display_Name") var ProductDisplayName: String? = null,
    @SerializedName("Product_Thumbnail") var ProductThumbnail: String? = null,
    @SerializedName("Discount_Type") var DiscountType: String? = null,
    @SerializedName("Discount") var Discount: Double? = null,
    @SerializedName("Price_Per_Quantity") var PricePerQuantity: Double? = null,
    @SerializedName("Discounted_Price") var DiscountedPrice: Double? = null,
    @SerializedName("Installation_Charges") var InstallationCharges: Double? = null,
    @SerializedName("Product_Weight") var ProductWeight: Double? = null,
    @SerializedName("Counter") var Counter: Int? = null,
    @SerializedName("StockCounterAmount") var StockCounterAmount: Float? = null
) {}

