package com.ab.hicareservices.data.model.savesalesmodel

import com.google.gson.annotations.SerializedName

data class SaveSalesResponseModel(
    @SerializedName("HomeProduct") var HomeProduct: ArrayList<HomeProduct> = arrayListOf(),
    @SerializedName("CustomerAddress") var CustomerAddress: CustomerAddress? = CustomerAddress(),
    @SerializedName("AddressId") var AddressId: Int? = null,
    @SerializedName("BillToAddressId") var BillToAddressId: Int? = null,
    @SerializedName("Pincode") var Pincode: String? = null,
    @SerializedName("CartAmount") var CartAmount: Int? = null,
    @SerializedName("PayableAmount") var PayableAmount: Int? = null,
    @SerializedName("DiscountAmount") var DiscountAmount: Int? = null,
    @SerializedName("DelieveryCharges") var DelieveryCharges: Int? = null,
    @SerializedName("InstallationCharges") var InstallationCharges: Int? = null,
    @SerializedName("VoucherCode") var VoucherCode: String? = null,
    @SerializedName("SFDC_OrderNo") var SFDCOrderNo: String? = null,
    @SerializedName("PaymentId") var PaymentId: String? = null,
    @SerializedName("PayMethod") var PayMethod: String? = null,
    @SerializedName("PayStatus") var PayStatus: String? = null,
    @SerializedName("PayAmount") var PayAmount: Int? = null,
    @SerializedName("Booking_Source") var BookingSource: String? = null,
    @SerializedName("Referred_By_Technician") var ReferredByTechnician: String? = null,
    @SerializedName("Order_Source") var OrderSource: String? = null,
    @SerializedName("Payment_LinkId") var PaymentLinkId: String? = null,
    @SerializedName("Razorpay_Payment_Id") var RazorpayPaymentId: String? = null,
    @SerializedName("User_Id") var UserId: Int? = null
)