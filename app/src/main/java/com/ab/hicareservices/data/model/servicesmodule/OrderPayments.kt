package com.ab.hicareservices.data.model.servicesmodule

import com.google.gson.annotations.SerializedName

data class OrderPayments (
  @SerializedName("PaymentMode"   ) var PaymentMode   : String? = null,
  @SerializedName("PaymentMethod" ) var PaymentMethod : String? = null,
  @SerializedName("Amount"        ) var Amount        : String? = null,
  @SerializedName("TransactionId" ) var TransactionId : String? = null,
  @SerializedName("Type"          ) var Type          : String? = null

)