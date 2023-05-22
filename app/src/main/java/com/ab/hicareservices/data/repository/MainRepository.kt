package com.ab.hicareservices.data.repository

import com.ab.hicareservices.data.api.RetrofitService


class MainRepository {
    private val retrofitService = RetrofitService.getInstance()

    fun getOrders(mobileNO: String, type: String) = retrofitService.getOrders(mobileNO, type)
    fun getCustomerOrdersByMobileNo(mobileNo: String) = retrofitService.getCustomerOrdersByMobileNo(mobileNo)
    fun getOtpResponse(mobileNo: String) = retrofitService.getOtpResponse(mobileNo)
    fun validateAccount(mobileNo: String) = retrofitService.validateAccount(mobileNo)
    fun getAllComplaints(mobile: String) = retrofitService.getAllComplaints(mobile)
    fun getServiceRequest(orderNo: String, type: String) = retrofitService.getServiceRequest(orderNo, type)
    fun getComplaintReasonResponse(serviceType: String) = retrofitService.getComplaintReasonResponse(serviceType)
    fun getReferralCodeResponse(mobileNo: String) = retrofitService.getReferralCodeResponse(mobileNo)
    fun createComplaintResponse(request: HashMap<String, String>) = retrofitService.createComplaintResponse(request)
    fun getOrderDetailsByOrderNo(orderNo: String, serviceType: String) =
        retrofitService.getOrderDetailsByOrderNo(orderNo, serviceType)
    fun saveAppPaymentDetails(data: HashMap<String, Any>) = retrofitService.saveAppPaymentDetails(data)
}