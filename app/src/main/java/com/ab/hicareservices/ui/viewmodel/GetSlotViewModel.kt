package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.getslots.GetSlots
import com.ab.hicareservices.data.model.orderdetails.Data
import com.ab.hicareservices.data.model.orderdetails.OrderDetails
import com.ab.hicareservices.data.model.payment.SavePaymentResponse
import com.ab.hicareservices.data.model.slotcomplaincemodel.GetComplaiceResponce
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.utils.AppUtils2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetSlotViewModel : ViewModel() {
    val repository = MainRepository()

    val getcomplainceresponse = MutableLiveData<List<com.ab.hicareservices.data.model.slotcomplaincemodel.Data>>()
    val getSlotresponse = MutableLiveData<com.ab.hicareservices.data.model.getslots.Data>()
//    val getcomplainceresponse = MutableLiveData<com.ab.hicareservices.data.model.slotcomplaincemodel.Data>()
//    fun getOrderDetailsByOrderNo(orderNo: String, serviceType: String){
//        repository.getOrderDetailsByOrderNo(orderNo, serviceType)
//            .enqueue(object : Callback<OrderDetails>{
//                override fun onResponse(call: Call<OrderDetails>, response: Response<OrderDetails>) {
//                    if (response.body()?.isSuccess == true){
//                        val responseBody = response.body()?.data
//                        orderDetailsData.postValue(responseBody)
//                    }
//                }
//                override fun onFailure(call: Call<OrderDetails>, t: Throwable) {
//                }
//            })
//    }
    fun GetSlots(data: HashMap<String, Any>){
        repository.GetSlots(data)
            .enqueue(object : Callback<GetSlots>{
                override fun onResponse(call: Call<GetSlots>, response: Response<GetSlots>) {
                    if (response.body()?.IsSuccess == true){
                        val responseBody = response.body()?.Data
                        getSlotresponse.postValue(responseBody)
                    }
                }
                override fun onFailure(call: Call<GetSlots>, t: Throwable) {
                }
            })
    }
        fun getComplainceData(data:HashMap<String,Any>){
        repository.getComplainceData(data)
            .enqueue(object : Callback<GetComplaiceResponce>{
                override fun onResponse(call: Call<GetComplaiceResponce>, response: Response<GetComplaiceResponce>) {
                    if (response.body()?.IsSuccess == true){
                        val responseBody = response.body()?.Data
                        getcomplainceresponse.postValue(responseBody)
                    }
                }
                override fun onFailure(call: Call<GetComplaiceResponce>, t: Throwable) {
                }
            })
    }
}