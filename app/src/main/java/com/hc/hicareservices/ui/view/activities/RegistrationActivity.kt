package com.hc.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hc.hicareservices.R
import com.hc.hicareservices.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.backIv.setOnClickListener {
            finish()
        }
        binding.alreadyHaveAccTv.setOnClickListener {
            finish()
        }
    }
}