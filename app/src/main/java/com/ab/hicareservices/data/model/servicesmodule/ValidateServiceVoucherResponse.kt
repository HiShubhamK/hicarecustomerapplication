package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class ValidateServiceVoucherResponse(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ValidateServiceVoucherResponseData? = ValidateServiceVoucherResponseData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null

)
