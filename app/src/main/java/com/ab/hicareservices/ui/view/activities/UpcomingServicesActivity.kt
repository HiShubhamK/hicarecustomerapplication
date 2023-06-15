package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityUpcomingServicesBinding
import com.ab.hicareservices.databinding.LayoutBinding

class UpcomingServicesActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpcomingServicesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_services)
        binding= ActivityUpcomingServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}