package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.LeadResponse
import com.ab.hicareservices.data.model.leadResopnse
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.orders.OrdersResponse
import com.ab.hicareservices.data.model.payment.SavePaymentResponse
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.AppUtils2.leaderlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityViewModel: ViewModel() {
    val repository = MainRepository()
    val spinnerList = MutableLiveData<ArrayList<String>>()
    val errorMessage = MutableLiveData<String>()
    val leadResponse = MutableLiveData<LeadResponse>()

    fun getleaderspinner(servicetype: String) {

        val response = repository.lead(servicetype)
        response.enqueue(object : Callback<leadResopnse> {

            override fun onResponse(call: Call<leadResopnse>, response: Response<leadResopnse>) {
                spinnerList.postValue(response.body()?.Data)
                Log.d("TAG", "Response "+ response.body()?.Data.toString())
            }

            override fun onFailure(call: Call<leadResopnse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun postleaderdata(data: HashMap<String, Any>){
        repository.postLead(data)
            .enqueue(object : Callback<LeadResponse>{
                override fun onResponse(call: Call<LeadResponse>, response: Response<LeadResponse>) {
                    if (response.body()?.IsSuccess == true){
                        val responseBody = response.body()?.Data
                        AppUtils2.paymentsucess= response.body()!!.Data.toString()
                        leadResponse.postValue(response.body())
                    }
                }
                override fun onFailure(call: Call<LeadResponse>, t: Throwable) {
                }
            })
    }



}

