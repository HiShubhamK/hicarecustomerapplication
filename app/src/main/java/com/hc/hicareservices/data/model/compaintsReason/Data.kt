package com.hc.hicareservices.data.model.compaintsReason

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("Id") val id : Int?,
	@SerializedName("Name") val name : String?,
	@SerializedName("SubType") val subType : List<String>?
)