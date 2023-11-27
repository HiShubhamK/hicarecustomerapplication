package com.ab.hicareservices.ui.viewmodel

import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.orders.OrdersResponse
import com.ab.hicareservices.data.model.product.SaveAddressResponse
import com.ab.hicareservices.data.model.product.SaveServiceAddressResponse
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.ui.view.activities.LoginActivity
import com.ab.hicareservices.utils.AppUtils2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {
    val repository = MainRepository()
    val ordersList = MutableLiveData<List<OrdersData>>()
    val errorMessage = MutableLiveData<String>()
    val saveAddressResponse = MutableLiveData<SaveServiceAddressResponse>()
    val responseMessage = MutableLiveData<String>()

    fun getCustomerOrdersByMobileNo(mobileNo: String, progressDialog: ProgressDialog, fragmentActivity: FragmentActivity) {

        progressDialog.show()

        val response = repository.getCustomerOrdersByMobileNo(mobileNo)
        response.enqueue(object : Callback<OrdersResponse> {

            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {

                if(response.code()==200){
                    if (response.body()?.isSuccess == true) {
                        responseMessage.postValue(response.body()?.responseMessage!!)
                        ordersList.postValue(response.body()?.data!!)
                        progressDialog.dismiss()
                        Log.d("TAG", "Response " + response.body()?.data.toString())
                    } else {
                        responseMessage.postValue(response.body()?.responseMessage!!)

                    }
                }
                else if(response.code()==401){

                    Toast.makeText(fragmentActivity,"Session Expired",Toast.LENGTH_LONG).show()

                    SharedPreferenceUtil.setData(fragmentActivity, "mobileNo", "-1")
                    SharedPreferenceUtil.setData(fragmentActivity, "bToken", "")
                    SharedPreferenceUtil.setData(fragmentActivity, "IsLogin", false)
                    SharedPreferenceUtil.setData(fragmentActivity, "pincode", "")
                    SharedPreferenceUtil.setData(fragmentActivity, "customerid", "")
                    SharedPreferenceUtil.setData(fragmentActivity, "FirstName", "")
                    SharedPreferenceUtil.setData(fragmentActivity, "MobileNo", "")

                    val intent= Intent(fragmentActivity, LoginActivity::class.java)
                    fragmentActivity.startActivity(intent)
                    fragmentActivity.finish()

                }else if(response.code()==500){
                    Toast.makeText(fragmentActivity,"Internal server error",Toast.LENGTH_LONG).show()
                }else{

                }
            }

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
                progressDialog.dismiss()
            }
        })
    }


    fun getCustomerOrdersByMobileNo(
        mobileNo: String,
        ordertype: String,
        progressBar: ProgressDialog,
        myOrderActivity: FragmentActivity
    ) {
        progressBar.show()

        val response = repository.getCustomerOrdersByMobileNo(mobileNo, ordertype)
        response.enqueue(object : Callback<OrdersResponse> {

            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {
//
//                var checkrequestcode = AppUtils2.requestcode(response.code().toString())

                if(response.code()==200){
                    if (response.body()?.isSuccess == true) {
                        responseMessage.postValue(response.body()?.responseMessage!!)
                        ordersList.postValue(response.body()?.data!!)
                        progressBar.dismiss()
                    } else {
                        responseMessage.postValue(response.body()?.responseMessage!!)
                    }
                }else if(response.code()==401){

                    SharedPreferenceUtil.setData(myOrderActivity, "mobileNo", "-1")
                    SharedPreferenceUtil.setData(myOrderActivity, "bToken", "")
                    SharedPreferenceUtil.setData(myOrderActivity, "IsLogin", false)
                    SharedPreferenceUtil.setData(myOrderActivity, "pincode", "")
                    SharedPreferenceUtil.setData(myOrderActivity, "customerid", "")
                    SharedPreferenceUtil.setData(myOrderActivity, "FirstName", "")
                    SharedPreferenceUtil.setData(myOrderActivity, "MobileNo", "")

                    val intent= Intent(myOrderActivity,LoginActivity::class.java)
                    myOrderActivity.startActivity(intent)
                    myOrderActivity.finish()
                } else if(response.code()==401){
                    Toast.makeText(myOrderActivity,"Internal server error",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
                progressBar.dismiss()
            }
        })
    }


    fun getCustomerOrdersByMobileNos(
        mobileNo: String,
        ordertype: String,
        requireActivity: FragmentActivity,
    ) {

//        progressBar.visibility = View.VISIBLE

        val response = repository.getCustomerOrdersByMobileNo(mobileNo, ordertype)
        response.enqueue(object : Callback<OrdersResponse> {


            override fun onResponse(
                call: Call<OrdersResponse>,
                response: Response<OrdersResponse>
            ) {
//
//
//                var checkrequestcode = AppUtils2.requestcode(response.code().toString())

                if(response.code()==200){
                    if (response.body()?.isSuccess == false) {
                        responseMessage.postValue(response.body()?.responseMessage!!)
                    } else {
                        ordersList.postValue(response.body()?.data!!)
                        Log.d("TAG", "Response " + response.body()?.data.toString())
                    }
                }else if(response.code()==401){


                    SharedPreferenceUtil.setData(requireActivity, "mobileNo", "-1")
                    SharedPreferenceUtil.setData(requireActivity, "bToken", "")
                    SharedPreferenceUtil.setData(requireActivity, "IsLogin", false)
                    SharedPreferenceUtil.setData(requireActivity, "pincode", "")
                    SharedPreferenceUtil.setData(requireActivity, "customerid", "")
                    SharedPreferenceUtil.setData(requireActivity, "FirstName", "")
                    SharedPreferenceUtil.setData(requireActivity, "MobileNo", "")

                    val intent= Intent(requireActivity,LoginActivity::class.java)
                    requireActivity.startActivity(intent)
                    requireActivity.finish()

                }else if(response.code()==500){
                    Toast.makeText(requireActivity,"Internal server error",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<OrdersResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

}