package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.referral.ReferralResponse
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferralViewModel : ViewModel() {
    val repository = MainRepository()

    val referralResponse = MutableLiveData<ReferralResponse>()
    val errorMessage = MutableLiveData<String>()
    val responseMessage = MutableLiveData<String>()

    fun getReferralCode(mobileNo: String) {
        val response = repository.getReferralCodeResponse(mobileNo)
        response.enqueue(object : Callback<ReferralResponse> {
            override fun onResponse(call: Call<ReferralResponse>, response: Response<ReferralResponse>) {
                if(response.body()!!.IsSuccess==true) {
                    referralResponse.postValue(response.body())
                }else{
                    responseMessage.postValue(response.body()!!.ResponseMessage)
                }
                Log.d("TAG", "Response " + response.body()?.Data.toString())
            }

            override fun onFailure(call: Call<ReferralResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}