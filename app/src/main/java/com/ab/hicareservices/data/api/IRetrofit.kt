package com.ab.hicareservices.data.api

import com.ab.hicareservices.data.model.NotificationToken
import com.ab.hicareservices.data.model.attachment.AttachmentResponse
import com.ab.hicareservices.data.model.compaintsReason.ComplaintReasons
import com.ab.hicareservices.data.model.complaints.ComplaintResponse
import com.ab.hicareservices.data.model.complaints.CreateComplaint
import com.ab.hicareservices.data.model.getslots.GetSlots
import com.ab.hicareservices.data.model.orderdetails.OrderDetails
import com.ab.hicareservices.data.model.orders.OrdersResponse
import com.ab.hicareservices.data.model.otp.OtpResponse
import com.ab.hicareservices.data.model.otp.ValidateResponse
import com.ab.hicareservices.data.model.payment.SavePaymentResponse
import com.ab.hicareservices.data.model.referral.ReferralResponse
import com.ab.hicareservices.data.model.service.ServiceResponse
import com.ab.hicareservices.data.model.slotcomplaincemodel.GetComplaiceResponce
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IRetrofit {

    @GET("Order/GetAllOrdersByMobileNo")
    fun getOrders(
        @Query("mobileNo") mobileNo: String,
        @Query("orderType") orderType: String
    ): Call<OrdersResponse>

    @GET("Order/GetCustomerOrdersByMobileNo")
    fun getCustomerOrdersByMobileNo(
        @Query("mobileNo") mobileNo: String
    ): Call<OrdersResponse>

    @GET("SMS/SendOTP")
    fun getOtpResponse(
        @Query("mobileNo") mobileNo: String,
    ): Call<OtpResponse>

    @GET("Account/ValidateAccount")
    fun validateAccount(
        @Query("mobileNo") mobileNo: String,
    ): Call<ValidateResponse>

    @GET("Complaint/GetAllComplaintByMobileNo")
    fun getAllComplaints(
        @Query("mobileNo") mobileNo: String,
    ): Call<ComplaintResponse>

    @GET("ServiceRequest/GetServiceRequestByOrderNo")
    fun getServiceRequest(
        @Query("orderNo") mobileNo: String,
        @Query("serviceType") serviceType: String
    ): Call<ServiceResponse>

    @GET("Complaint/GetComplaintReasons")
    fun getComplaintReasonResponse(
        @Query("serviceType") serviceType: String,
    ): Call<ComplaintReasons>

    @POST("Complaint/CreateComplaint")
    fun createComplaintResponse(
        @Body request: HashMap<String, Any>,
    ): Call<CreateComplaint>

    @GET("Account/GetAccountReferralCode")
    fun getReferralCodeResponse(
        @Query("mobileNo") mobileNo: String
    ): Call<ReferralResponse>

    @GET("Order/GetOrderDetailsByOrderNo")
    fun getOrderDetailsByOrderNo(
        @Query("orderNo") orderNo: String,
        @Query("serviceType") serviceType: String
    ): Call<OrderDetails>

    @POST("Payment/SaveAppPaymentDetails")
    fun saveAppPaymentDetails(@Body data: HashMap<String, Any>): Call<SavePaymentResponse>

    @GET("Order/GetAllOrdersByMobileNo")
    fun getCustomerOrdersByMobileNo(
        @Query("mobileNo") mobileNo: String,
        @Query("orderType") orderType: String
    ): Call<OrdersResponse>

    @GET("Notification/SubscribeApplicationAsync")
    fun getNotificationToken(
        @Query("appToken") appToken: String,
    ): Call<NotificationToken>

    @POST("Attachment/UploadAttachment")
    fun UploadAttachment(@Body data: HashMap<String, Any>): Call<AttachmentResponse>

    @POST("Slot/GetSlot")
    fun GetSlot(@Body data: HashMap<String, Any>): Call<GetSlots>
    @POST("Slot/GetSlotCompliance")
    fun getComplainceData(@Body data: HashMap<String, Any>): Call<GetComplaiceResponce>

//    @POST("Slot/GetSlotCompliance")
//    fun getComplainceData(
//        @Query("ServiceCenter_Id") ServiceCenterId: String,
//        @Query("SlotDate") SlotDate: String,
//        @Query("TaskId") TaskId: String,
//        @Query("Lat") Lat: String,
//        @Query("Long") Long: String,
//        @Query("ServiceType") ServiceType: String
//    ): Call<GetComplaiceResponce>

}