package com.ab.hicareservices.ui.view.activities

import android.content.*
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityInappWebviewBinding
import com.ab.hicareservices.utils.AppUtils2
import java.util.*


class InAppWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInappWebviewBinding

    var pagelink=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityInappWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppUtils2.checkmenuButton=false
        val intent = intent
        pagelink = intent.getStringExtra("PageLink").toString()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.pluginState = PluginState.ON
        binding.webView.settings.allowFileAccess = true
        binding.webView.isVerticalScrollBarEnabled = true;
        binding.webView.isHorizontalScrollBarEnabled = true;

        binding.webView.loadUrl(pagelink) // Here You can put your Url

        binding.webView.webChromeClient = object : WebChromeClient() {}

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                //Toast.makeText(context, "Page Load Finished", Toast.LENGTH_SHORT).show();
            }
        }

    }





    //









    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        super.onBackPressed()


    }


}