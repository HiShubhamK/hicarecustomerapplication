package com.hc.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hc.hicareservices.data.model.complaints.ComplaintResponse
import com.hc.hicareservices.data.model.complaints.ComplaintsData
import com.hc.hicareservices.data.model.service.ServiceData
import com.hc.hicareservices.data.model.service.ServiceResponse
import com.hc.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceViewModel : ViewModel(){
    val repository = MainRepository()

    val serviceList = MutableLiveData<List<ServiceData>>()
    val errorMessage = MutableLiveData<String>()

    fun getServiceRequest( orderNo: String, type: String) {
        val response = repository.getServiceRequest(orderNo, type)
        response.enqueue(object : Callback<ServiceResponse> {
            override fun onResponse(call: Call<ServiceResponse>, response: Response<ServiceResponse>) {
                serviceList.postValue(response.body()?.data)
                Log.d("TAG", "Response "+ response.body()?.data.toString())
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}