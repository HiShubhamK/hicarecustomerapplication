package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.CreateEventNotificationResponse
import com.ab.hicareservices.data.model.GetCreateRazorpayProductOrderIdResponse
import com.ab.hicareservices.data.model.SaveSalesResponse
import com.ab.hicareservices.data.model.ValidateVoucherResponse
import com.ab.hicareservices.data.model.getaddressdetailbyidmodel.AddressByCustomerModel
import com.ab.hicareservices.data.model.getaddressdetailbyidmodel.AddressByDetailIdData
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryResponse
import com.ab.hicareservices.data.model.product.*
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel: ViewModel() {
    val repository = MainRepository()
    val customerlogininfo = MutableLiveData<CustomerLoginInfo>()
    val cutomeraddress= MutableLiveData<List<CustomerAddressData>>()
    val productlist= MutableLiveData<List<ProductListResponseData>>()
    val errorMessage = MutableLiveData<String>()
    val producDetailsResponse=MutableLiveData<ProducDetailsData>()
    val addtocart=MutableLiveData<AddProductInCart>()
    val productcount = MutableLiveData<ProductCount>()
    val cartlist=MutableLiveData<List<CartlistResponseData>>()
    val getsummarydata=MutableLiveData<GetCartSummaryData>()
    val getDeleteProductCart=MutableLiveData<DeleteProductInCart>()
    val getsaveaddressresponse = MutableLiveData<SaveAddressResponse>()
    val SaveServiceAddress = MutableLiveData<SaveAddressResponse>()
    val getordersummeryList= MutableLiveData<List<OrderSummeryData>>()
    val getaddressbydetailid= MutableLiveData<AddressByDetailIdData>()
    val razorpayOrderIdResponse = MutableLiveData<RazorpayOrderIdResponse>()
    val validateVoucherResponse = MutableLiveData<ValidateVoucherResponse>()
    val paymentsuceess = MutableLiveData<SaveSalesResponse>()
    val CreateEventNotificationResponse = MutableLiveData<CreateEventNotificationResponse>()
    val responseMessage = MutableLiveData<String>()
    val clearCacheResponse=MutableLiveData<ClearCacheResponse>()
    val GetCreateRazorpayProductOrderIdResponse=MutableLiveData<GetCreateRazorpayProductOrderIdResponse>()



    fun getCustomerid(mobileno: String) {
        val response = repository.getcustomerloginid(mobileno)
        response.enqueue(object : Callback<CustomerLoginInfo> {
            override fun onResponse(call: Call<CustomerLoginInfo>, response: Response<CustomerLoginInfo>) {
                if(response.body()!!.IsSuccess==true) {
                    customerlogininfo.postValue(response.body())
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<CustomerLoginInfo>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getCustomerAddress(customerid: Int) {
        val response = repository.getcustomerAddress(customerid)
        response.enqueue(object : Callback<CustomerAddress> {
            override fun onResponse(call: Call<CustomerAddress>, response: Response<CustomerAddress>) {
                if(response.body()!!.IsSuccess==true) {
                cutomeraddress.postValue(response.body()!!.Data)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<CustomerAddress>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }



    fun getProductlist(pincode: String) {
        val response = repository.getproductlist(pincode)
        response.enqueue(object : Callback<ProductListResponse> {
            override fun onResponse(call: Call<ProductListResponse>, response: Response<ProductListResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    productlist.postValue(response.body()!!.Data)
                }else{

                    responseMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getProductDetails(productid:Int,pincode: String,customerid: Int){
        val response=repository.getProductDetails(productid,pincode,customerid)
        response.enqueue(object  : Callback<ProducDetailsResponse>{
            override fun onResponse(call: Call<ProducDetailsResponse>, response: Response<ProducDetailsResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    producDetailsResponse.postValue(response.body()!!.Data!!)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<ProducDetailsResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }


    fun getAddProductInCart(quantity:Int,productid:Int,customerid:Int){
        val response=repository.getAddProductInCart(quantity,productid,customerid)
        response.enqueue(object : Callback<AddProductInCart>{
            override fun onResponse(call: Call<AddProductInCart>, response: Response<AddProductInCart>) {
                if(response.body()!!.IsSuccess==true) {
                    addtocart.postValue(response.body())
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<AddProductInCart>, t: Throwable) {
               errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }

    fun getProductCountInCar(userid:Int){
        val response = repository.getProductCountInCar(userid)
        response.enqueue(object : Callback<ProductCount>{
            override fun onResponse(call: Call<ProductCount>, response: Response<ProductCount>) {
                if(response.body()!!.IsSuccess==true) {
                    productcount.postValue(response.body())
                }else{
                    errorMessage.postValue(response.body()!!.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<ProductCount>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getProductCartByUserId(userid: Int,pincode: String){
        val response = repository.getProductCartByUserId(userid,pincode)
        response.enqueue(object : Callback<CartlistResponse>{
            override fun onResponse(call: Call<CartlistResponse>, response: Response<CartlistResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    cartlist.postValue(response.body()!!.Data)
                }else{
                    responseMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<CartlistResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getCartSummary(userid: Int,pincode: String,vouchercode:String){
        val response=repository.getCartSummary(userid,pincode,vouchercode)
        response.enqueue(object : Callback<GetCartSummaryResponse>{
            override fun onResponse(
                call: Call<GetCartSummaryResponse>,
                response: Response<GetCartSummaryResponse>
            ) {
                if(response.body()!!.IsSuccess ==true){
                    getsummarydata.postValue(response.body()!!.Data!!)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<GetCartSummaryResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }

    fun getDeleteProductCart(cartId:Int,userid: Int){
        val response=repository.getDeleteProductCart(cartId,userid)
        response.enqueue(object : Callback<DeleteProductInCart>{
            override fun onResponse(
                call: Call<DeleteProductInCart>,
                response: Response<DeleteProductInCart>
            ) {
                getDeleteProductCart.postValue(response.body())
            }

            override fun onFailure(call: Call<DeleteProductInCart>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }

    fun postSaveAddress(data: HashMap<String, Any>){
        val response=repository.postSaveAddress(data)
        response.enqueue(object :Callback<SaveAddressResponse>{
            override fun onResponse(
                call: Call<SaveAddressResponse>,
                response: Response<SaveAddressResponse>
            ) {
                getsaveaddressresponse.postValue(response.body())
            }

            override fun onFailure(call: Call<SaveAddressResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }
    fun getOrderSummeryList(userid: Int) {
        val response = repository.getordersummeryList(userid)
        response.enqueue(object : Callback<OrderSummeryResponse> {
            override fun onResponse(call: Call<OrderSummeryResponse>, response: Response<OrderSummeryResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    if(response.body()!!.orderSummeryData!=null) {
                        getordersummeryList.postValue(response.body()!!.orderSummeryData)
                    }else{
                        responseMessage.postValue(response.body()!!.ResponseMessage!!)
                    }
                }else{
                    responseMessage.postValue(response.body()!!.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<OrderSummeryResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
            }
        })
    }
    fun getAddressDetailbyId(addressid: Int) {
        val response = repository.getaddressdetailbyid(addressid)
        response.enqueue(object : Callback<AddressByCustomerModel> {
            override fun onResponse(call: Call<AddressByCustomerModel>, response: Response<AddressByCustomerModel>) {
                getaddressbydetailid.postValue(response.body()!!.Data!!)
            }
            override fun onFailure(call: Call<AddressByCustomerModel>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun postSaveSalesOrder(data: HashMap<String, Any>){
        val response=repository.postSaveSalesOrder(data)
        response.enqueue(object :Callback<SaveSalesResponse>{
            override fun onResponse(
                call: Call<SaveSalesResponse>,
                response: Response<SaveSalesResponse>
            ) {
                paymentsuceess.postValue(response.body())
            }

            override fun onFailure(call: Call<SaveSalesResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }
    fun CreateEventForMobileAppNotification(data: HashMap<String, Any>){
        val response=repository.CreateEventForMobileAppNotification(data)
        response.enqueue(object :Callback<CreateEventNotificationResponse>{
            override fun onResponse(
                call: Call<CreateEventNotificationResponse>,
                response: Response<CreateEventNotificationResponse>
            ) {
                CreateEventNotificationResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<CreateEventNotificationResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun CreateRazorpayOrderId(amount:Double,mobilenumber:Int){
        val response=repository.CreateRazorpayOrderId(amount,mobilenumber)
        response.enqueue(object  : Callback<RazorpayOrderIdResponse>{
            override fun onResponse(call: Call<RazorpayOrderIdResponse>,
                                    response: Response<RazorpayOrderIdResponse>) {

                razorpayOrderIdResponse.postValue(response.body())

            }

            override fun onFailure(call: Call<RazorpayOrderIdResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getValidateVoucher(vouchercode: String,userid: Int,pincode: String){
        val response=repository.getValidateVoucher(vouchercode,userid,pincode)
        response.enqueue(object : Callback<ValidateVoucherResponse>{
            override fun onResponse(call: Call<ValidateVoucherResponse>, response: Response<ValidateVoucherResponse>) {
                validateVoucherResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<ValidateVoucherResponse>, t: Throwable) {
                    errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }

    fun getClearCache(mobileno: String){
        val response=repository.getClearCache(mobileno)
        response.enqueue(object : Callback<ClearCacheResponse>{
            override fun onResponse(call: Call<ClearCacheResponse>, response: Response<ClearCacheResponse>) {
                clearCacheResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<ClearCacheResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }

    fun GetCreateRazorpayProductOrderId(toString: HashMap<String, Any>) {
        val response=repository.GetCreateRazorpayProductOrderId(toString)
        response.enqueue(object : Callback<GetCreateRazorpayProductOrderIdResponse>{
            override fun onResponse(call: Call<GetCreateRazorpayProductOrderIdResponse>, response: Response<GetCreateRazorpayProductOrderIdResponse>) {
                GetCreateRazorpayProductOrderIdResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<GetCreateRazorpayProductOrderIdResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }
}

