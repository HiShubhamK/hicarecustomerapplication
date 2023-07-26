package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityComplaintNewBinding
import com.ab.hicareservices.databinding.ActivityComplaintsBinding
import com.ab.hicareservices.ui.adapter.ProductViewPagerAdapter
import com.ab.hicareservices.ui.view.fragments.ProductComplaintsFragment

class ComplaintNewActivity : AppCompatActivity() {

    lateinit var binding: ActivityComplaintNewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_new)
        binding = ActivityComplaintNewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val orderfragment = ProductComplaintsFragment()
        val extraList = ProductComplaintsFragment()
        val adapter = ProductViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(orderfragment, "Services")
        adapter.addFragment(extraList, "Products")

        val viewPager = binding.vpFragments
        viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.vpFragments)

    }
}