package com.ab.hicareservices.ui.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.dashboard.ScheduledService
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.data.model.service.ServiceResponse
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.ui.view.activities.LoginActivity
import com.ab.hicareservices.ui.view.activities.OrderDetailActivity
import com.ab.hicareservices.utils.AppUtils2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceViewModel : ViewModel() {
    val repository = MainRepository()

    val serviceList = MutableLiveData<List<ServiceData>>()
    val ScheduledService = MutableLiveData<ScheduledService>()
    val errorMessage = MutableLiveData<String>()
    val responseMessage = MutableLiveData<String>()

    fun getServiceRequest(orderNo: String, type: String, orderDetailsFragment: OrderDetailActivity) {
        val response = repository.getServiceRequest(orderNo, type)
        response.enqueue(object : Callback<ServiceResponse> {
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {

                if (response.code() == 200) {

                    serviceList.postValue(response.body()?.Data)
                    Log.d("TAG", "Response " + response.body()?.Data.toString())

                } else if(response.code()==401){

                    SharedPreferenceUtil.setData(orderDetailsFragment, "mobileNo", "-1")
                    SharedPreferenceUtil.setData(orderDetailsFragment, "bToken", "")
                    SharedPreferenceUtil.setData(orderDetailsFragment, "IsLogin", false)
                    SharedPreferenceUtil.setData(orderDetailsFragment, "pincode", "")
                    SharedPreferenceUtil.setData(orderDetailsFragment, "customerid", "")
                    SharedPreferenceUtil.setData(orderDetailsFragment, "FirstName", "")
                    SharedPreferenceUtil.setData(orderDetailsFragment, "MobileNo", "")

                    val intent= Intent(orderDetailsFragment, LoginActivity::class.java)
                    orderDetailsFragment.startActivity(intent)
                    orderDetailsFragment.finish()


                }else if(response.code()==500){

                }


            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getUpcomingServices(mobilenoo: String) {
        val response = repository.getUpcomingScheduledService(AppUtils2.mobileno)
        response.enqueue(object : Callback<ScheduledService> {


            override fun onFailure(call: Call<ScheduledService>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

            override fun onResponse(
                call: Call<ScheduledService>,
                response: Response<ScheduledService>
            ) {
                if (response.body()!!.IsSuccess == true) {
                    ScheduledService.postValue(response.body())
                } else {
                    responseMessage.postValue(response.body()!!.ResponseMessage!!)
                }
            }
        })
    }

}

