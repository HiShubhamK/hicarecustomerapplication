package com.ab.hicareservices.data.model.compaintsReason

import com.google.gson.annotations.SerializedName

data class ComplaintReasons (
	@SerializedName("IsSuccess") val isSuccess : Boolean?,
	@SerializedName("Data") val data : List<Data>?,
	@SerializedName("ResponseMessage") val responseMessage : String?
)