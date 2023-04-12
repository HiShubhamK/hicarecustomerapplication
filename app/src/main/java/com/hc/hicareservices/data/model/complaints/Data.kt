package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName

data class Data (
	@SerializedName("Id") val id : String?,
	@SerializedName("ComplaintNo") val complaintNo : String?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)