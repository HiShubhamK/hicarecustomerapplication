package com.ab.hicareservices.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.ab.hicareservices.data.model.dashboard.*
import com.ab.hicareservices.data.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {
    val repository = MainRepository()

    val dashboardmain = MutableLiveData<DashboardMainData>()
    val bannerArrayArrayList = MutableLiveData<List<BannerData>>()
    val brandListArrayList= MutableLiveData<ArrayList<BrandData>>()
    val menuDataArrayList = MutableLiveData<ArrayList<MenuData>>()
    val offerDataArrayList = MutableLiveData<ArrayList<OfferData>>()
    val socialMediadataArrayList = MutableLiveData<ArrayList<SocialMediadata>>()
    val todayserviceArrayList = MutableLiveData<ArrayList<Any>>()
    val upcomingservices = MutableLiveData<ArrayList<Any>>()
    val videodataArrayList = MutableLiveData<ArrayList<VideoData>>()
//    val errorMessage = MutableLiveData<String>()
fun GetDashboard(mobileNo: String) {
    repository.GetDashboard(mobileNo)
        .enqueue(object : Callback<DashboardModel> {


            override fun onFailure(call: Call<DashboardModel>, t: Throwable) {
//                errorMessage.postValue(t.message)

            }

            override fun onResponse(
                call: Call<DashboardModel>,
                response: Response<DashboardModel>
            ) {
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
            }catch (e:Exception){
                e.printStackTrace()
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



