package com.ab.hicareservices.ui.viewmodel

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.CreateEventNotificationResponse
import com.ab.hicareservices.data.model.SaveSalesResponse
import com.ab.hicareservices.data.model.compaintsReason.ComplaintReasons
import com.ab.hicareservices.data.model.complaints.CreateComplaint
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.ui.view.activities.AddComplaintsActivity
import com.ab.hicareservices.ui.view.activities.AddProductComplaintsActivity
import com.ab.hicareservices.ui.view.activities.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CComplaintViewModel : ViewModel() {
    val repository = MainRepository()
    val createComplaintResponse = MutableLiveData<CreateComplaint>()
    val SaveSalesResponse = MutableLiveData<String>()
    val complaintReasons = MutableLiveData<ComplaintReasons>()
    val errorMessage = MutableLiveData<String>()
    val responseMessage = MutableLiveData<String>()
    val CreateEventNotificationResponse = MutableLiveData<CreateEventNotificationResponse>()

    fun getComplaintReasons(
        serviceType: String,
        addProductComplaintsActivity: AddProductComplaintsActivity, ) {
        val response = repository.getComplaintReasonResponse(serviceType)
        response.enqueue(object : Callback<ComplaintReasons> {
            override fun onResponse(call: Call<ComplaintReasons>, response: Response<ComplaintReasons>) {
                if(response.body()!!.isSuccess==true) {
                    if(response.code()==200) {
                        complaintReasons.postValue(response.body())
                    }else if(response.code()==401){

                    SharedPreferenceUtil.setData(addProductComplaintsActivity, "mobileNo", "-1")
                    SharedPreferenceUtil.setData(addProductComplaintsActivity, "bToken", "")
                    SharedPreferenceUtil.setData(addProductComplaintsActivity, "IsLogin", false)
                    SharedPreferenceUtil.setData(addProductComplaintsActivity, "pincode", "")
                    SharedPreferenceUtil.setData(addProductComplaintsActivity, "customerid", "")
                    SharedPreferenceUtil.setData(addProductComplaintsActivity, "FirstName", "")
                    SharedPreferenceUtil.setData(addProductComplaintsActivity, "MobileNo", "")

                    val intent= Intent(addProductComplaintsActivity, LoginActivity::class.java)
                        addProductComplaintsActivity.startActivity(intent)
                        addProductComplaintsActivity.finish()
                    }else if(response.code()==500){
                        Toast.makeText(addProductComplaintsActivity,"Internal server error",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    responseMessage.postValue(response.body()!!.responseMessage!!)
                }

                Log.d("TAG", "Response " + response.body()?.data.toString())
            }

            override fun onFailure(call: Call<ComplaintReasons>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getComplaintReasonses(
        serviceType: String,
        addProductComplaintsActivity: AddComplaintsActivity, ) {
        val response = repository.getComplaintReasonResponse(serviceType)
        response.enqueue(object : Callback<ComplaintReasons> {
            override fun onResponse(call: Call<ComplaintReasons>, response: Response<ComplaintReasons>) {
                if(response.body()!!.isSuccess==true) {
                    if(response.code()==200) {
                        complaintReasons.postValue(response.body())
                    }else if(response.code()==401){

                        SharedPreferenceUtil.setData(addProductComplaintsActivity, "mobileNo", "-1")
                        SharedPreferenceUtil.setData(addProductComplaintsActivity, "bToken", "")
                        SharedPreferenceUtil.setData(addProductComplaintsActivity, "IsLogin", false)
                        SharedPreferenceUtil.setData(addProductComplaintsActivity, "pincode", "")
                        SharedPreferenceUtil.setData(addProductComplaintsActivity, "customerid", "")
                        SharedPreferenceUtil.setData(addProductComplaintsActivity, "FirstName", "")
                        SharedPreferenceUtil.setData(addProductComplaintsActivity, "MobileNo", "")

                        val intent= Intent(addProductComplaintsActivity, LoginActivity::class.java)
                        addProductComplaintsActivity.startActivity(intent)
                        addProductComplaintsActivity.finish()
                    }else if(response.code()==500){
                        Toast.makeText(addProductComplaintsActivity,"Internal server error",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    responseMessage.postValue(response.body()!!.responseMessage!!)
                }

                Log.d("TAG", "Response " + response.body()?.data.toString())
            }

            override fun onFailure(call: Call<ComplaintReasons>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun createComplaint(request: HashMap<String, Any>) {
        val response = repository.createComplaintResponse(request)
        response.enqueue(object : Callback<CreateComplaint> {

            override fun onResponse(call: Call<CreateComplaint>, response: Response<CreateComplaint>) {
                if(response.body()?.isSuccess ==true) {
                    createComplaintResponse.postValue(response.body())
                }else{
                    responseMessage.postValue(response.body()!!.responseMessage!!)
                }
                Log.d("TAG", "Response " + response.body()?.data.toString())
            }

            override fun onFailure(call: Call<CreateComplaint>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }
    fun CreateProductComplaint(request: HashMap<String, Any>) {
        val response = repository.CreateProductComplaint(request)
        response.enqueue(object : Callback<SaveSalesResponse> {

            override fun onResponse(call: Call<SaveSalesResponse>, response: Response<SaveSalesResponse>) {
                if (response.isSuccessful){
                    SaveSalesResponse.postValue(response.body()?.ResponseMessage!!)
                }else{
                    responseMessage.postValue(response.body()!!.ResponseMessage!!)
                }
                Log.d("TAG", "Response " + response.body()?.ResponseMessage.toString())
            }

            override fun onFailure(call: Call<SaveSalesResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }
    fun CreateEventForMobileAppNotification(request: HashMap<String, Any>) {
        val response = repository.CreateProductComplaint(request)
        response.enqueue(object : Callback<SaveSalesResponse> {

            override fun onResponse(call: Call<SaveSalesResponse>, response: Response<SaveSalesResponse>) {
                if (response.isSuccessful){
                    SaveSalesResponse.postValue(response.body()?.ResponseMessage!!)
                }else{
                    responseMessage.postValue(response.body()!!.ResponseMessage!!)
                }
                Log.d("TAG", "Response " + response.body()?.ResponseMessage.toString())
            }

            override fun onFailure(call: Call<SaveSalesResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }
}