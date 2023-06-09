package com.hc.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hc.hicareservices.data.model.referral.ReferralResponse
import com.hc.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferralViewModel : ViewModel() {
    val repository = MainRepository()

    val referralResponse = MutableLiveData<ReferralResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getReferralCode(mobileNo: String) {
        val response = repository.getReferralCodeResponse(mobileNo)
        response.enqueue(object : Callback<ReferralResponse> {
            override fun onResponse(call: Call<ReferralResponse>, response: Response<ReferralResponse>) {
                referralResponse.postValue(response.body())
                Log.d("TAG", "Response " + response.body()?.data.toString())
            }

            override fun onFailure(call: Call<ReferralResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}