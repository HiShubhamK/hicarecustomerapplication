package com.ab.hicareservices.ui.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentOrdersBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel

class OrdersFragment() : Fragment() {
    private val TAG = "OrdersFragment"
    lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    lateinit var homeActivity: HomeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrdersFragment().apply {
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
        getOrdersList()
        getOrdersList2()
        Handler(Looper.getMainLooper()).postDelayed({
//            getOrdersList()
        }, 1000)

    }

    private fun getOrdersList2() {

        binding.recyclerView2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        nAdapter = OrderMenuAdapter()

        binding.recyclerView2.adapter = nAdapter

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            nAdapter.setOrdersList(it)
            binding.progressBar.visibility = View.GONE
//            binding.recyclerView2.visibility = View.VISIBLE
        })
//        mAdapter.setOnOrderItemClicked(object : OnOrderClickedHandler {
//            override fun onOrderItemClicked(
//                position: Int,
//                orderNo: String,
//                serviceType: String,
//                service_url_image: String
//            ) {
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, OrderDetailsFragment.newInstance(orderNo, serviceType,service_url_image)).addToBackStack("OrdersFragment").commit();
//            }
//        })

        viewModel.errorMessage.observe(requireActivity(), Observer {

        })
        if (mobile != "-1") {
            viewModel.getCustomerOrdersByMobileNo(mobile)
        }

    }

    private fun getOrdersList() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = OrdersAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            mAdapter.setOrdersList(it)
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        })
        mAdapter.setOnOrderItemClicked(object : OnOrderClickedHandler {
            override fun onOrderItemClicked(
                position: Int,
                orderNo: String,
                serviceType: String,
                service_url_image: String
            ) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, OrderDetailsFragment.newInstance(orderNo, serviceType,service_url_image)).addToBackStack("OrdersFragment").commit();
            }
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {

        })
        if (mobile != "-1") {
            viewModel.getCustomerOrdersByMobileNo(mobile)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}