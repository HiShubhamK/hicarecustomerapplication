package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class GetCartSummaryData(
    @SerializedName("TotalAmount") var TotalAmount: Double? = null,
    @SerializedName("FinalAmount") var FinalAmount: Double? = null,
    @SerializedName("TotalDiscount") var TotalDiscount: Double? = null,
    @SerializedName("TotalGST") var TotalGST: Double? = null,
    @SerializedName("ServiceCharges") var ServiceCharges: Double? = null,
    @SerializedName("InstallationCharges") var InstallationCharges: Double? = null,
    @SerializedName("DeliveryCharges") var DeliveryCharges: Double? = null,
    @SerializedName("VoucherDiscount") var VoucherDiscount: Double? = null,
    @SerializedName("VoucherCode") var VoucherCode: String? = null
) {}
