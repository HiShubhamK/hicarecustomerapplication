package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class CustomerLoginInfoData(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("First_Name") var FirstName: String? = null,
    @SerializedName("Last_Name") var LastName: String? = null,
    @SerializedName("Mobile_No") var MobileNo: String? = null,
    @SerializedName("Alt_Mobile_No") var AltMobileNo: String? = null,
    @SerializedName("Communication_No") var CommunicationNo: String? = null,
    @SerializedName("Email") var Email: String? = null,
    @SerializedName("Login_Otp") var LoginOtp: String? = null,
    @SerializedName("Profile_Photo") var ProfilePhoto: String? = null
) {}
