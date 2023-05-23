package com.hc.hicareservices.data.model.complaints

import com.google.gson.annotations.SerializedName


data class Account(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("customer_id__c") var customerId_c: String? = null,
    @SerializedName("customer_Referral_Code__c") var customerReferralCode_c: String? = null,
    @SerializedName("referral_Amount__c") var referralAmount_c: String? = null,
    @SerializedName("mobile__c") var mobile_c: String? = null,
    @SerializedName("alternate_Mobile__c") var alternateMobile_c: String? = null,
    @SerializedName("alternate_Phone__c") var alternatePhone_c: String? = null,
    @SerializedName("communication_Mobile_No__c") var communicationMobileNo_c: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("account_Types__c") var accountTypes_c: String? = null,
    @SerializedName("email__c") var email_c: String? = null,
    @SerializedName("flat_Number__c") var flatNumber_c: String? = null,
    @SerializedName("building_Name__c") var buildingName_c: String? = null,
    @SerializedName("landmark__c") var landmark_c: String? = null,
    @SerializedName("locality_Suburb__c") var localitySuburb_c: String? = null,
    @SerializedName("billingStreet") var billingStreet: String? = null,
    @SerializedName("billingPostalCode") var billingPostalCode: String? = null,
    @SerializedName("billingCity") var billingCity: String? = null,
    @SerializedName("billingState") var billingState: String? = null,
    @SerializedName("billingCountry") var billingCountry: String? = null,
    @SerializedName("account_Lat__c") var accountLat_c: String? = null,
    @SerializedName("account_Long__c") var accountLong_c: String? = null,
    @SerializedName("location__Latitude__s") var location_Latitude_s: String? = null,
    @SerializedName("location__Longitude__s") var location_Longitude_s: String? = null,
    @SerializedName("salutation__c") var salutation_c: String? = null,
    @SerializedName("first_Name__c") var firstName_c: String? = null,
    @SerializedName("last_Name__c") var lastName_c: String? = null,
    @SerializedName("accountAddress") var accountAddress: String? = null,
    @SerializedName("sub_Type__r") var subType_r: String? = null,
    @SerializedName("flat_Type__r") var flatType_r: String? = null,
    @SerializedName("account_Types__r") var accountTypes_r: String? = null,
    @SerializedName("locality_Pincode__r") var localityPincode_r: String? = null,
    @SerializedName("customer_Type__r") var customerType_r: String? = null

)