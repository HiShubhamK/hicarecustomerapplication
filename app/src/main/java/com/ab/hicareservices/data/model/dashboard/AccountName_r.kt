package com.ab.hicareservices.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class AccountName_r(
    @SerializedName("Account_Lat__c") var AccountLat_c: String? = null,
    @SerializedName("Account_Long__c") var AccountLong_c: String? = null,
    @SerializedName("Account_Types__c") var AccountTypes_c: String? = null,
    @SerializedName("Account_Types__r") var AccountTypes_r: String? = null,
    @SerializedName("AccountAddress") var AccountAddress: String? = null,
    @SerializedName("Alternate_Mobile__c") var AlternateMobile_c: String? = null,
    @SerializedName("Alternate_Phone__c") var AlternatePhone_c: String? = null,
    @SerializedName("BillingCity") var BillingCity: String? = null,
    @SerializedName("BillingCountry") var BillingCountry: String? = null,
    @SerializedName("BillingPostalCode") var BillingPostalCode: String? = null,
    @SerializedName("BillingState") var BillingState: String? = null,
    @SerializedName("BillingStreet") var BillingStreet: String? = null,
    @SerializedName("Building_Name__c") var BuildingName_c: String? = null,
    @SerializedName("Communication_Mobile_No__c") var CommunicationMobileNo_c: String? = null,
    @SerializedName("Customer_id__c") var CustomerId_c: String? = null,
    @SerializedName("Customer_Referral_Code__c") var CustomerReferralCode_c: String? = null,
    @SerializedName("Customer_Type__r") var CustomerType_r: String? = null,
    @SerializedName("Email__c") var Email_c: String? = null,
    @SerializedName("First_Name__c") var FirstName_c: String? = null,
    @SerializedName("Flat_Number__c") var FlatNumber_c: String? = null,
    @SerializedName("Flat_Type__r") var FlatType_r: String? = null,
    @SerializedName("Id") var Id: String? = null,
    @SerializedName("Landmark__c") var Landmark_c: String? = null,
    @SerializedName("Last_Name__c") var LastName_c: String? = null,
    @SerializedName("Locality_Pincode__r") var LocalityPincode_r: LocalityPincode_r? = LocalityPincode_r(),
    @SerializedName("Locality_Suburb__c") var LocalitySuburb_c: String? = null,
    @SerializedName("Location__Latitude__s") var Location_Latitude_s: String? = null,
    @SerializedName("Location__Longitude__s") var Location_Longitude_s: String? = null,
    @SerializedName("Mobile__c") var Mobile_c: String? = null,
    @SerializedName("Name") var Name: String? = null,
    @SerializedName("Phone") var Phone: String? = null,
    @SerializedName("Referral_Amount__c") var ReferralAmount_c: String? = null,
    @SerializedName("Salutation__c") var Salutation_c: String? = null,
    @SerializedName("Sub_Type__r") var SubType_r: String? = null

) {}
