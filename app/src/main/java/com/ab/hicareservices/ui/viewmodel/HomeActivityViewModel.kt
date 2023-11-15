package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.AppversionData
import com.ab.hicareservices.data.model.CurrentApiVersion
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
    val responseMessgae = MutableLiveData<String>()
    val currentapp = MutableLiveData<AppversionData>()
    val requestcode = MutableLiveData<String>()

    fun getleaderspinner(servicetype: String) {

        val response = repository.lead(servicetype)
        response.enqueue(object : Callback<leadResopnse> {

            override fun onResponse(call: Call<leadResopnse>, response: Response<leadResopnse>) {
                if(response.code()==200) {
                    spinnerList.postValue(response.body()?.Data)
                    Log.d("TAG", "Response " + response.body()?.Data.toString())
                }else if(response.code()==401){
                    requestcode.postValue("401")
                }else if(response.code()==500){

                }
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
                    }else{
                        responseMessgae.postValue(response.body()?.Data!!.ResponseMessage!!)
                    }
                }
                override fun onFailure(call: Call<LeadResponse>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
    }

    fun getcurretnapversioncode(mobile:String){
        repository.getcurrentAppversion(mobile)
            .enqueue(object : Callback<CurrentApiVersion>{
                override fun onResponse(call: Call<CurrentApiVersion>, response: Response<CurrentApiVersion>) {
                    if (response.body()?.IsSuccess == true){
                        currentapp.postValue(response.body()!!.data!!)
                    }else{
                        responseMessgae.postValue(response.body()?.ResponseMessage!!)
                    }
                }
                override fun onFailure(call: Call<CurrentApiVersion>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })
    }

}

