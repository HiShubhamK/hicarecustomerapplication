package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.dashboard.ScheduledService
import com.ab.hicareservices.data.model.dashboard.UpcomingService
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceScheduleResponse: ViewModel(){
    val repository = MainRepository()
    val ScheduledService = MutableLiveData<List<UpcomingService>>()
    val errorMessage = MutableLiveData<String>()
//
//    fun getUpcomingScheduledService( mobileno: String) {
//        val response = repository.getUpcomingScheduledService(mobileno)
//        response.enqueue(object : Callback<ScheduledService> {
//            override fun onResponse(call: Call<ScheduledService>, response: Response<ScheduledService>) {
//                ScheduledService.postValue(response.body()?.Data)
//                Log.d("TAG", "Response "+ response.body()?.Data.toString())
//            }
//
//            override fun onFailure(call: Call<ScheduledService>, t: Throwable) {
//                errorMessage.postValue(t.message)
//            }
//        })
//    }
//
//    fun getTodayScheduledService(mobileno: String) {
//        val response = repository.getTodayScheduledService(mobileno)
//        response.enqueue(object : Callback<ScheduledService> {
//            override fun onResponse(call: Call<ScheduledService>, response: Response<ScheduledService>) {
//                ScheduledService.postValue(response.body()?.Data)
//                Log.d("TAG", "Response "+ response.body()?.Data.toString())
//            }
//
//            override fun onFailure(call: Call<ScheduledService>, t: Throwable) {
//                errorMessage.postValue(t.message)
//            }
//        })
//    }

}
