package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName


data class Account (
    @SerializedName("Id") val id : String?,
    @SerializedName("Name") val name : String?,
    @SerializedName("Customer_id__c") val customer_id__c : String?,
    @SerializedName("Customer_Referral_Code__c") val customer_Referral_Code__c : String?,
    @SerializedName("Referral_Amount__c") val referral_Amount__c : String?,
    @SerializedName("Mobile__c") val mobile__c : Int?,
    @SerializedName("Alternate_Mobile__c") val alternate_Mobile__c : String?,
    @SerializedName("Alternate_Phone__c") val alternate_Phone__c : String?,
    @SerializedName("Communication_Mobile_No__c") val communication_Mobile_No__c : String?,
    @SerializedName("Phone") val phone : String?,
    @SerializedName("Account_Types__c") val account_Types__c : String?,
    @SerializedName("Email__c") val email__c : String?,
    @SerializedName("Flat_Number__c") val flat_Number__c : Int?,
    @SerializedName("Building_Name__c") val building_Name__c : String?,
    @SerializedName("Landmark__c") val landmark__c : String?,
    @SerializedName("Locality_Suburb__c") val locality_Suburb__c : String?,
    @SerializedName("BillingStreet") val billingStreet : String?,
    @SerializedName("BillingPostalCode") val billingPostalCode : Int?,
    @SerializedName("BillingCity") val billingCity : String?,
    @SerializedName("BillingState") val billingState : String?,
    @SerializedName("BillingCountry") val billingCountry : String?,
    @SerializedName("Account_Lat__c") val account_Lat__c : String?,
    @SerializedName("Account_Long__c") val account_Long__c : String?,
    @SerializedName("Location__Latitude__s") val location__Latitude__s : String?,
    @SerializedName("Location__Longitude__s") val location__Longitude__s : String?,
    @SerializedName("Salutation__c") val salutation__c : String?,
    @SerializedName("First_Name__c") val first_Name__c : String?,
    @SerializedName("Last_Name__c") val last_Name__c : String?,
    @SerializedName("AccountAddress") val accountAddress : String?,
    @SerializedName("Sub_Type__r") val sub_Type__r : String?,
    @SerializedName("Flat_Type__r") val flat_Type__r : String?,
    @SerializedName("Account_Types__r") val account_Types__r : String?,
    @SerializedName("Locality_Pincode__r") val locality_Pincode__r : String?,
    @SerializedName("Customer_Type__r") val customer_Type__r : String?
)