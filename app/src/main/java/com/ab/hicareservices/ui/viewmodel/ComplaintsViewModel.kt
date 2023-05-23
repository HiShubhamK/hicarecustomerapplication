package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.complaints.ComplaintResponse
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplaintsViewModel : ViewModel(){
    val repository = MainRepository()
    val complaintList = MutableLiveData<List<ComplaintsData>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllComplaints( mobileNo: String) {

        val response = repository.getAllComplaints(mobileNo)
        response.enqueue(object : Callback<ComplaintResponse> {

            override fun onResponse(call: Call<ComplaintResponse>, response: Response<ComplaintResponse>) {
                if(response.isSuccessful){
                    complaintList.postValue(response.body()?.data!!)
                }else {
                    Log.d("TAGFail", "Response " + response.body()?.data.toString())
                }
            }

            override fun onFailure(call: Call<ComplaintResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}