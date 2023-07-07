package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.SaveSalesResponse
import com.ab.hicareservices.data.model.compaintsReason.ComplaintReasons
import com.ab.hicareservices.data.model.complaints.CreateComplaint
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CComplaintViewModel : ViewModel() {
    val repository = MainRepository()
    val createComplaintResponse = MutableLiveData<CreateComplaint>()
    val SaveSalesResponse = MutableLiveData<String>()
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


    fun createComplaint(request: HashMap<String, Any>) {
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
    fun CreateProductComplaint(request: HashMap<String, Any>) {
        val response = repository.CreateProductComplaint(request)
        response.enqueue(object : Callback<SaveSalesResponse> {

            override fun onResponse(call: Call<SaveSalesResponse>, response: Response<SaveSalesResponse>) {
                if (response.isSuccessful){
                    SaveSalesResponse.postValue(response.body()?.Data!!)
                }
                Log.d("TAG", "Response " + response.body()?.Data.toString())
            }

            override fun onFailure(call: Call<SaveSalesResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}