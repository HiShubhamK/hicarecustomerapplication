package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName
data class ExistingCustomerAddressData (


    @SerializedName("Id"                ) var Id               : Int?     = null,
    @SerializedName("User_Id"           ) var UserId           : Int?     = null,
    @SerializedName("Fname"             ) var Fname            : String?  = null,
    @SerializedName("LastName"          ) var LastName         : String?  = null,
    @SerializedName("Customer_Name"     ) var CustomerName     : String?  = null,
    @SerializedName("Mobile_No"         ) var MobileNo         : String?  = null,
    @SerializedName("Alt_Mobile_No"     ) var AltMobileNo      : String?  = null,
    @SerializedName("Email_Id"          ) var EmailId          : String?  = null,
    @SerializedName("FlatNo"            ) var FlatNo           : String?  = null,
    @SerializedName("BuildingName"      ) var BuildingName     : String?  = null,
    @SerializedName("Locality"          ) var Locality         : String?  = null,
    @SerializedName("Landmark"          ) var Landmark         : String?  = null,
    @SerializedName("Street"            ) var Street           : String?  = null,
    @SerializedName("City"              ) var City             : String?  = null,
    @SerializedName("State"             ) var State            : String?  = null,
    @SerializedName("Lat"               ) var Lat              : String?  = null,
    @SerializedName("Long"              ) var Long             : String?  = null,
    @SerializedName("Pincode"           ) var Pincode          : String?  = null,
    @SerializedName("Is_DeafultAddress" ) var IsDeafultAddress : Boolean? = null
)
