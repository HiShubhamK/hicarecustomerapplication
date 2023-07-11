package com.ab.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName


data class ComplaintsData(
    @SerializedName("Id") var Id: String? = null,
    @SerializedName("Name") var Name: String? = null,
    @SerializedName("CaseNumber") var CaseNumber: Int? = null,
    @SerializedName("Subject") var Subject: String? = null,
    @SerializedName("Complaint_No__c") var ComplaintNo_c: String? = null,
    @SerializedName("Complaint_Description__c") var ComplaintDescription_c: String? = null,
    @SerializedName("Sub_Type") var SubType: String? = null,
    @SerializedName("Description") var Description: String? = null,
    @SerializedName("Status") var Status: String? = null,
    @SerializedName("CreatedDate") var CreatedDate: String? = null,
    @SerializedName("ClosedDate") var ClosedDate: String? = null,
    @SerializedName("Case_Age_in_hours__c") var CaseAgeInHours_c: Int? = null,
    @SerializedName("Refund_Requested__c") var RefundRequested_c: Boolean? = null,
    @SerializedName("False_Complaint__c") var FalseComplaint_c: Boolean? = null,
    @SerializedName("Customer_Feedback__c") var CustomerFeedback_c: String? = null,
    @SerializedName("Resolved_False_Remarks__c") var ResolvedFalseRemarks_c: String? = null,
    @SerializedName("Customer_Escalation__c") var CustomerEscalation_c: Boolean? = null,
    @SerializedName("Escalation_Level__c") var EscalationLevel_c: String? = null,
    @SerializedName("Account_Type__c") var AccountType_c: String? = null,
    @SerializedName("Account") var Account: Account? = Account(),
    @SerializedName("Refund_Received_By_Customer__c") var RefundReceivedByCustomer_c: Boolean? = null,
    @SerializedName("Order_No__c") var OrderNo_c: String? = null,
    @SerializedName("IsSelected") var IsSelected: Boolean? = null,
    @SerializedName("Complain_Category") var ComplainCategory: String? = null,
    @SerializedName("Remark") var Remark: String? = null,
    @SerializedName("Complain_Feedback") var ComplainFeedback: String? = null,
    @SerializedName("ComplaintOwner") var ComplaintOwner: String? = null,
    @SerializedName("Source") var Source: String? = null,
    @SerializedName("Service_Code__c") var ServiceCode_c: String? = null,
    @SerializedName("Service_Plan__c") var ServicePlan_c: String? = null,
    @SerializedName("Address") var Address: String? = null,
    @SerializedName("Order_Number__r") var OrderNumber_r: Order_Number__r? = Order_Number__r(),
    @SerializedName("Attachments") var Attachments: String? = null
)