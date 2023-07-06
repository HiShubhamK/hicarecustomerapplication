package com.ab.hicareservices.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.attachment.GetAttachmentResponse
import com.ab.hicareservices.data.model.complaints.ComplaintResponse
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.data.model.productcomplaint.ProductComplaintData
import com.ab.hicareservices.data.model.productcomplaint.ProductComplaintListResponse
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplaintsViewModel : ViewModel() {
    val repository = MainRepository()

    val complaintList = MutableLiveData<List<ComplaintsData>>()
    val procuctcomplaintList = MutableLiveData<List<ProductComplaintData>>()
    val attachments = MutableLiveData<List<String>>()
    val errorMessage = MutableLiveData<String>()
    val responseMessage = MutableLiveData<String>()


    fun ProductComplaintListByUserId(userid: Int) {

        val response = repository.ProductComplaintListByUserId(userid)
        response.enqueue(object : Callback<ProductComplaintListResponse> {

            override fun onResponse(
                call: Call<ProductComplaintListResponse>,
                response: Response<ProductComplaintListResponse>
            ) {
                if (response.body()?.IsSuccess==true) {
                    procuctcomplaintList.postValue(response.body()?.Data)
//                    attachments.postValue(response.body()?.Attachments)
                } else {
                    Log.d("TAGFail", "Response " + response.body()!!.ResponseMessage)
                    responseMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<ProductComplaintListResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun getAllComplaints(mobileNo: String) {

        val response = repository.getAllComplaints(mobileNo)
        response.enqueue(object : Callback<ComplaintResponse> {

            override fun onResponse(
                call: Call<ComplaintResponse>,
                response: Response<ComplaintResponse>
            ) {
                if (response.body()?.IsSuccess==true) {
                    complaintList.postValue(response.body()?.data)
//                    attachments.postValue(response.body()?.Attachments)
                } else {
                    Log.d("TAGFail", "Response " + response.body()!!.ResponseMessage)
                    responseMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<ComplaintResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
    fun getComlaintAttachment(complaintid: String) {

        val response = repository.GetComplaintAttachments(complaintid)
        response.enqueue(object : Callback<GetAttachmentResponse> {

            override fun onResponse(
                call: Call<GetAttachmentResponse>,
                response: Response<GetAttachmentResponse>
            ) {
                if (response.isSuccessful) {
                    attachments.postValue(response.body()?.Data)

                } else {
                    responseMessage.postValue(response.body()!!.ResponseMessage!!)
                    Log.d("TAGFail", "Response " + response.body()!!.ResponseMessage)
                }
            }

            override fun onFailure(call: Call<GetAttachmentResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}