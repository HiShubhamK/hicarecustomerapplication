package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName

data class Order_Number__r (
	@SerializedName("Id") val id : String?,
	@SerializedName("Name") val name : String?,
	@SerializedName("Customer_Id__c") val customer_Id__c : String?,
	@SerializedName("Order_Number__c") val order_Number__c : String?,
	@SerializedName("Status__c") val status__c : String?,
	@SerializedName("Unit1__c") val unit1__c : String?,
	@SerializedName("Sub_Type1__c") val sub_Type1__c : String?,
	@SerializedName("Account_Types__c") val account_Types__c : String?,
	@SerializedName("HR_Shipping_Region__r") val hR_Shipping_Region__r : String?,
	@SerializedName("Quantity__c") val quantity__c : String?,
	@SerializedName("Payment_Terms__c") val payment_Terms__c : String?,
	@SerializedName("End_Date__c") val end_Date__c : String?,
	@SerializedName("Start_Date__c") val start_Date__c : String?,
	@SerializedName("Account_Name__r") val account_Name__r : String?,
	@SerializedName("Standard_Value__c") val standard_Value__c : String?,
	@SerializedName("Order_Value_with_Tax__c") val order_Value_with_Tax__c : String?,
	@SerializedName("OrderValueWithTaxAfterDiscount") val orderValueWithTaxAfterDiscount : String?,
	@SerializedName("OrderDiscountValue") val orderDiscountValue : String?,
	@SerializedName("Discount_Offered__c") val discount_Offered__c : String?,
	@SerializedName("Payment_Type__c") val payment_Type__c : String?,
	@SerializedName("AppointmentStartDateTime__c") val appointmentStartDateTime__c : String?,
	@SerializedName("AppointmentEndDateTime__c") val appointmentEndDateTime__c : String?,
	@SerializedName("Service_Plan_Name__c") val service_Plan_Name__c : String?,
	@SerializedName("Web_ID__c") val web_ID__c : String?,
	@SerializedName("TaskTypeDescription") val taskTypeDescription : String?,
	@SerializedName("CreatedDate") val createdDate : String?,
	@SerializedName("CreatedDateText") val createdDateText : String?,
	@SerializedName("CVclaim__c") val cVclaim__c : Boolean?,
	@SerializedName("First_SR_SV_Claimed_date__c") val first_SR_SV_Claimed_date__c : String?,
	@SerializedName("Voucher_Code__c") val voucher_Code__c : String?,
	@SerializedName("SP_Code") val sP_Code : String?,
	@SerializedName("Service_SP_Code__c") val service_SP_Code__c : String?,
	@SerializedName("House_Type__r") val house_Type__r : String?,
	@SerializedName("Service_Type") val service_Type : String?,
	@SerializedName("Enable_Payment_Link") val enable_Payment_Link : Boolean?,
	@SerializedName("Service_Period") val service_Period : String?,
	@SerializedName("Service_Plan_Image_Url") val service_Plan_Image_Url : String?,
	@SerializedName("Payment_Subscription_Id__c") val payment_Subscription_Id__c : String?,
	@SerializedName("Payment_Subscription_Plan_Id__c") val payment_Subscription_Plan_Id__c : String?,
	@SerializedName("Redeem_Hygine_Points__c") val redeem_Hygine_Points__c : String?,
	@SerializedName("Service_Group__c") val service_Group__c : String?
)