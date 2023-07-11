package com.ab.hicareservices.data.model.productcomplaint.productdetails

data class ComplaintHistory(
    val Complaint_Id: Int,
    val Complaint_Status: String,
    val Created_By: Int,
    val Created_On: String,
    val Remark: String,
    val id: Int
)