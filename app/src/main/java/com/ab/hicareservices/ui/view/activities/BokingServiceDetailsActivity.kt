package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.ActivityBokingServiceDetailsBinding
import com.ab.hicareservices.ui.adapter.BookingServiceListDetailsAdapter
import com.ab.hicareservices.ui.adapter.BookingServicePlanListAdapter
import com.ab.hicareservices.ui.handler.OnBookingViewDetials
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class BokingServiceDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBokingServiceDetailsBinding
    var serviceName: String? = null
    var serviceCode: String? = null
    var serviceThumbnail: String? = null
    var shortDescription: String? = null
    var stailDescription: String? = null
    lateinit var spinnerlist: ArrayList<String>
    var selectedLocation:String?=null
    private lateinit var mAdapter: BookingServicePlanListAdapter
    private val viewProductModel: ServiceBooking by viewModels()
    private lateinit var nAdapter: BookingServiceListDetailsAdapter
    lateinit var bhklistResponseData: List<BhklistResponseData>
    var descrition=""
    var shortdescrition=""
    var id=0
    var thumbnail=""
    var servicecode=""
    var servicename=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_boking_service_details)

        binding=ActivityBokingServiceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        serviceName = intent.getStringExtra("ServiceName").toString()
        serviceCode = intent.getStringExtra("ServiceCode").toString()
        serviceThumbnail = intent.getStringExtra("ServiceThumbnail").toString()
        shortDescription = intent.getStringExtra("ShortDescription").toString()
        stailDescription = intent.getStringExtra("DetailDescription").toString()

        bhklistResponseData = ArrayList<BhklistResponseData>()

        Picasso.get().load(serviceThumbnail).into(binding.imgbanner)

        binding.recycleviewplans.layoutManager = LinearLayoutManager(this@BokingServiceDetailsActivity, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServicePlanListAdapter()
        binding.recycleviewplans.adapter = mAdapter


        viewProductModel.servicePlanResponseData.observe(this@BokingServiceDetailsActivity, Observer {
            if (it.isNotEmpty()) {
                mAdapter.setServiceList(it,this)
            } else {

            }
        })

        viewProductModel.getPlanAndPriceByPincodeAndServiceCode("400601",AppUtils2.servicecode)

//        mAdapter.setonViewdatail(object : )

        mAdapter.setonViewdatail(object : OnBookingViewDetials {
            override fun onViewDetails(position: Int, serviceid: Int) {
//                val bottomSheetFragment = CustomBottomSheetFragment.newInstance(1,"cms","cms","cms","cms","cms")
//                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

                viewProductModel.bookingServiceDetailResponseData.observe(this@BokingServiceDetailsActivity, Observer {
                    if (it.Id!=0) {
                         descrition= it.DetailDescription.toString()
                         shortdescrition= it.ShortDescription.toString()
                         id= it.Id!!
                         thumbnail= it.ServiceThumbnail.toString()
                         servicecode= it.ServiceCode.toString()
                         servicename= it.ServiceName.toString()

                    } else {

                    }
                })
                viewProductModel.getActiveServiceDetailById(1)

                Handler(Looper.getMainLooper()).postDelayed({
                    val bottomSheetFragment = CustomBottomSheetFragment.newInstance(id,servicename,servicecode,thumbnail,shortdescrition,descrition)
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }, 200)
            }

            override fun onClickAddButton(
                position: Int,
                planid: Int?,
                pincodeid: String,
                noOfBHK: String?,
                getServicePlanResponseData: ArrayList<GetServicePlanResponseData>
            ) {

                AppUtils2.getServicePlanResponseData= ArrayList()

                AppUtils2.getServicePlanResponseData.clear()

                AppUtils2.getServicePlanResponseData.addAll(getServicePlanResponseData)

                viewProductModel.activebhklist.observe(this@BokingServiceDetailsActivity, Observer {
                    if (it.isNotEmpty()) {

                        bhklistResponseData=it

                    } else {

                    }
                })
                viewProductModel.getActiveBHKList()

                Handler(Looper.getMainLooper()).postDelayed({
                    val bottomSheetFragments = CustomBottomSheetAddBhkFragment.newInstance(
                        bhklistResponseData as ArrayList<BhklistResponseData>,
                        planid,
                        pincodeid,
                        getServicePlanResponseData as ArrayList<GetServicePlanResponseData>
                    )
                    bottomSheetFragments.show(supportFragmentManager, bottomSheetFragments.tag)
                }, 200)
            }
        })

        binding.recMenu.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        nAdapter = BookingServiceListDetailsAdapter()
        binding.recMenu.adapter = nAdapter

        viewProductModel.serviceresponssedata.observe(this, Observer {
            if (it.isNotEmpty()) {
                nAdapter.setServiceLists(it,this)
            } else {

            }
        })
        viewProductModel.getActiveServiceList()

        binding.txtshortdes.text=shortDescription.toString()
        binding.txtlongdes.text=stailDescription.toString()

        spinnerlist= ArrayList()
        spinnerlist.add("Select flat area")

        binding.imgLogo.setOnClickListener{
            onBackPressed()
            finish()
        }

    }
}
