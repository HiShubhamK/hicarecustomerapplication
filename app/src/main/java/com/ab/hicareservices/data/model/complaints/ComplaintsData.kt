package com.ab.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName


data class ComplaintsData (
    @SerializedName("Id") val id : String?,
    @SerializedName("Name") val name : String?,
    @SerializedName("CaseNumber") val caseNumber : Int?,
    @SerializedName("Subject") val subject : String?,
    @SerializedName("Complaint_No__c") val complaint_No__c : String?,
    @SerializedName("Complaint_Description__c") val complaint_Description__c : String?,
    @SerializedName("Sub_Type") val sub_Type : String?,
    @SerializedName("Description") val description : String?,
    @SerializedName("Status") val status : String?,
    @SerializedName("CreatedDate") val createdDate : String?,
    @SerializedName("ClosedDate") val closedDate : String?,
    @SerializedName("Case_Age_in_hours__c") val case_Age_in_hours__c : Int?,
    @SerializedName("Refund_Requested__c") val refund_Requested__c : Boolean?,
    @SerializedName("False_Complaint__c") val false_Complaint__c : Boolean?,
    @SerializedName("Customer_Feedback__c") val customer_Feedback__c : String?,
    @SerializedName("Resolved_False_Remarks__c") val resolved_False_Remarks__c : String?,
    @SerializedName("Customer_Escalation__c") val customer_Escalation__c : Boolean?,
    @SerializedName("Escalation_Level__c") val escalation_Level__c : String?,
    @SerializedName("Account_Type__c") val account_Type__c : String?,
    @SerializedName("Account") val account : Account?,
    @SerializedName("Refund_Received_By_Customer__c") val refund_Received_By_Customer__c : Boolean?,
    @SerializedName("Order_No__c") val order_No__c : Int?,
    @SerializedName("IsSelected") val isSelected : Boolean?,
    @SerializedName("Complain_Category") val complain_Category : String?,
    @SerializedName("Remark") val remark : String?,
    @SerializedName("Complain_Feedback") val complain_Feedback : String?,
    @SerializedName("ComplaintOwner") val complaintOwner : String?,
    @SerializedName("Source") val source : String?,
    @SerializedName("Service_Code__c") val service_Code__c : String?,
    @SerializedName("Service_Plan__c") val service_Plan__c : String?,
    @SerializedName("Address") val address : String?,
    @SerializedName("Order_Number__r") val order_Number__r : Order_Number__r?,
)