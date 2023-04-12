package com.hc.hicareservices.data.model.orderdetails

import com.google.gson.annotations.SerializedName

data class LocalityPincode (
	@SerializedName("Id") val id : String?,
	@SerializedName("Name") val name : String?
)