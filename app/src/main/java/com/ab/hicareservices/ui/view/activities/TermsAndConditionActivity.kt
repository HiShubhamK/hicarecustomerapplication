package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityTermsAndConditionBinding

class TermsAndConditionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_condition)
        binding= ActivityTermsAndConditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webView.isVerticalScrollBarEnabled = true;
        binding.webView.requestFocus();
        binding.webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        binding.webView.settings.defaultTextEncodingName = "utf-8";
        binding.webView.settings.javaScriptEnabled = true;
        binding.webView.isScrollbarFadingEnabled = true
        binding.webView.loadUrl("https://hicare.in/terms-conditions")

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}