package com.ab.hicareservices.ui.viewmodel

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ab.hicareservices.data.SharedPreferenceUtil

import com.ab.hicareservices.data.model.dashboard.*
import com.ab.hicareservices.data.repository.MainRepository
import com.ab.hicareservices.ui.view.activities.LoginActivity
import com.ab.hicareservices.utils.AppUtils2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    val repository = MainRepository()

    val dashboardmain = MutableLiveData<DashboardMainData?>()
    val bannerArrayArrayList = MutableLiveData<List<BannerData>>()
    val brandListArrayList= MutableLiveData<ArrayList<BrandData>>()
    val menuDataArrayList = MutableLiveData<ArrayList<MenuData>>()
    val offerDataArrayList = MutableLiveData<ArrayList<OfferData>>()
    val socialMediadataArrayList = MutableLiveData<ArrayList<SocialMediadata>>()
    val todayserviceArrayList = MutableLiveData<ArrayList<String>>()
    val upcomingservices = MutableLiveData<ArrayList<UpcomingService>>()
    val videodataArrayList = MutableLiveData<ArrayList<VideoData>>()
    val errorMessage = MutableLiveData<String>()
    val requestcodes = MutableLiveData<String>()

fun GetDashboard(mobileNo: String, requireActivity: FragmentActivity) {
    repository.GetDashboard(mobileNo)
        .enqueue(object : Callback<DashboardModel> {

            override fun onFailure(call: Call<DashboardModel>, t: Throwable) {
//                errorMessage.postValue(t.message)

            }

            override fun onResponse(
                call: Call<DashboardModel>,
                response: Response<DashboardModel>
            ) {
                var checkrequestcode = AppUtils2.requestcode(response.code().toString())

                if (response.code()==200) {
                    try {
                        if (response.isSuccessful) {
                            dashboardmain.postValue(response.body()!!.Data)
//                    bannerArrayArrayList.postValue(response.body()!!.DashboardMainData.BannerData)
//                    brandListArrayList.postValue(response.body()!!.DashboardMainData.BrandData)
//                    videodataArrayList.postValue(response.body()!!.DashboardMainData.VideoData)
//                    menuDataArrayList.postValue(response.body()!!.DashboardMainData.MenuData)
//                    offerDataArrayList.postValue(response.body()!!.DashboardMainData.OfferData)
//                    socialMediadataArrayList.postValue(response.body()!!.DashboardMainData.SocialMediadata)
//                    todayserviceArrayList.postValue(response.body()!!.DashboardMainData.TodaysService)
//                    upcomingservices.postValue(response.body()!!.DashboardMainData.UpcomingService)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }else if(response.code()==401) {
                    requestcodes.postValue("401")

//
//                    SharedPreferenceUtil.setData(requireActivity, "mobileNo", "-1")
//                    SharedPreferenceUtil.setData(requireActivity, "bToken", "")
//                    SharedPreferenceUtil.setData(requireActivity, "IsLogin", false)
//                    SharedPreferenceUtil.setData(requireActivity, "pincode", "")
//                    SharedPreferenceUtil.setData(requireActivity, "customerid", "")
//                    SharedPreferenceUtil.setData(requireActivity, "FirstName", "")
//                    SharedPreferenceUtil.setData(requireActivity, "MobileNo", "")
//
//
//                    Toast.makeText(requireActivity,response.code().toString(),Toast.LENGTH_LONG).show()
//                    val intent= Intent(requireActivity,LoginActivity::class.java)
//                    requireActivity.startActivity(intent)
//                    requireActivity.finish()

               } else {
                    Toast.makeText(requireActivity,response.code().toString(),Toast.LENGTH_LONG).show()
//                    val intent= Intent(requireActivity,LoginActivity::class.java)
//                    requireActivity.startActivity(intent)
//                    requireActivity.finish()
                }
            }
        })
}

//    fun GetDashboard(data: HashMap<String, Any>) {
//
//        val response = repository.GetDashboard(data)
//        response.enqueue(object : Callback<DashboardModel> {
//
//            override fun onResponse(
//                call: Call<DashboardModel>,
//                response: Response<DashboardModel>
//            ) {
//                if (response.isSuccessful) {
//                    dashboardmain.postValue(response.body()?.DashboardMainData)
//                    bannerArrayArrayList.postValue(response.body()?.DashboardMainData!!.BannerData)
//                    brandListArrayList.postValue(response.body()?.DashboardMainData!!.BrandData)
//                    menuDataArrayList.postValue(response.body()?.DashboardMainData!!.MenuData)
//                    offerDataArrayList.postValue(response.body()?.DashboardMainData!!.OfferData)
//                    socialMediadataArrayList.postValue(response.body()?.DashboardMainData!!.SocialMediadata)
//                    todayserviceArrayList.postValue(response.body()?.DashboardMainData!!.TodaysService)
//                    upcomingservices.postValue(response.body()?.DashboardMainData!!.UpcomingService)
//                    videodataArrayList.postValue(response.body()?.DashboardMainData!!.VideoData)
//
//                } else {
//                    Log.d("TAGFail", "Response " + response.body()!!.ResponseMessage)
//                }
//            }
//
//            override fun onFailure(call: Call<DashboardModel>, t: Throwable) {
////                errorMessage.postValue(t.message)
//            }
//        })
//    }
}



