package com.hc.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hc.hicareservices.data.model.orderdetails.Data
import com.hc.hicareservices.data.model.orderdetails.OrderDetails
import com.hc.hicareservices.data.model.payment.SavePaymentResponse
import com.hc.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsViewModel : ViewModel() {
    val repository = MainRepository()

    val orderDetailsData = MutableLiveData<List<Data>>()
    val savePaymentResponse = MutableLiveData<String>()
    fun getOrderDetailsByOrderNo(orderNo: String, serviceType: String){
        repository.getOrderDetailsByOrderNo(orderNo, serviceType)
            .enqueue(object : Callback<OrderDetails>{
                override fun onResponse(call: Call<OrderDetails>, response: Response<OrderDetails>) {
                    if (response.body()?.isSuccess == true){
                        val responseBody = response.body()?.data
                        orderDetailsData.postValue(responseBody)
                    }
                }
                override fun onFailure(call: Call<OrderDetails>, t: Throwable) {
                }
            })
    }
    fun saveAppPaymentDetails(data: HashMap<String, Any>){
        repository.saveAppPaymentDetails(data)
            .enqueue(object : Callback<SavePaymentResponse>{
                override fun onResponse(call: Call<SavePaymentResponse>, response: Response<SavePaymentResponse>) {
                    if (response.body()?.isSuccess == true){
                        val responseBody = response.body()?.data
                        savePaymentResponse.postValue("Success")
                    }
                }
                override fun onFailure(call: Call<SavePaymentResponse>, t: Throwable) {
                }
            })
    }
}