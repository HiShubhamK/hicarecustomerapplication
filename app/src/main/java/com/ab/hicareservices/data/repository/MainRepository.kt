package com.ab.hicareservices.data.repository

import com.ab.hicareservices.data.api.RetrofitService


class MainRepository {
    private val retrofitService = RetrofitService.getInstance()

    fun getOrders(mobileNO: String, type: String) = retrofitService.getOrders(mobileNO, type)
    fun getCustomerOrdersByMobileNo(mobileNo: String) = retrofitService.getCustomerOrdersByMobileNo(mobileNo)
    fun getCustomerOrdersByMobileNo(mobileNo: String,ordertype:String) = retrofitService.getCustomerOrdersByMobileNo(mobileNo,ordertype)

    fun getOtpResponse(mobileNo: String) = retrofitService.getOtpResponse(mobileNo)
    fun validateAccount(mobileNo: String) = retrofitService.validateAccount(mobileNo)
    fun getAllComplaints(mobile: String) = retrofitService.getAllComplaints(mobile)
    fun getServiceRequest(orderNo: String, type: String) = retrofitService.getServiceRequest(orderNo, type)
    fun getComplaintReasonResponse(serviceType: String) = retrofitService.getComplaintReasonResponse(serviceType)
    fun getReferralCodeResponse(mobileNo: String) = retrofitService.getReferralCodeResponse(mobileNo)
    fun createComplaintResponse(request: HashMap<String, Any>) = retrofitService.createComplaintResponse(request)
    fun getOrderDetailsByOrderNo(orderNo: String, serviceType: String) =
        retrofitService.getOrderDetailsByOrderNo(orderNo, serviceType)
    fun saveAppPaymentDetails(data: HashMap<String, Any>) = retrofitService.saveAppPaymentDetails(data)
    fun UploadAttachment(data: HashMap<String, Any>) = retrofitService.UploadAttachment(data)
    fun notification(apptoken:String) = retrofitService.getNotificationToken(apptoken)
    fun GetSlots(data: HashMap<String, Any>) = retrofitService.GetSlot(data)
    fun getComplainceData(ServiceCenter_Id:String,SlotDate:String,TaskId:String,Lat:String,Long:String,ServiceType:String) = retrofitService.getComplainceData(ServiceCenter_Id,SlotDate,TaskId, Lat, Long, ServiceType)

}