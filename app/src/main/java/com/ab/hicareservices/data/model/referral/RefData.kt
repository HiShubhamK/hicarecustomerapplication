package com.ab.hicareservices.data.model.referral

import com.google.gson.annotations.SerializedName

data class RefData(
    @SerializedName("Referral_Code"     ) var ReferralCode     : String? = null,
    @SerializedName("Referral_Discount" ) var ReferralDiscount : String? = null,
    @SerializedName("PrimaryMessage"    ) var PrimaryMessage   : String? = null,
    @SerializedName("SecondaryMessage"  ) var SecondaryMessage : String? = null,
    @SerializedName("ShareText"         ) var ShareText        : String? = null,
    @SerializedName("WAShareText"       ) var WAShareText      : String? = null,
    @SerializedName("FBShareText"       ) var FBShareText      : String? = null,
    @SerializedName("TwitterShareText"  ) var TwitterShareText : String? = null,
    @SerializedName("OtherShareText"    ) var OtherShareText   : String? = null

)
