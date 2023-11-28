package com.ab.hicareservices.ui.view.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.servicesmodule.BHKandPincodeData
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.ui.adapter.BookingServiceBhklistAdapter
import com.ab.hicareservices.ui.handler.OnBookingFlatPerPrice
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.collections.ArrayList

class CustomBottomSheetAddBhkFragment() : BottomSheetDialogFragment() {

    private lateinit var mAdapter: BookingServiceBhklistAdapter
    private val viewProductModel: ServiceBooking by viewModels()
    lateinit var bhkandpincodedata: List<BHKandPincodeData>
    var price = ""
    private var ServiceCenter_Id = ""
    private var SkillId = ""
    private var SlotDate = ""
    private var TaskId = ""
    private var Latt = ""
    private var Longg = ""
    private var ServiceType = ""
    private var Pincode = ""
    private var Service_Code = ""
    private var Unit = ""
    private var spcode = ""
    var prices=""
    var discountedPrice=""

    companion object {
        private const val ARG_DATA = "list_key"
        private const val PLAN_ID = "PLAN_ID"
        private const val PINCODE_ID = "PINCODE_ID"
        private const val PLANLIST = "PLAN_LIST"
        private const val ARG_PRICE ="data_PRICE"
        private const val ARG_DISCOUNTEDPRICE ="data_DISCOUNTEDPRICE"


        fun newInstance(
            bhklistResponseData: List<BhklistResponseData>,
            planid: Int?,
            pincodeid: String,
            getServicePlanResponseData: ArrayList<GetServicePlanResponseData>,
            price: Int?,
            discountedPrice: Int?
        ): CustomBottomSheetAddBhkFragment {
            val fragment = CustomBottomSheetAddBhkFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_DATA, ArrayList(bhklistResponseData))
            bundle.putParcelableArrayList(PLANLIST, ArrayList(getServicePlanResponseData))
            bundle.putString(PLAN_ID, planid.toString())
            bundle.putString(PINCODE_ID, pincodeid)
            bundle.putString(ARG_PRICE,price.toString())
            bundle.putString(ARG_DISCOUNTEDPRICE,discountedPrice.toString())

            fragment.arguments = bundle
            return fragment
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.custom_bottom_sheet_add_bhk, container, false)

        val serviceList = arguments?.getParcelableArrayList<BhklistResponseData>(ARG_DATA)

        val planlist = arguments?.getParcelableArrayList<GetServicePlanResponseData>(ARG_DATA)
        var pincode = arguments?.getString(PINCODE_ID).toString()
        var planid = arguments?.getString(PLAN_ID).toString()
        prices=arguments?.getString(ARG_PRICE).toString()
        discountedPrice=arguments?.getString(ARG_DISCOUNTEDPRICE).toString()



        bhkandpincodedata = ArrayList<BHKandPincodeData>()

        val pricewisebhk = view.findViewById<AppCompatTextView>(R.id.pricewisebhk)
        val discountedamount= view.findViewById<AppCompatTextView>(R.id.discountedamount)
        val button = view.findViewById<TextView>(R.id.btnproceed)

        pricewisebhk.text =
            "\u20B9" + prices.toString()
        discountedamount.text =
            "\u20B9" + discountedPrice.toString()
        pricewisebhk.paintFlags =
            pricewisebhk.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

//        button.alpha=0.6f

        val recycleview = view.findViewById<RecyclerView>(R.id.recycleviewaddarea)

        recycleview.layoutManager = GridLayoutManager(activity, 3)
//        recycleview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = BookingServiceBhklistAdapter()
        recycleview.adapter = mAdapter

        if (serviceList != null) {
            mAdapter.setServiceList(serviceList, activity!!)
        }

        mAdapter.setonViewdatail(object : OnBookingFlatPerPrice {
            override fun onClickonFlat(position: Int, noofbhk: String?, selectedPosition: Int) {

                viewProductModel.activebhkpincode.observe(activity!!, Observer {
                    if (it.isNotEmpty()) {

                        try {

                            bhkandpincodedata = it

                            for (i in 0 until bhkandpincodedata.size) {
                                if (bhkandpincodedata.get(i).Pincode.equals("400601")) {

                                    pricewisebhk.text =
                                        "\u20B9" + bhkandpincodedata.get(i).Price.toString()
                                    discountedamount.text =
                                        "\u20B9" + bhkandpincodedata.get(i).DiscountedPrice.toString()
                                    pricewisebhk.paintFlags =
                                        pricewisebhk.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG


                                    price = bhkandpincodedata.get(i).Price.toString()
                                    AppUtils2.bookingserviceprice =
                                        bhkandpincodedata.get(i).Price.toString()
//                                if (bhkandpincodedata.get(i).Price != 0) {
//                                }else{
//                                    pricewisebhk.text = "\u20B9" + "00"
//                                }
                                }
                            }
                        }catch(e:Exception){
                            e.printStackTrace()
                        }

                    } else {

                    }
                })

                viewProductModel.getPlanAndPriceByBHKandPincode("400601", noofbhk.toString(), "CMS")

//                viewProductModel.getPlanAndPriceByBHKandPincode(
//                    pincode,
//                    noofbhk.toString(),
//                    AppUtils2.servicecode
//                )
            }
        })

        button.setOnClickListener {
            val intent = Intent(activity, MapsDemoActivity::class.java)
            intent.putExtra("ServiceCenter_Id", "")
            intent.putExtra("SlotDate", "")
            intent.putExtra("TaskId", "")
            intent.putExtra("SkillId", "")
            intent.putExtra("Latt", "")
            intent.putExtra("Longg", "")
            intent.putExtra("ServiceType", "pest")
            intent.putExtra("Pincode", AppUtils2.pincode)
            intent.putExtra("Service_Code", "CMS")
            intent.putExtra("Unit", Unit)
            intent.putExtra("SPCode", spcode)
//            ServiceCenter_Id = intent.getStringExtra("ServiceCenter_Id").toString()
//            SlotDate = intent.getStringExtra("SlotDate").toString()
//            TaskId = intent.getStringExtra("TaskId").toString()
//            SkillId = intent.getStringExtra("SkillId").toString()
//            Latt = intent.getStringExtra("Lat").toString()
//            Longg = intent.getStringExtra("Long").toString()
//            ServiceType = intent.getStringExtra("ServiceType").toString()
//            Pincode = intent.getStringExtra("Pincode").toString()
//            Service_Code = intent.getStringExtra("Service_Code").toString()
//            Unit = intent.getStringExtra("Unit").toString()
//            spcode = intent.getStringExtra("SPCode").toString()
            startActivity(intent)
//            Toast.makeText(activity,AppUtils2.getServicePlanResponseData.toString(),Toast.LENGTH_LONG).show()
//            Toast.makeText(activity, planlist.toString(), Toast.LENGTH_LONG).show()

        }
        return view
    }
}