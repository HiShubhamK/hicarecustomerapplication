package com.ab.hicareservices.data.model.otp

import com.google.gson.annotations.SerializedName

data class ValidateResponseData(

    @SerializedName("Token") var Token: String? = null,
    @SerializedName("PestCustomerData") var PestCustomerData: PestCustomerData? = PestCustomerData(),
    @SerializedName("ProductCustomerData") var ProductCustomerData: ProductCustomerData? = ProductCustomerData()
) {}
