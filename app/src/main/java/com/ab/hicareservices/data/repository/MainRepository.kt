package com.ab.hicareservices.data.repository

import com.ab.hicareservices.data.api.RetrofitService

class MainRepository {
    private val retrofitService = RetrofitService.getInstance()

    private val retrofitProduct=RetrofitService.getProductInstance()

    fun getOrders(mobileNO: String, type: String) = retrofitService.getOrders(mobileNO, type)
    fun getCustomerOrdersByMobileNo(mobileNo: String) = retrofitService.getCustomerOrdersByMobileNo(mobileNo)
    fun getCustomerOrdersByMobileNo(mobileNo: String,ordertype:String) = retrofitService.getCustomerOrdersByMobileNo(mobileNo,ordertype)
    fun getOtpResponse(mobileNo: String) = retrofitService.getOtpResponse(mobileNo)
    fun validateAccount(mobileNo: String) = retrofitService.validateAccount(mobileNo)
    fun getAllComplaints(mobile: String) = retrofitService.getAllComplaints(mobile)
    fun ProductComplaintListByUserId(userid: Int) = retrofitProduct.ProductComplaintListByUserId(userid)
    fun GetComplaintDetailId(complantid: Int) = retrofitProduct.GetComplaintDetailId(complantid)
    fun getServiceRequest(orderNo: String, type: String) = retrofitService.getServiceRequest(orderNo, type)
    fun getComplaintReasonResponse(serviceType: String) = retrofitService.getComplaintReasonResponse(serviceType)
    fun getReferralCodeResponse(mobileNo: String) = retrofitService.getReferralCodeResponse(mobileNo)
    fun createComplaintResponse(request: HashMap<String, Any>) = retrofitService.createComplaintResponse(request)
    fun getOrderDetailsByOrderNo(orderNo: String, serviceType: String) = retrofitService.getOrderDetailsByOrderNo(orderNo, serviceType)
    fun saveAppPaymentDetails(data: HashMap<String, Any>) = retrofitService.saveAppPaymentDetails(data)
    fun UploadAttachment(data: HashMap<String, Any>) = retrofitService.UploadAttachment(data)
    fun notification(apptoken:String,mobile: String) = retrofitService.getNotificationToken(apptoken,mobile)
    fun lead(serviceType: String) = retrofitService.getLead(serviceType)
    fun GetSlots(data: HashMap<String, Any>) = retrofitService.GetSlot(data)
    fun getComplainceData(data: HashMap<String, Any>) = retrofitService.getComplainceData(data)
    fun BookSlot(data: HashMap<String, Any>) = retrofitService.BookSlot(data)
    fun GetDashboard(mobileNo: String) = retrofitService.GetDashboard(mobileNo)
    fun GetComplaintAttachments(complaintId: String) = retrofitService.GetComplaintAttachments(complaintId)
    fun postLead(data: HashMap<String, Any>) = retrofitService.postLead(data)
    fun getWhatappVerify(watoken: String) = retrofitService.getWhatappVerification(watoken)
    fun getUpcomingScheduledService(mobileNo: String)=retrofitService.getUpcomingScheduledService(mobileNo)
    fun getTodayScheduledService(mobileNo: String)=retrofitService.getTodayScheduledService(mobileNo)
    fun getcustomerloginid(mobileNo: String) = retrofitProduct.getcustomerid(mobileNo)
    fun getcustomerAddress(customerid: Int) = retrofitProduct.getcustomerAddress(customerid)
    fun getordersummeryList(userid: Int) = retrofitProduct.getorderSummeryList(userid)
    fun getAddressDetailById(addressid: Int) = retrofitProduct.getorderSummeryList(addressid)
    fun getproductlist(pincoode:String) = retrofitProduct.getProductlist(pincoode)
    fun getProductDetails(productid:Int,pincode:String,customerid:Int)=retrofitProduct.getProductlistbyId(productid,pincode,customerid)
    fun getAddProductInCart(quantity:Int,productid: Int,userId:Int) = retrofitProduct.getAddProductInCart(quantity,productid,userId)
    fun getProductCountInCar(userId: Int)=retrofitProduct.getProductCountInCar(userId)
    fun getProductCartByUserId(userId: Int)=retrofitProduct.getProductCartByUserId(userId)
    fun getCartSummary(userId: Int,pincoode: String,vouchercode:String)=retrofitProduct.getCartSummary(userId,pincoode,vouchercode)
    fun getDeleteProductCart(cartId:Int,userId: Int)=retrofitProduct.getDeleteProductCart(cartId,userId)
    fun postSaveAddress(data: HashMap<String, Any>)=retrofitProduct.postSaveAddress(data)
    fun  getaddressdetailbyid(addressId:Int)=retrofitProduct.getaddressdetailbyid(addressId)
    fun postSaveSalesOrder(data: HashMap<String, Any>)=retrofitProduct.postSaveSalesOrder(data)
    fun CreateProductComplaint(data: HashMap<String, Any>)=retrofitProduct.CreateProductComplaint(data)
    fun CreateEventForMobileAppNotification(data: HashMap<String, Any>)=retrofitService.CreateEventForMobileAppNotification(data)
    fun CreateRazorpayOrderId(amount:Double,mobilenumber:Int) = retrofitProduct.CreateRazorpayOrderId(amount,mobilenumber)
    fun getValidateVoucher(vouchercode: String,userId: Int,pincoode: String)=retrofitProduct.getValidateVoucher(vouchercode,userId,pincoode)
    fun getClearCache(mobileNo: String)=retrofitService.getClearCache(mobileNo)
}