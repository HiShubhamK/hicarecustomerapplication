package com.ab.hicareservices.data.model.savesalesmodel

import com.google.gson.annotations.SerializedName

data class CustomerAddress(

    @SerializedName("BillTo_Contact_Person_Name") var BillToContactPersonName: String? = null,
    @SerializedName("BillTo_Contact_Person_Mobile") var BillToContactPersonMobile: String? = null,
    @SerializedName("BillTo_Contact_Person_Email") var BillToContactPersonEmail: String? = null,
    @SerializedName("BillTo_AddressType") var BillToAddressType: String? = null,
    @SerializedName("BillTo_Flat_No") var BillToFlatNo: String? = null,
    @SerializedName("BillTo_Building_Name") var BillToBuildingName: String? = null,
    @SerializedName("BillTo_Street") var BillToStreet: String? = null,
    @SerializedName("BillTo_Locality") var BillToLocality: String? = null,
    @SerializedName("BillTo_Landmark") var BillToLandmark: String? = null,
    @SerializedName("BillTo_Pincode") var BillToPincode: String? = null,
    @SerializedName("BillTo_City") var BillToCity: String? = null,
    @SerializedName("BillTo_GSTNo") var BillToGSTNo: String? = null,
    @SerializedName("BillTo_IsDefault") var BillToIsDefault: Boolean? = null,
    @SerializedName("ShipTo_Contact_Person_Name") var ShipToContactPersonName: String? = null,
    @SerializedName("ShipTo_Contact_Person_Mobile") var ShipToContactPersonMobile: String? = null,
    @SerializedName("ShipTo_Contact_Person_Email") var ShipToContactPersonEmail: String? = null,
    @SerializedName("ShipTo_AddressType") var ShipToAddressType: String? = null,
    @SerializedName("ShipTo_Flat_No") var ShipToFlatNo: String? = null,
    @SerializedName("ShipTo_Building_Name") var ShipToBuildingName: String? = null,
    @SerializedName("ShipTo_Street") var ShipToStreet: String? = null,
    @SerializedName("ShipTo_Locality") var ShipToLocality: String? = null,
    @SerializedName("ShipTo_Landmark") var ShipToLandmark: String? = null,
    @SerializedName("ShipTo_Pincode") var ShipToPincode: String? = null,
    @SerializedName("ShipTo_City") var ShipToCity: String? = null,
    @SerializedName("Pincode") var Pincode: String? = null,
    @SerializedName("ShipTo_GSTNo") var ShipToGSTNo: String? = null,
    @SerializedName("AddressId") var AddressId: Int? = null,
    @SerializedName("BillToAddressId") var BillToAddressId: Int? = null
)