package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.product.*
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel: ViewModel() {
    val repository = MainRepository()
    val customerlogininfo = MutableLiveData<CustomerLoginInfo>()
    val cutomeraddress= MutableLiveData<CustomerAddress>()
    val productlist= MutableLiveData<List<ProductListResponseData>>()
    val errorMessage = MutableLiveData<String>()
    val producDetailsResponse=MutableLiveData<ProducDetailsResponse>()
    fun getCustomerid(mobileno: String) {
        val response = repository.getcustomerloginid(mobileno)
        response.enqueue(object : Callback<CustomerLoginInfo> {
            override fun onResponse(call: Call<CustomerLoginInfo>, response: Response<CustomerLoginInfo>) {
                customerlogininfo.postValue(response.body())
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
                cutomeraddress.postValue(response.body())
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
                productlist.postValue(response.body()!!.Data)
            }
            override fun onFailure(call: Call<ProductListResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
            }
        })
    }

    fun getProductDetails(productid:String,pincode: String,customerid: Int){
        val response=repository.getProductDetails(productid,pincode,customerid)
        response.enqueue(object  : Callback<ProducDetailsResponse>{
            override fun onResponse(call: Call<ProducDetailsResponse>, response: Response<ProducDetailsResponse>) {
                producDetailsResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<ProducDetailsResponse>, t: Throwable) {
                errorMessage.postValue("Something went to wrong")
            }

        })
    }

}

