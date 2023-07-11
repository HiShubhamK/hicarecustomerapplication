package com.ab.hicareservices.data.model.productcomplaint.productdetails

data class Complaint(
    val AttachmentList: Any,
    val Complaint_Description: String,
    val Complaint_Status: String,
    val Complaint_Subject: String,
    val Complaint_Type: String,
    val Contact_Person_Mobile: String,
    val Contact_Person_Name: String,
    val Created_By: Int,
    val Created_On: String,
    val Customer_Id: Int,
    val Email: Any,
    val HiCare_Remark: Any,
    val Id: Int,
    val Is_Active: Boolean,
    val Last_Interaction: String,
    val Mobile_No: Any,
    val OrderDate: String,
    val OrderNumber: String,
    val OrderValuePostDiscount: Int,
    val Order_Id: Int,
    val Product_Display_Name: Any,
    val Product_Id: Int,
    val Product_Name: String,
    val Updated_On: Any,
    val User_Name: Any
)