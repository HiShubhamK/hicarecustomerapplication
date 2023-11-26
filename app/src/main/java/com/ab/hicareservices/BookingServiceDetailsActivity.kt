package com.ab.hicareservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ab.hicareservices.databinding.ActivityBookingServiceDetailsBinding
import com.ab.hicareservices.ui.adapter.BookingServiceBhklistAdapter
import com.ab.hicareservices.ui.viewmodel.ServiceBooking

class BookingServiceDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingServiceDetailsBinding
    private val viewProductModel: ServiceBooking by viewModels()
    private lateinit var mAdapter: BookingServiceBhklistAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_service_details)
        binding = ActivityBookingServiceDetailsBinding.inflate(layoutInflater)



        binding.recycleviewaddarea.layoutManager = GridLayoutManager(this, 3)
//        recycleview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = BookingServiceBhklistAdapter()
        binding.recycleviewaddarea.adapter = mAdapter


        viewProductModel.activebhklist.observe(this, Observer {
            if (it.isNotEmpty()) {
                    mAdapter.setServiceList(it, this!!)

            } else {

            }
        })
        viewProductModel.getActiveBHKList()

    }
}