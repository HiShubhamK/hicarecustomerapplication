package com.ab.hicareservices.data.api

import com.ab.hicareservices.data.model.*
import com.ab.hicareservices.data.model.attachment.AttachmentResponse
import com.ab.hicareservices.data.model.attachment.GetAttachmentResponse
import com.ab.hicareservices.data.model.bookslot.BookSlotResponce
import com.ab.hicareservices.data.model.compaintsReason.ComplaintReasons
import com.ab.hicareservices.data.model.complaints.ComplaintResponse
import com.ab.hicareservices.data.model.complaints.CreateComplaint
import com.ab.hicareservices.data.model.dashboard.DashboardModel
import com.ab.hicareservices.data.model.dashboard.ScheduledService
import com.ab.hicareservices.data.model.getaddressdetailbyidmodel.AddressByCustomerModel
import com.ab.hicareservices.data.model.getslots.GetSlots
import com.ab.hicareservices.data.model.orderdetails.OrderDetails
import com.ab.hicareservices.data.model.orders.OrdersResponse
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryResponse
import com.ab.hicareservices.data.model.otp.OtpResponse
import com.ab.hicareservices.data.model.otp.ValidateResponse
import com.ab.hicareservices.data.model.payment.SavePaymentResponse
import com.ab.hicareservices.data.model.product.*
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
    fun getOrders(@Query("mobileNo") mobileNo: String, @Query("orderType") orderType: String): Call<OrdersResponse>

    @GET("Order/GetCustomerOrdersByMobileNo")
    fun getCustomerOrdersByMobileNo(@Query("mobileNo") mobileNo: String): Call<OrdersResponse>

    @GET("SMS/SendOTP")
    fun getOtpResponse(@Query("mobileNo") mobileNo: String): Call<OtpResponse>

    @GET("Account/ValidateAccount")
    fun validateAccount(@Query("mobileNo") mobileNo: String): Call<ValidateResponse>

    @GET("Complaint/GetAllComplaintByMobileNo")
    fun getAllComplaints(@Query("mobileNo") mobileNo: String): Call<ComplaintResponse>

    @GET("ServiceRequest/GetServiceRequestByOrderNo")
    fun getServiceRequest(@Query("orderNo") mobileNo: String, @Query("serviceType") serviceType: String): Call<ServiceResponse>

    @GET("Complaint/GetComplaintReasons")
    fun getComplaintReasonResponse(@Query("serviceType") serviceType: String): Call<ComplaintReasons>

    @POST("Complaint/CreateComplaint")
    fun createComplaintResponse(@Body request: HashMap<String, Any>): Call<CreateComplaint>

    @GET("Account/GetAccountReferralCode")
    fun getReferralCodeResponse(@Query("mobileNo") mobileNo: String): Call<ReferralResponse>

    @GET("Order/GetOrderDetailsByOrderNo")
    fun getOrderDetailsByOrderNo(@Query("orderNo") orderNo: String, @Query("serviceType") serviceType: String): Call<OrderDetails>

    @POST("Payment/SaveAppPaymentDetails")
    fun saveAppPaymentDetails(@Body data: HashMap<String, Any>): Call<SavePaymentResponse>

    @GET("Order/GetAllOrdersByMobileNo")
    fun getCustomerOrdersByMobileNo(@Query("mobileNo") mobileNo: String, @Query("orderType") orderType: String): Call<OrdersResponse>

    @GET("Notification/SubscribeApplicationAsync")
    fun getNotificationToken(@Query("appToken") appToken: String): Call<NotificationToken>

    @GET("Lead/GetInterestedServices")
    fun getLead(@Query("serviceType") serviceType: String): Call<leadResopnse>

    @POST("Attachment/UploadAttachment")
    fun UploadAttachment(@Body data: HashMap<String, Any>): Call<AttachmentResponse>

    @POST("Slot/GetSlot")
    fun GetSlot(@Body data: HashMap<String, Any>): Call<GetSlots>

    @POST("Slot/GetSlotCompliance")
    fun getComplainceData(@Body data: HashMap<String, Any>): Call<GetComplaiceResponce>

    @POST("Slot/BookSlot")
    fun BookSlot(@Body data: HashMap<String, Any>): Call<BookSlotResponce>

    @POST("Dashboard/GetDashboard")
    fun GetDashboard(@Query("mobileNo") mobileNo: String): Call<DashboardModel>

    @GET("Complaint/GetComplaintAttachment")
    fun GetComplaintAttachments(@Query("complaintId") complaintId: String): Call<GetAttachmentResponse>

    @POST("Lead/AddLeadAsync")
    fun postLead(@Body data: HashMap<String, Any>): Call<LeadResponse>

    @GET("Account/VerifyWhatsAppUser")
    fun getWhatappVerification(@Query("waToken") waToken:String):Call<Whatappresponse>

    @GET("ServiceRequest/GetUpcomingScheduledService")
    fun getUpcomingScheduledService(@Query("mobileNo") mobileNo: String): Call<ScheduledService>

    @GET("ServiceRequest/GetTodayScheduledService")
    fun getTodayScheduledService(@Query("mobileNo") mobileNo: String): Call<ScheduledService>

    @GET("CustomerAddress/GetCustomerLoginInfo")
    fun getcustomerid(@Query("mobileno") mobileno:String):Call<CustomerLoginInfo>

    @GET("Address/GetCustomerAddressByCustomerId")
    fun getcustomerAddress(@Query("customerId") customerId:Int):Call<CustomerAddress>

    @GET("Product/GetProductListByPincode")
    fun getProductlist(@Query("pincode") pincode: String):Call<ProductListResponse>

    @GET("Product/GetProductDetailById")
    fun getProductlistbyId(@Query("productId") productId: Int,
                           @Query("pincode") pincode: String,
                           @Query("userId") userId: Int):Call<ProducDetailsResponse>

    @GET("Cart/AddProductInCart")
    fun getAddProductInCart(@Query("quantity") quantity: Int,
                           @Query("productId") productId: Int,
                           @Query("userId") userId: Int):Call<AddProductInCart>


    @GET("Cart/GetProductCountInCart")
    fun getProductCountInCar(@Query("userId") userId: Int):Call<ProductCount>

    @GET("Cart/GetProductCartByUserId")
    fun getProductCartByUserId(@Query("userId") userId: Int) : Call<CartlistResponse>

    @GET("Cart/GetCartSummary")
    fun getCartSummary(@Query("userId") userId: Int,
                       @Query("pincode") pincode: String,
                       @Query("voucherCode") voucherCode:String):Call<GetCartSummaryResponse>

    @GET("Cart/DeleteProductInCart")
    fun getDeleteProductCart(@Query("cartId") cartId:Int,@Query("userId") userId: Int):Call<DeleteProductInCart>

    @POST("Address/SaveAddress")
    fun postSaveAddress(@Body data: HashMap<String, Any>): Call<SaveAddressResponse>


    @GET("Order/GetOrderList")
    fun getorderSummeryList(@Query("userId") userId: Int): Call<OrderSummeryResponse>

    @GET("Address/GetAddressDetailById")
    fun getaddressdetailbyid(@Query("addressId") addressId: Int): Call<AddressByCustomerModel>

    @POST("Order/SaveSalesOrder")
    fun postSaveSalesOrder(@Body data: HashMap<String, Any>): Call<SaveSalesResponse>

}