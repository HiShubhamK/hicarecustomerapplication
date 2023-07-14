package com.ab.hicareservices.ui.viewmodel

import android.app.ProgressDialog
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.orders.OrdersResponse
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {
    val repository = MainRepository()
    val ordersList = MutableLiveData<List<OrdersData>>()
    val errorMessage = MutableLiveData<String>()
    val responseMessage = MutableLiveData<String>()

    fun getCustomerOrdersByMobileNo(mobileNo: String, progressDialog: ProgressDialog) {

        progressDialog.show()

        val response = repository.getCustomerOrdersByMobileNo(mobileNo)
        response.enqueue(object : Callback<OrdersResponse> {

            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {

                if (response.body()?.isSuccess == false) {
                    responseMessage.postValue(response.body()?.responseMessage!!)
                } else {
                    ordersList.postValue(response.body()?.data!!)
                    progressDialog.dismiss()
                    Log.d("TAG", "Response " + response.body()?.data.toString())
                }
            }

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
                progressDialog.dismiss()
            }
        })
    }


    fun getCustomerOrdersByMobileNo(
        mobileNo: String,
        ordertype: String,
        progressBar: ProgressDialog
    ) {

//        progressBar.visibility = View.VISIBLE

        progressBar.show()

        val response = repository.getCustomerOrdersByMobileNo(mobileNo, ordertype)
        response.enqueue(object : Callback<OrdersResponse> {


            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {

                if (response.body()?.isSuccess == false) {
                    responseMessage.postValue(response.body()?.responseMessage)
                } else {
                    ordersList.postValue(response.body()?.data)
                    Log.d("TAG", "Response " + response.body()?.data.toString())
                    progressBar.dismiss()
                }
            }

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
                progressBar.dismiss()
            }
        })
    }


}