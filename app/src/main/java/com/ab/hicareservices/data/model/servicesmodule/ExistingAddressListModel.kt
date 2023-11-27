package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class ExistingAddressListModel(

    @SerializedName("IsSuccess") var IsSuccess: Boolean? = null,
    @SerializedName("Data") var Data: ArrayList<ExistingCustomerAddressData> = arrayListOf(),
    @SerializedName("ResponseMessage") var ResponseMessage: String? = null

)
