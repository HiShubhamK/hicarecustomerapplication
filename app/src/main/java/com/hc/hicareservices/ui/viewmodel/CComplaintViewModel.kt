package com.hc.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hc.hicareservices.data.model.compaintsReason.ComplaintReasons
import com.hc.hicareservices.data.model.complaints.CreateComplaint
import com.hc.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CComplaintViewModel : ViewModel() {
    val repository = MainRepository()
    val createComplaintResponse = MutableLiveData<CreateComplaint>()
    val complaintReasons = MutableLiveData<ComplaintReasons>()
    val errorMessage = MutableLiveData<String>()

    fun getComplaintReasons(serviceType: String) {
        val response = repository.getComplaintReasonResponse(serviceType)
        response.enqueue(object : Callback<ComplaintReasons> {
            override fun onResponse(call: Call<ComplaintReasons>, response: Response<ComplaintReasons>) {
                complaintReasons.postValue(response.body())
                Log.d("TAG", "Response " + response.body()?.data.toString())
            }

            override fun onFailure(call: Call<ComplaintReasons>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    fun createComplaint(request: HashMap<String, String>) {
        val response = repository.createComplaintResponse(request)
        response.enqueue(object : Callback<CreateComplaint> {

            override fun onResponse(call: Call<CreateComplaint>, response: Response<CreateComplaint>) {
                createComplaintResponse.postValue(response.body())
                Log.d("TAG", "Response " + response.body()?.data.toString())
            }

            override fun onFailure(call: Call<CreateComplaint>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}