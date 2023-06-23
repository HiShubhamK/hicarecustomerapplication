package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class GetCartSummaryData(
    @SerializedName("TotalAmount") var TotalAmount: Int? = null,
    @SerializedName("FinalAmount") var FinalAmount: Int? = null,
    @SerializedName("TotalDiscount") var TotalDiscount: Int? = null,
    @SerializedName("TotalGST") var TotalGST: Int? = null,
    @SerializedName("ServiceCharges") var ServiceCharges: Int? = null,
    @SerializedName("InstallationCharges") var InstallationCharges: Int? = null,
    @SerializedName("DeliveryCharges") var DeliveryCharges: Int? = null,
    @SerializedName("VoucherDiscount") var VoucherDiscount: Int? = null,
    @SerializedName("VoucherCode") var VoucherCode: String? = null
) {}
