package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.service.ServiceResponse
import com.ab.hicareservices.data.model.servicesmodule.BHKandPincode
import com.ab.hicareservices.data.model.servicesmodule.BHKandPincodeData
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponse
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.data.model.servicesmodule.BookingServiceDetailResponse
import com.ab.hicareservices.data.model.servicesmodule.ServiceListResponse
import com.ab.hicareservices.data.model.servicesmodule.ServiceListResponseData
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceBooking:ViewModel() {
    val repository = MainRepository()

    val serviceresponssedata = MutableLiveData<List<ServiceListResponseData>>()
    val activebhklist = MutableLiveData<List<BhklistResponseData>>()
    val activebhkpincode = MutableLiveData<List<BHKandPincodeData>>()

    val errorMessage = MutableLiveData<String>()

    fun getActiveServiceList() {
        val response = repository.GetActiveServiceList()
        response.enqueue(object : Callback<ServiceListResponse> {
            override fun onResponse(call: Call<ServiceListResponse>, response: Response<ServiceListResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    serviceresponssedata.postValue(response.body()!!.Data)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<ServiceListResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getActiveBHKList() {
        val response = repository.GetActiveBHKList()
        response.enqueue(object : Callback<BhklistResponse> {
            override fun onResponse(call: Call<BhklistResponse>, response: Response<BhklistResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    activebhklist.postValue(response.body()!!.Data)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<BhklistResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getPlanAndPriceByBHKandPincode(pincode:String,noofbhk:String,services:String) {
        val response = repository.getPlanAndPriceByBHKandPincode(pincode,noofbhk,services)
        response.enqueue(object : Callback<BHKandPincode> {
            override fun onResponse(call: Call<BHKandPincode>, response: Response<BHKandPincode>) {
                if(response.body()!!.IsSuccess==true) {
                    activebhkpincode.postValue(response.body()!!.Data)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<BHKandPincode>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getActiveServiceDetailById(ServiceId:Int) {
        val response = repository.getActiveServiceDetailById(ServiceId)
        response.enqueue(object : Callback<BookingServiceDetailResponse> {
            override fun onResponse(call: Call<BookingServiceDetailResponse>, response: Response<BookingServiceDetailResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    activebhkpincode.postValue(response.body()!!.Data)
                }else{
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }
            override fun onFailure(call: Call<BookingServiceDetailResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }


}

