package com.ab.hicareservices.data.model.productcomplaint.productdetails

data class Data(
    val Complaint: Complaint,
    val ComplaintAttachment: List<ComplaintAttachment>,
    val ComplaintHistory: List<ComplaintHistory>
)