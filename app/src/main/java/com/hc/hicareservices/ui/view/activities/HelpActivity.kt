package com.hc.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hc.hicareservices.R
import com.hc.hicareservices.databinding.ActivityHelpBinding
import com.hc.hicareservices.databinding.ActivityMainBinding

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}