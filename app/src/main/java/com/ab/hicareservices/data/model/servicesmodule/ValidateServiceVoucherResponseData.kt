package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class ValidateServiceVoucherResponseData(
    @SerializedName("VoucherDiscount" ) var VoucherDiscount : Int? = null,
    @SerializedName("FinalAmount"     ) var FinalAmount     : Int? = null
)
