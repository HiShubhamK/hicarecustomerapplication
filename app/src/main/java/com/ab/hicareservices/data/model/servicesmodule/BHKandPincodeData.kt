package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class BHKandPincodeData(

    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("PincodeId") var PincodeId: Int? = null,
    @SerializedName("Pincode") var Pincode: String? = null,
    @SerializedName("BHKId") var BHKId: Int? = null,
    @SerializedName("No_Of_BHK") var NoOfBHK: String? = null,
    @SerializedName("PlanId") var PlanId: Int? = null,
    @SerializedName("Service_Plan_Name") var ServicePlanName: String? = null,
    @SerializedName("Service_Plan_Description") var ServicePlanDescription: String? = null,
    @SerializedName("SPCode") var SPCode: String? = null,
    @SerializedName("Price") var Price: Int? = null,
    @SerializedName("Discounted_Amount") var DiscountedAmount: Int? = null,
    @SerializedName("Discounted_Price") var DiscountedPrice: Int? = null,
    @SerializedName("Discount_Type") var DiscountType: String? = null,
    @SerializedName("Discount") var Discount: Int? = null,
    @SerializedName("Discount_StartDate") var DiscountStartDate: String? = null,
    @SerializedName("Discount_EndDate") var DiscountEndDate: String? = null,
    @SerializedName("Ratings") var Ratings: String? = null,
    @SerializedName("Is_Active") var IsActive: Boolean? = null,
    @SerializedName("Created_By") var CreatedBy: Int? = null,
    @SerializedName("Created_On") var CreatedOn: String? = null,
    @SerializedName("Serviceid") var Serviceid: Int? = null,
    @SerializedName("Modified_By") var ModifiedBy: Int? = null,
    @SerializedName("Modified_On") var ModifiedOn: String? = null
)
