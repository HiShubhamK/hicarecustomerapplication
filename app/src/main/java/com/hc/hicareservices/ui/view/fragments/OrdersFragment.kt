package com.hc.hicareservices.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hc.hicareservices.R
import com.hc.hicareservices.data.SharedPreferenceUtil
import com.hc.hicareservices.databinding.FragmentOrdersBinding
import com.hc.hicareservices.ui.adapter.OrdersAdapter
import com.hc.hicareservices.ui.handler.OnOrderClickedHandler
import com.hc.hicareservices.ui.viewmodel.OrdersViewModel


class OrdersFragment : Fragment() {
    private val TAG = "OrdersFragment"
    lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private var mobile = ""

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
        binding.swipeRefreshLayout.setOnRefreshListener {
            getOrdersList()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        getOrdersList()
    }

    private fun getOrdersList() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        mAdapter = OrdersAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            mAdapter.setOrdersList(it)
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        })
        mAdapter.setOnOrderItemClicked(object : OnOrderClickedHandler {
            override fun onOrderItemClicked(position: Int, orderNo: String, serviceType: String) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, OrderDetailsFragment.newInstance(orderNo, serviceType)).addToBackStack("OrdersFragment").commit();
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