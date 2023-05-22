package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.orders.OrdersResponse
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel(){
    val repository = MainRepository()
    val ordersList = MutableLiveData<List<OrdersData>>()
    val errorMessage = MutableLiveData<String>()

    fun getCustomerOrdersByMobileNo(mobileNo: String) {

        val response = repository.getCustomerOrdersByMobileNo(mobileNo)
        response.enqueue(object : Callback<OrdersResponse> {

            override fun onResponse(call: Call<OrdersResponse>, response: Response<OrdersResponse>) {
                ordersList.postValue(response.body()?.data)
                Log.d("TAG", "Response "+ response.body()?.data.toString())
            }

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}