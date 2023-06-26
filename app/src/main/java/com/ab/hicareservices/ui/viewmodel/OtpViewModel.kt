package com.ab.hicareservices.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.NotificationToken
import com.ab.hicareservices.data.model.Whatappresponse
import com.ab.hicareservices.data.model.otp.OtpResponse
import com.ab.hicareservices.data.model.otp.ValidateResponse
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.ui.handler.ValidateAccountListener
import com.ab.hicareservices.utils.AppUtils2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpViewModel : ViewModel(){
    val repository = MainRepository()

    val otpResponse = MutableLiveData<OtpResponse>()
    val validateResponses = MutableLiveData<ValidateResponse>()
    val notificationToken = MutableLiveData<NotificationToken>()
    val errorMessage = MutableLiveData<String>()
    var validateAccountListener: ValidateAccountListener? = null
    val whatsResponse = MutableLiveData<Whatappresponse>()


    fun getOtp(mobileNo: String) {

        val response = repository.getOtpResponse(mobileNo)
        response.enqueue(object : Callback<OtpResponse> {

            override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                otpResponse.postValue(response.body())
                Log.d("TAG", "Response "+ response.body()?.data.toString())
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun validateAccount(mobileNo: String) {
        val response = repository.validateAccount(mobileNo)
        response.enqueue(object : Callback<ValidateResponse> {
            override fun onResponse(call: Call<ValidateResponse?>, response: Response<ValidateResponse>?) {
                if (response != null && response.body()?.IsSuccess == true) {
                    val body = response.body()
                    AppUtils2.TOKEN=response.body()?.Data!!.Token.toString()
                    validateAccountListener?.onSuccess(response.body()?.Data!!.Token.toString())
                }else{
                    validateAccountListener?.onSuccess("")
                }
            }

            override fun onFailure(call: Call<ValidateResponse>, t: Throwable) {
                validateAccountListener?.onError(t.message.toString())
            }
        })
    }

    fun validateAccounts(mobileNo: String,context:Context) {
        val response = repository.validateAccount(mobileNo)
        response.enqueue(object : Callback<ValidateResponse> {
            override fun onResponse(call: Call<ValidateResponse?>, response: Response<ValidateResponse>?) {
                if (response != null && response.body()?.IsSuccess == true) {
                    val body = response.body()
                    AppUtils2.TOKEN=response.body()?.Data!!.Token.toString()
                    SharedPreferenceUtil.setData(context, "bToken",response.body()?.Data!!.Token.toString())
                    SharedPreferenceUtil.setData(context, "pincode",response.body()?.Data!!.PestCustomerData!!.BillingPostalCode.toString())
                    SharedPreferenceUtil.setData(context, "customerid",response.body()?.Data!!.ProductCustomerData!!.Id.toString())
                    SharedPreferenceUtil.setData(context, "FirstName",response.body()?.Data!!.ProductCustomerData!!.FirstName.toString())
                    SharedPreferenceUtil.setData(context, "MobileNo",response.body()?.Data!!.ProductCustomerData!!.MobileNo.toString())


                    validateAccountListener?.onSuccess(response.body()?.Data!!.Token.toString())
                }else{
                    validateAccountListener?.onSuccess("")
                }
            }

            override fun onFailure(call: Call<ValidateResponse>, t: Throwable) {
                validateAccountListener?.onError(t.message.toString())
            }
        })
    }


    fun getNotificationtoken(apptoken: String) {
        val response = repository.notification(apptoken)
        response.enqueue(object : Callback<NotificationToken> {
            override fun onResponse(call: Call<NotificationToken?>, response: Response<NotificationToken>?) {
                if (response != null) {
                   val body = response.body()?.Data.toString()
//                    AppUtils2.TOKEN = response.body()?.Data.toString()
                    validateAccountListener?.onSuccess(body)
                }
            }

            override fun onFailure(call: Call<NotificationToken>, t: Throwable) {
                validateAccountListener?.onError(t.message.toString())
            }
        })
    }


    fun getWhatappToken(watoken: String) {
        val response = repository.getWhatappVerify(watoken)
        response.enqueue(object : Callback<Whatappresponse> {
            override fun onResponse(call: Call<Whatappresponse?>, response: Response<Whatappresponse>?) {
                if (response != null) {
                    whatsResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Whatappresponse>, t: Throwable) {
                validateAccountListener?.onError(t.message.toString())
            }
        })
    }

}


