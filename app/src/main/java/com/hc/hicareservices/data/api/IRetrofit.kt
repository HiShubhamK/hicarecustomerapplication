package com.hc.hicareservices.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import com.google.gson.GsonBuilder
import com.hc.hicareservices.data.SharedPreferenceUtil
import com.hc.hicareservices.data.model.complaints.ComplaintResponse
import com.hc.hicareservices.data.model.orders.OrdersResponse
import com.hc.hicareservices.data.model.otp.OtpResponse
import com.hc.hicareservices.data.model.service.ServiceResponse
import com.hc.hicareservices.data.model.compaintsReason.ComplaintReasons
import com.hc.hicareservices.data.model.complaints.CreateComplaint
import com.hc.hicareservices.data.model.orderdetails.OrderDetails
import com.hc.hicareservices.data.model.otp.ValidateResponse
import com.hc.hicareservices.data.model.payment.SavePaymentResponse
import com.hc.hicareservices.data.model.referral.ReferralResponse
import com.hc.hicareservices.utils.AppUtils2
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit

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
        @Body request: HashMap<String, String>,
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

}