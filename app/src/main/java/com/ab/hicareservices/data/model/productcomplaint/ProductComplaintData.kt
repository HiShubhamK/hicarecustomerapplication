package com.ab.hicareservices.data.model.productcomplaint

import com.google.gson.annotations.SerializedName

class ProductComplaintData(
    @SerializedName("Id")
    var Id: Int? = null,
    @SerializedName("Complaint_Type")
    var ComplaintType: String? = null,
    @SerializedName("Complaint_Subject")
    var ComplaintSubject: String? = null,
    @SerializedName("Complaint_Description")
    var ComplaintDescription: String? = null,
    @SerializedName("Order_Id")
    var OrderId: Int? = null,
    @SerializedName("OrderNumber")
    var OrderNumber: String? = null,
    @SerializedName("HiCare_Remark")
    var HiCareRemark: String? = null,
    @SerializedName("Complaint_Status")
    var ComplaintStatus: String? = null,
    @SerializedName("Product_Id")
    var ProductId: Int? = null,
    @SerializedName("Product_Name")
    var ProductName: String? = null,
    @SerializedName("AttachmentList")
    var AttachmentList: ArrayList<String>? = null,
    @SerializedName("Is_Active")
    var IsActive: Boolean? = null,
    @SerializedName("Created_By")
    var CreatedBy: Int? = null,
    @SerializedName("Customer_Id")
    var CustomerId: Int? = null,
    @SerializedName("Created_On")
    var CreatedOn: String? = null,
    @SerializedName("Updated_On")
    var UpdatedOn: String? = null,
    @SerializedName("User_Name")
    var UserName: String? = null,
    @SerializedName("Mobile_No")
    var MobileNo: String? = null,
    @SerializedName("Email")
    var Email: String? = null,
    @SerializedName("OrderDate")
    var OrderDate: String? = null,
    @SerializedName("Contact_Person_Name")
    var ContactPersonName: String? = null,
    @SerializedName("Contact_Person_Mobile")
    var ContactPersonMobile: String? = null,
    @SerializedName("Product_Display_Name")
    var ProductDisplayName: String? = null,
    @SerializedName("OrderValuePostDiscount")
    var OrderValuePostDiscount: Double? = null,
    @SerializedName("Last_Interaction")
    var LastInteraction: String? = null


)

