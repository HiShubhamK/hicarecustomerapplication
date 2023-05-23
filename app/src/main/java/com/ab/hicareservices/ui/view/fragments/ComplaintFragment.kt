package com.ab.hicareservices.ui.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityComplaintsBinding
import com.ab.hicareservices.ui.adapter.ComplaintsAdapter
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel

class ComplaintFragment() : Fragment() {
    private val TAG = "OrdersFragment"
    lateinit var binding: ActivityComplaintsBinding
    private val viewModel: ComplaintsViewModel by viewModels()
    private lateinit var mAdapter: ComplaintsAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    lateinit var homeActivity: HomeActivity
    private val viewModeld: OtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityComplaintsBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ComplaintFragment().apply {
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
        viewModeld.validateAccount("9967994682")

//        getOrdersList2()
        Handler(Looper.getMainLooper()).postDelayed({
            getOrdersList()

        }, 1000)

    }


    private fun getOrdersList() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mAdapter = ComplaintsAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.complaintList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            mAdapter.setComplaintsList(it)
//            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
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
        viewModel.getAllComplaints("9967994682")


//        if (mobile != "-1") {
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}