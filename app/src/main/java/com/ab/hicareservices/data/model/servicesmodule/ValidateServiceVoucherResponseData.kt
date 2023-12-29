package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class ValidateServiceVoucherResponseData(
    @SerializedName("VoucherDiscount") var VoucherDiscount: Double? = null,
    @SerializedName("VoucherDiscount_InPercentage") var VoucherDiscountInPercentage: Int? = null,
    @SerializedName("FinalAmount") var FinalAmount: Double? = null
)
