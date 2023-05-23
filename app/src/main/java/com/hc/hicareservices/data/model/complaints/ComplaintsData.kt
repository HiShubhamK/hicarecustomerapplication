package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName


data class ComplaintsData(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("caseNumber") var caseNumber: Int? = null,
    @SerializedName("subject") var subject: String? = null,
    @SerializedName("complaint_No__c") var complaintNo_c: String? = null,
    @SerializedName("complaint_Description__c") var complaintDescription_c: String? = null,
    @SerializedName("sub_Type") var subType: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("createdDate") var createdDate: String? = null,
    @SerializedName("closedDate") var closedDate: String? = null,
    @SerializedName("case_Age_in_hours__c") var caseAgeInHours_c: Int? = null,
    @SerializedName("refund_Requested__c") var refundRequested_c: Boolean? = null,
    @SerializedName("false_Complaint__c") var falseComplaint_c: Boolean? = null,
    @SerializedName("customer_Feedback__c") var customerFeedback_c: String? = null,
    @SerializedName("resolved_False_Remarks__c") var resolvedFalseRemarks_c: String? = null,
    @SerializedName("customer_Escalation__c") var customerEscalation_c: Boolean? = null,
    @SerializedName("escalation_Level__c") var escalationLevel_c: String? = null,
    @SerializedName("account_Type__c") var accountType_c: String? = null,
    @SerializedName("account") var account: Account? = Account(),
    @SerializedName("refund_Received_By_Customer__c") var refundReceivedByCustomer_c: Boolean? = null,
    @SerializedName("order_No__c") var orderNo_c: String? = null,
    @SerializedName("isSelected") var isSelected: Boolean? = null,
    @SerializedName("complain_Category") var complainCategory: String? = null,
    @SerializedName("remark") var remark: String? = null,
    @SerializedName("complain_Feedback") var complainFeedback: String? = null,
    @SerializedName("complaintOwner") var complaintOwner: String? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("service_Code__c") var serviceCode_c: String? = null,
    @SerializedName("service_Plan__c") var servicePlan_c: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("order_Number__r") var orderNumber_r: Order_Number__r? = Order_Number__r()

)