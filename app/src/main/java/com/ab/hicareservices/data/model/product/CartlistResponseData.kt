package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class CartlistResponseData(
    @SerializedName("Product_Id") var ProductId: Int? = null,
    @SerializedName("User_Id") var UserId: Int? = null,
    @SerializedName("Product_Name") var ProductName: String? = null,
    @SerializedName("Product_Code") var ProductCode: String? = null,
    @SerializedName("Product_Display_Name") var ProductDisplayName: String? = null,
    @SerializedName("Product_Thumbnail") var ProductThumbnail: String? = null,
    @SerializedName("Discount_Type") var DiscountType: String? = null,
    @SerializedName("Discount") var Discount: Int? = null,
    @SerializedName("Price_Per_Quantity") var PricePerQuantity: Int? = null,
    @SerializedName("Discounted_Price_Per_Quantity") var DiscountedPricePerQuantity: Int? = null,
    @SerializedName("Discounted_Price") var DiscountedPrice: Int? = null,
    @SerializedName("Actual_Price") var ActualPrice: Int? = null,
    @SerializedName("Free_Shipping") var FreeShipping: Boolean? = null,
    @SerializedName("Delivery_Charges") var DeliveryCharges: Int? = null,
    @SerializedName("Discount_StartDate") var DiscountStartDate: String? = null,
    @SerializedName("Discount_EndDate") var DiscountEndDate: String? = null,
    @SerializedName("Product_Rating") var ProductRating: Double? = null,
    @SerializedName("Minimum_Buy_Quantity") var MinimumBuyQuantity: Int? = null,
    @SerializedName("Maximum_Buy_Quantity") var MaximumBuyQuantity: Int? = null,
    @SerializedName("Cart_Id") var CartId: Int? = null,
    @SerializedName("Quantity") var Quantity: Int? = null,
    @SerializedName("Source_Ip") var SourceIp: String? = null,
    @SerializedName("Installation_Charges") var InstallationCharges: Int? = null,
    @SerializedName("Product_Weight") var ProductWeight: Int? = null,
    @SerializedName("Stock_Status") var StockStatus: Int? = null,
    @SerializedName("Utm_Campaign") var UtmCampaign: String? = null,
    @SerializedName("Utm_Content") var UtmContent: String? = null,
    @SerializedName("Utm_Medium") var UtmMedium: String? = null,
    @SerializedName("Utm_Source") var UtmSource: String? = null,
    @SerializedName("CalculatedDeliveryCharges") var CalculatedDeliveryCharges: Int? = null,
    @SerializedName("Is_SubscribedItem") var IsSubscribedItem: Boolean? = null,
    @SerializedName("Product_Tenure_InDays") var ProductTenureInDays: Int? = null,
    @SerializedName("SubscriptionDiscount") var SubscriptionDiscount: Int? = null
) {}
