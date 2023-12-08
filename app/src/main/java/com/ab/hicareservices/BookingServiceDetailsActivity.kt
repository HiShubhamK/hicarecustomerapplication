package com.ab.hicareservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.data.model.servicesmodule.BHKandPincodeData
import com.ab.hicareservices.databinding.ActivityBookingServiceDetailsBinding
import com.ab.hicareservices.ui.adapter.BookingServiceDetailsAdapters
import com.ab.hicareservices.ui.viewmodel.ServiceBooking

class BookingServiceDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingServiceDetailsBinding
    private val viewProductModel: ServiceBooking by viewModels()
    private lateinit var mAdapter: BookingServiceDetailsAdapters
    lateinit var bhkandpincodedata: List<BHKandPincodeData>
    var price = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingServiceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recMenu.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceDetailsAdapters()
        binding.recMenu.adapter = mAdapter

        viewProductModel.activebhklist.observe(this, Observer {
            if (it.isNotEmpty()) {
                mAdapter.setServiceList(it,this)
            } else {

            }
        })



//        binding.recycleviewaddarea.layoutManager = GridLayoutManager(this, 3)
//        mAdapter = BookingServiceBhklistAdapter()
//        binding.recycleviewaddarea.adapter = mAdapter
//        binding.recycleviewaddarea.adapter = mAdapter

//        binding.recycleviewaddarea.layoutManager = GridLayoutManager(this, 3)
//        binding.recycleviewdata.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        mAdapter = BookingServiceDetailsAdapters()
//        binding.recycleviewdata.adapter = mAdapter
//
//        viewProductModel.activebhklist.observe(this, Observer {
//            if (it.isNotEmpty()) {
//                mAdapter.setServiceList(it, this@BookingServiceDetailsActivity)
//                Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
//            } else {
//
//            }
//        })
//        viewProductModel.getActiveBHKList()
//
//        mAdapter.setonViewdatail(object : OnBookingFlatPerPrice {
//            override fun onClickonFlat(position: Int, noofbhk: String?) {
//                viewProductModel.activebhkpincode.observe(
//                    this@BookingServiceDetailsActivity,
//                    Observer {
//                        if (it.isNotEmpty()) {
//                            bhkandpincodedata = it
//                            for (i in 0 until bhkandpincodedata.size) {
//                                if (bhkandpincodedata.get(i).Pincode.equals("400601")) {
//
////                                pricewisebhk.text = "\u20B9" + bhkandpincodedata.get(i).Price.toString()
//                                    price = bhkandpincodedata.get(i).Price.toString()
//                                    AppUtils2.bookingserviceprice =
//                                        bhkandpincodedata.get(i).Price.toString()
//                                }
//                            }
//
//                        } else {
//
//                        }
//                    })
//                viewProductModel.getPlanAndPriceByBHKandPincode("400601", noofbhk.toString(), "CMS")
//            }
//        })


    }

    override fun onResume() {
        super.onResume()
        viewProductModel.getActiveBHKList()

    }
}