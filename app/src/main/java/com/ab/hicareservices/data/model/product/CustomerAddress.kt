package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class CustomerAddress(
    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ArrayList<CustomerAddressData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null
) {}
