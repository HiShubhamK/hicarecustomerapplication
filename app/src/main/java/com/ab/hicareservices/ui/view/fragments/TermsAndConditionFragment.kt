package com.ab.hicareservices.ui.view.fragments

import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentTermsBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel


class TermsAndConditionFragment : Fragment() {
    private val TAG = "TermsAndConditionFragment"
    lateinit var binding: FragmentTermsBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    lateinit var homeActivity: HomeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TermsAndConditionFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.swipeRefreshLayout.setOnRefreshListener {
//            getOrdersList()
//            getOrdersList2()
//            binding.swipeRefreshLayout.isRefreshing = false
//        }

        setupHTMLWebView()

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupHTMLWebView() {
//        binding.webView.settings.javaScriptEnabled = true
////        binding.webView.webViewClient = object : WebViewClient() {
////            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
////                view?.loadUrl(url)
////                return true
////            }
////        }
//        binding.webView.loadUrl("https://hicare.in/terms-conditions")
//        binding.webView.isVerticalScrollBarEnabled = true
//        binding.webView.isHorizontalScrollBarEnabled = true

        binding.webView.isVerticalScrollBarEnabled = true;
        binding.webView.requestFocus();
        binding.webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        binding.webView.settings.defaultTextEncodingName = "utf-8";
        binding.webView.settings.javaScriptEnabled = true;
        binding.webView.isScrollbarFadingEnabled = true
        binding.webView.loadUrl("https://hicare.in/terms-conditions")


    }


}