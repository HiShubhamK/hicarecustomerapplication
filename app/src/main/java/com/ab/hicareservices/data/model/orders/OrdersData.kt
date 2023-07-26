package com.ab.hicareservices.data.model.orders

import com.ab.hicareservices.data.model.dashboard.AccountName_r
import com.ab.hicareservices.data.model.dashboard.HRShippingRegion_r
import com.ab.hicareservices.data.model.dashboard.HouseType_r
import com.google.gson.annotations.SerializedName

data class OrdersData(

    @SerializedName("Id") var Id: String? = null,
    @SerializedName("Name") var Name: String? = null,
    @SerializedName("Customer_Id__c") var CustomerId_c: String? = null,
    @SerializedName("Order_Number__c") var OrderNumber_c: String? = null,
    @SerializedName("Status__c") var Status_c: String? = null,
    @SerializedName("Unit1__c") var Unit1_c: String? = null,
    @SerializedName("Sub_Type1__c") var SubType1_c: String? = null,
    @SerializedName("Account_Types__c") var AccountTypes_c: String? = null,
    @SerializedName("HR_Shipping_Region__r") var HRShippingRegion_r: HRShippingRegion_r? = HRShippingRegion_r(),
    @SerializedName("Quantity__c") var Quantity_c: String? = null,
    @SerializedName("Payment_Terms__c") var PaymentTerms_c: String? = null,
    @SerializedName("End_Date__c") var EndDate_c: String? = null,
    @SerializedName("Start_Date__c") var StartDate_c: String? = null,
    @SerializedName("Account_Name__r") var AccountName_r: AccountName_r? = AccountName_r(),
    @SerializedName("Standard_Value__c") var StandardValue_c: String? = null,
    @SerializedName("Order_Value_with_Tax__c") var OrderValueWithTax_c: String? = null,
    @SerializedName("OrderValueWithTaxAfterDiscount") var OrderValueWithTaxAfterDiscount: String? = null,
    @SerializedName("OrderDiscountValue") var OrderDiscountValue: String? = null,
    @SerializedName("Discount_Offered__c") var DiscountOffered_c: String? = null,
    @SerializedName("Payment_Type__c") var PaymentType_c: String? = null,
    @SerializedName("AppointmentStartDateTime__c") var AppointmentStartDateTime_c: String? = null,
    @SerializedName("AppointmentEndDateTime__c") var AppointmentEndDateTime_c: String? = null,
    @SerializedName("Service_Plan_Name__c") var ServicePlanName_c: String? = null,
    @SerializedName("Web_ID__c") var WebID_c: String? = null,
    @SerializedName("TaskTypeDescription") var TaskTypeDescription: String? = null,
    @SerializedName("CreatedDate") var CreatedDate: String? = null,
    @SerializedName("CreatedDateText") var CreatedDateText: String? = null,
    @SerializedName("CVclaim__c") var CVclaim_c: Boolean? = null,
    @SerializedName("First_SR_SV_Claimed_date__c") var FirstSRSVClaimedDate_c: String? = null,
    @SerializedName("Voucher_Code__c") var VoucherCode_c: String? = null,
    @SerializedName("SP_Code") var SPCode: String? = null,
    @SerializedName("Service_SP_Code__c") var ServiceSPCode_c: String? = null,
    @SerializedName("House_Type__r") var HouseType_r: HouseType_r? = HouseType_r(),
    @SerializedName("Service_Type") var ServiceType: String? = null,
    @SerializedName("Enable_Payment_Link") var EnablePaymentLink: Boolean? = null,
    @SerializedName("Service_Period") var ServicePeriod: String? = null,
    @SerializedName("Service_Plan_Image_Url") var ServicePlanImageUrl: String? = null,
    @SerializedName("Payment_Subscription_Id__c") var PaymentSubscriptionId_c: String? = null,
    @SerializedName("Payment_Subscription_Plan_Id__c") var PaymentSubscriptionPlanId_c: String? = null,
    @SerializedName("Is_Subscription_Active__c") var IsSubscriptionActive_c: Boolean? = null,
    @SerializedName("Subscription_Payment_Method__c") var SubscriptionPaymentMethod_c: String? = null,
    @SerializedName("Redeem_Hygine_Points__c") var RedeemHyginePoints_c: String? = null,
    @SerializedName("Service_Group__c") var ServiceGroup_c: String? = null,
    @SerializedName("EnableRenewal") var EnableRenewal: Boolean? = null,
    @SerializedName("LastServiceDate") var LastServiceDate: String? = null,
    @SerializedName("NextServiceDate") var NextServiceDate: String? = null


//    @SerializedName("Id") val id: String?,
//    @SerializedName("Name") val name: String?,
//    @SerializedName("Customer_Id__c") val customer_Id__c: String?,
//    @SerializedName("Order_Number__c") val order_Number__c: String?,
//    @SerializedName("Status__c") val status__c: String?,
//    @SerializedName("Unit1__c") val unit1__c: String?,
//    @SerializedName("Sub_Type1__c") val sub_Type1__c: String?,
//    @SerializedName("Account_Types__c") val account_Types__c: String?,
//    @SerializedName("HR_Shipping_Region__r") val hR_Shipping_Region__r: Region?,
//    @SerializedName("Quantity__c") val quantity__c: String?,
//    @SerializedName("Payment_Terms__c") val payment_Terms__c: String?,
//    @SerializedName("End_Date__c") val end_Date__c: String?,
//    @SerializedName("Start_Date__c") val start_Date__c: String?,
//    @SerializedName("Account_Name__r") val account_Name__r: Account?,
//    @SerializedName("Standard_Value__c") val standard_Value__c: Double?,
//    @SerializedName("Order_Value_with_Tax__c") val order_Value_with_Tax__c: Double?,
//    @SerializedName("OrderValueWithTaxAfterDiscount") val orderValueWithTaxAfterDiscount: String?,
//    @SerializedName("OrderDiscountValue") val orderDiscountValue: String?,
//    @SerializedName("Discount_Offered__c") val discount_Offered__c: String?,
//    @SerializedName("Payment_Type__c") val payment_Type__c: String?,
//    @SerializedName("AppointmentStartDateTime__c") val appointmentStartDateTime__c: String?,
//    @SerializedName("AppointmentEndDateTime__c") val appointmentEndDateTime__c: String?,
//    @SerializedName("Service_Plan_Name__c") val service_Plan_Name__c: String?,
//    @SerializedName("Web_ID__c") val web_ID__c: String?,
//    @SerializedName("TaskTypeDescription") val taskTypeDescription: String?,
//    @SerializedName("CreatedDate") val createdDate: String?,
//    @SerializedName("CreatedDateText") val createdDateText: String?,
//    @SerializedName("CVclaim__c") val cVclaim__c: Boolean?,
//    @SerializedName("First_SR_SV_Claimed_date__c") val first_SR_SV_Claimed_date__c: String?,
//    @SerializedName("Voucher_Code__c") val voucher_Code__c: String?,
//    @SerializedName("SP_Code") val sP_Code: String?,
//    @SerializedName("Service_SP_Code__c") val service_SP_Code__c: String?,
//    @SerializedName("House_Type__r") val house_Type__r: House?,
//    @SerializedName("Service_Type") val service_Type: String?,
//    @SerializedName("Enable_Payment_Link") val enable_Payment_Link: Boolean?,
//    @SerializedName("Service_Period") val service_Period: String?,
//    @SerializedName("Service_Plan_Image_Url") val service_Plan_Image_Url: String?,
//    @SerializedName("Payment_Subscription_Id__c") val payment_Subscription_Id__c: String?,
//    @SerializedName("Payment_Subscription_Plan_Id__c") val payment_Subscription_Plan_Id__c: String?,
//    @SerializedName("Is_Subscription_Active__c") var IsSubscriptionActive_c: Boolean? = null,
//    @SerializedName("Subscription_Payment_Method__c") var SubscriptionPaymentMethod_c: String? = null,
//    @SerializedName("Redeem_Hygine_Points__c") var RedeemHyginePoints_c: String? = null,
//    @SerializedName("Service_Group__c") var ServiceGroup_c: String? = null,
//    @SerializedName("EnableRenewal") var EnableRenewal: Boolean? = null,
//    @SerializedName("LastServiceDate") var LastServiceDate: String? = null,
//    @SerializedName("NextServiceDate") var NextServiceDate: String? = null

)