package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.databinding.ActivityPestServicesBinding
import com.ab.hicareservices.ui.adapter.BookingServiceListAdapter
import com.ab.hicareservices.ui.viewmodel.ServiceBooking

class PestServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPestServicesBinding
    private lateinit var mAdapter: BookingServiceListAdapter
    private val viewProductModel: ServiceBooking by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pest_services)
        binding = ActivityPestServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.recMenu.layoutManager = GridLayoutManager(this@PestServicesActivity, 3)
        binding.recMenu.layoutManager = LinearLayoutManager(this@PestServicesActivity, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceListAdapter()
        binding.recMenu.adapter = mAdapter

        viewProductModel.serviceresponssedata.observe(this@PestServicesActivity, Observer {
            if (it.isNotEmpty()) {
                mAdapter.setServiceList(it,this)
            } else {

            }
        })
        viewProductModel.getActiveServiceList()
    }
}