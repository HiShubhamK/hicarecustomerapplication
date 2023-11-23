package com.ab.hicareservices.ui.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.servicesmodule.BHKandPincodeData
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.ui.adapter.BookingServiceBhklistAdapter
import com.ab.hicareservices.ui.handler.OnBookingFlatPerPrice
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.collections.ArrayList

class CustomBottomSheetAddBhkFragment(): BottomSheetDialogFragment() {

    private lateinit var mAdapter: BookingServiceBhklistAdapter
    private val viewProductModel: ServiceBooking by viewModels()
    lateinit var bhkandpincodedata: List<BHKandPincodeData>


    companion object {
        private const val ARG_DATA = "list_key"

        fun newInstance(
            bhklistResponseData: List<BhklistResponseData>,
            planid: Int?,
            pincodeid: String
        ): CustomBottomSheetAddBhkFragment {
            val fragment = CustomBottomSheetAddBhkFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_DATA, ArrayList(bhklistResponseData) )
            fragment.arguments = bundle
            return fragment
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.custom_bottom_sheet_add_bhk, container, false)

        val serviceList = arguments?.getParcelableArrayList<BhklistResponseData>(ARG_DATA)

        bhkandpincodedata=ArrayList<BHKandPincodeData>()

        val pricewisebhk = view.findViewById<AppCompatTextView>(R.id.pricewisebhk)

        val button = view.findViewById<TextView>(R.id.btnproceed)

        val recycleview = view.findViewById<RecyclerView>(R.id.recycleviewaddarea)

        recycleview.layoutManager = GridLayoutManager(activity, 3)
//        recycleview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = BookingServiceBhklistAdapter()
       recycleview.adapter = mAdapter

        mAdapter.setServiceList(serviceList, activity!!)

        mAdapter.setonViewdatail(object :OnBookingFlatPerPrice{
            override fun onClickonFlat(position: Int, noofbhk: String?) {

                viewProductModel.activebhkpincode.observe(activity!!, Observer {
                    if (it.isNotEmpty()) {

                        bhkandpincodedata=it
                        for (i in 0 until bhkandpincodedata.size) {
                            if(bhkandpincodedata.get(i).Pincode.equals("400601")) {
                                pricewisebhk.text = bhkandpincodedata.get(i).Price.toString()
                            }
                        }

                    } else {

                    }
                })
                viewProductModel.getPlanAndPriceByBHKandPincode("400601", noofbhk.toString(),"CMS")

            }
        })

        button.setOnClickListener {

        }
        return view
    }
}
