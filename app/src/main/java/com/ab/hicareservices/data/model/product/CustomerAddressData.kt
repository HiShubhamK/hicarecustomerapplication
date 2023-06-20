package com.ab.hicareservices.data.model.product

import com.google.gson.annotations.SerializedName

data class CustomerAddressData(
    @SerializedName("Id") var Id: Int? = null,
    @SerializedName("OrderId") var OrderId: Int? = null,
    @SerializedName("Customer_Id") var CustomerId: Int? = null,
    @SerializedName("Flat_No") var FlatNo: String? = null,
    @SerializedName("Building_Name") var BuildingName: String? = null,
    @SerializedName("Street") var Street: String? = null,
    @SerializedName("Locality") var Locality: String? = null,
    @SerializedName("Landmark") var Landmark: String? = null,
    @SerializedName("Pincode") var Pincode: String? = null,
    @SerializedName("City") var City: String? = null,
    @SerializedName("State") var State: String? = null,
    @SerializedName("IsDefault") var IsDefault: Boolean? = null,
    @SerializedName("Address_Lat") var AddressLat: String? = null,
    @SerializedName("Address_Long") var AddressLong: String? = null,
    @SerializedName("AddressType") var AddressType: String? = null,
    @SerializedName("Contact_Person_Name") var ContactPersonName: String? = null,
    @SerializedName("Contact_Person_Mobile") var ContactPersonMobile: String? = null,
    @SerializedName("Contact_Person_Email") var ContactPersonEmail: String? = null,
    @SerializedName("GST_No") var GSTNo: String? = null

) {}
