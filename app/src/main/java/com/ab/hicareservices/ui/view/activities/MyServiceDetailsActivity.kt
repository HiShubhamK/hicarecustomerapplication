package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.service.ServiceTaskData
import com.ab.hicareservices.databinding.ActivityMyServiceDetailsBinding
import com.ab.hicareservices.databinding.ActivitySlotComplinceBinding
import com.ab.hicareservices.ui.adapter.MyServiceViewDetailsAdapter

class MyServiceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyServiceDetailsBinding
    private val tasksList: List<ServiceTaskData>? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: MyServiceViewDetailsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_service_details)
        binding=ActivityMyServiceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

        binding.recycleView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        binding.recycleView.layoutManager = layoutManager
        mAdapter = MyServiceViewDetailsAdapter(tasksList)
        binding.recycleView.adapter = mAdapter


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}