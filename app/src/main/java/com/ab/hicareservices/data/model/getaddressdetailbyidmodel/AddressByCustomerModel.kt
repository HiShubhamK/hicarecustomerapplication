package com.ab.hicareservices.data.model.getaddressdetailbyidmodel

import com.google.gson.annotations.SerializedName

data class AddressByCustomerModel(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: AddressByDetailIdData? = AddressByDetailIdData(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
)