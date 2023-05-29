package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.attachment.AttachmentResponse
import com.ab.hicareservices.data.model.orderdetails.Data
import com.ab.hicareservices.data.model.orderdetails.OrderDetails
import com.ab.hicareservices.data.model.payment.SavePaymentResponse
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadAttachmentViewModel : ViewModel() {
    val repository = MainRepository()

    val orderDetailsData = MutableLiveData<List<Data>>()
    val attachmenturl = MutableLiveData<String>()
    fun UploadAttachment(data: HashMap<String, Any>){
        repository.UploadAttachment(data)
            .enqueue(object : Callback<AttachmentResponse>{
                override fun onResponse(call: Call<AttachmentResponse>, response: Response<AttachmentResponse>) {
                    if (response.body()?.IsSuccess == true){
                        val responseBody = response.body()?.Data
                        attachmenturl.postValue(responseBody)
                    }
                }
                override fun onFailure(call: Call<AttachmentResponse>, t: Throwable) {
                }
            })
    }
}