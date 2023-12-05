package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.model.SaveSalesResponse
import com.ab.hicareservices.data.model.dashboard.DashboardMainData
import com.ab.hicareservices.data.model.product.SaveAddressResponse
import com.ab.hicareservices.data.model.servicesmodule.BHKandPincode
import com.ab.hicareservices.data.model.servicesmodule.BHKandPincodeData
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponse
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.data.model.servicesmodule.BookingServiceDetailResponse
import com.ab.hicareservices.data.model.servicesmodule.BookingServiceDetailResponseData
import com.ab.hicareservices.data.model.servicesmodule.ExistingAddressListModel
import com.ab.hicareservices.data.model.servicesmodule.ExistingCustomerAddressData
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponse
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.data.model.servicesmodule.ServiceListResponse
import com.ab.hicareservices.data.model.servicesmodule.ServiceListResponseData
import com.ab.hicareservices.data.model.servicesmodule.ValidateServiceVoucherResponse
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.utils.AppUtils2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceBooking : ViewModel() {
    val repository = MainRepository()
    val saveServiceAddressResponse = MutableLiveData<SaveAddressResponse>()
    val serviceresponssedata = MutableLiveData<List<ServiceListResponseData>>()
    val existingAddressListModel = MutableLiveData<List<ExistingCustomerAddressData>>()
    val activebhklist = MutableLiveData<List<BhklistResponseData>>()
    val activebhkpincode = MutableLiveData<List<BHKandPincodeData>>()
    val bookingServiceDetailResponseData = MutableLiveData<BookingServiceDetailResponseData>()
    val servicePlanResponseData = MutableLiveData<List<GetServicePlanResponseData>>()
    val paymentsuceess = MutableLiveData<SaveSalesResponse>()
    val validatevoucher = MutableLiveData<ValidateServiceVoucherResponse>()

    val errorMessage = MutableLiveData<String>()

    fun getActiveServiceList() {
        val response = repository.GetActiveServiceList()
        response.enqueue(object : Callback<ServiceListResponse> {
            override fun onResponse(
                call: Call<ServiceListResponse>,
                response: Response<ServiceListResponse>
            ) {
                if (response.body()!!.IsSuccess == true) {
                    serviceresponssedata.postValue(response.body()!!.Data)
                } else {
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<ServiceListResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }
    fun getexistingcustomeraddress(userId: Int) {
        val response = repository.getexistingserviceAddress(userId)
        response.enqueue(object : Callback<ExistingAddressListModel> {
            override fun onResponse(
                call: Call<ExistingAddressListModel>,
                response: Response<ExistingAddressListModel>
            ) {
                if (response.body()!!.IsSuccess == true) {
                    existingAddressListModel.postValue(response.body()!!.Data)
                } else {
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<ExistingAddressListModel>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getActiveBHKList() {
        val response = repository.GetActiveBHKList()
        response.enqueue(object : Callback<BhklistResponse> {
            override fun onResponse(
                call: Call<BhklistResponse>,
                response: Response<BhklistResponse>
            ) {
                if (response.body()!!.IsSuccess == true) {
                    activebhklist.postValue(response.body()!!.Data)
                } else {
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<BhklistResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getPlanAndPriceByBHKandPincode(pincode: String, noofbhk: String, services: String,planid:Int) {
        val response = repository.getPlanAndPriceByBHKandPincode(pincode, noofbhk, services,planid)
        response.enqueue(object : Callback<BHKandPincode> {
            override fun onResponse(call: Call<BHKandPincode>, response: Response<BHKandPincode>) {
                if (response.body()!!.IsSuccess == true) {
                    activebhkpincode.postValue(response.body()!!.Data)
                } else {
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<BHKandPincode>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun getActiveServiceDetailById(ServiceId: Int) {
        val response = repository.getActiveServiceDetailById(ServiceId)
        response.enqueue(object : Callback<BookingServiceDetailResponse> {
            override fun onResponse(
                call: Call<BookingServiceDetailResponse>,
                response: Response<BookingServiceDetailResponse>
            ) {
                if (response.body()!!.IsSuccess == true) {
                    bookingServiceDetailResponseData.postValue(response.body()!!.Data!!)
                } else {
                    errorMessage.postValue(response.body()?.ResponseMessage!!)
                }
            }

            override fun onFailure(call: Call<BookingServiceDetailResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }


    fun getPlanAndPriceByPincodeAndServiceCode(pincode: String, services: String) {
        val response = repository.getPlanAndPriceByPincodeAndServiceCode(pincode, services)
        response.enqueue(object : Callback<GetServicePlanResponse> {


            override fun onResponse(
                call: Call<GetServicePlanResponse>,
                response: Response<GetServicePlanResponse>
            ) {
                if (response.body()?.IsSuccess == true) {
                    servicePlanResponseData.postValue(response.body()!!.Data)
                } else {
                    errorMessage.postValue("Please Check Internet Connection.")
                }
            }

            override fun onFailure(call: Call<GetServicePlanResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }

        })
    }


    fun SaveServiceAddressnew(data: HashMap<String, Any>) {
        val response = repository.SaveServiceAddress(data)
        response.enqueue(object : Callback<SaveAddressResponse> {
            override fun onResponse(
                call: Call<SaveAddressResponse>,
                response: Response<SaveAddressResponse>
            ) {
                saveServiceAddressResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<SaveAddressResponse>, t: Throwable) {
                errorMessage.postValue(call.toString())
            }

        })
    }



    fun AddOrderAsync(data: HashMap<String, Any>){
        val response=repository.AddOrderAsync(data)
        response.enqueue(object :Callback<SaveSalesResponse>{
            override fun onResponse(
                call: Call<SaveSalesResponse>,
                response: Response<SaveSalesResponse>
            ) {
                paymentsuceess.postValue(response.body())
            }

            override fun onFailure(call: Call<SaveSalesResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }

    fun PostVoucherValidationcode(vouchercode:String,planId:Int,serviceprice:Float){
        val response=repository.postvalidateServiceVoucher(vouchercode,planId,serviceprice)
        response.enqueue(object :Callback<ValidateServiceVoucherResponse>{
            override fun onResponse(
                call: Call<ValidateServiceVoucherResponse>,
                response: Response<ValidateServiceVoucherResponse>
            ) {
                validatevoucher.postValue(response.body())
            }

            override fun onFailure(call: Call<ValidateServiceVoucherResponse>, t: Throwable) {
                errorMessage.postValue("Please Check Internet Connection.")
            }
        })
    }


}

