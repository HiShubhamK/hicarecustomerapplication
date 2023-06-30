package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.SaveSalesResponse
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
    val getordersummeryList= MutableLiveData<List<OrderSummeryData>>()
    val getaddressbydetailid= MutableLiveData<AddressByDetailIdData>()

    fun getCustomerid(mobileno: String) {
        val response = repository.getcustomerloginid(mobileno)
        response.enqueue(object : Callback<CustomerLoginInfo> {
            override fun onResponse(call: Call<CustomerLoginInfo>, response: Response<CustomerLoginInfo>) {
                if(response.body()!!.IsSuccess==true) {
                    customerlogininfo.postValue(response.body())
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage)
                }
            }
            override fun onFailure(call: Call<CustomerLoginInfo>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
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
                    errorMessage.postValue(response.body()?.ResponseMessage)
                }
            }
            override fun onFailure(call: Call<CustomerAddress>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
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
                    errorMessage.postValue(response.body()?.ResponseMessage)
                }
            }
            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
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
                    errorMessage.postValue(response.body()?.ResponseMessage)
                }
            }

            override fun onFailure(call: Call<ProducDetailsResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
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
                    errorMessage.postValue(response.body()?.ResponseMessage)
                }
            }

            override fun onFailure(call: Call<AddProductInCart>, t: Throwable) {
               errorMessage.postValue("Something went to wrong")
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
                    errorMessage.postValue(response.body()!!.ResponseMessage)
                }
            }

            override fun onFailure(call: Call<ProductCount>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
            }
        })
    }

    fun getProductCartByUserId(userid: Int){
        val response = repository.getProductCartByUserId(userid)
        response.enqueue(object : Callback<CartlistResponse>{
            override fun onResponse(call: Call<CartlistResponse>, response: Response<CartlistResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    cartlist.postValue(response.body()!!.Data)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage)
                }
            }

            override fun onFailure(call: Call<CartlistResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
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
                    errorMessage.postValue(response.body()?.ResponseMessage)
                }
            }

            override fun onFailure(call: Call<GetCartSummaryResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
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
                errorMessage.postValue("Something went to wrong")
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
                errorMessage.postValue("Something went to wrong")
            }

        })
    }
    fun getOrderSummeryList(userid: Int) {
        val response = repository.getordersummeryList(userid)
        response.enqueue(object : Callback<OrderSummeryResponse> {
            override fun onResponse(call: Call<OrderSummeryResponse>, response: Response<OrderSummeryResponse>) {
                getordersummeryList.postValue(response.body()!!.orderSummeryData)
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
                getaddressbydetailid.postValue(response.body()!!.Data)
            }
            override fun onFailure(call: Call<AddressByCustomerModel>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
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
                errorMessage.postValue(response.body()?.Data)
            }

            override fun onFailure(call: Call<SaveSalesResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
            }
        })
    }
}

