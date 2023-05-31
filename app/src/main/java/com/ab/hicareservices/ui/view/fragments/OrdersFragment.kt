package com.ab.hicareservices.ui.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentOrdersBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.view.activities.PaymentActivity
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import org.json.JSONObject

class OrdersFragment() : Fragment() {
    private val TAG = "OrdersFragment"
    lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype=""
    lateinit var homeActivity: HomeActivity
    lateinit var orderactivityforadapter:FragmentActivity
    lateinit var options: JSONObject


    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { activityResult ->
            val result = activityResult.resultCode
            val data = activityResult.data
        }
    )


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
//        getOrdersList2()
        Handler(Looper.getMainLooper()).postDelayed({
//            getOrdersList()
        }, 1000)


        binding.txtactive.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            ordertype="Active"
            getOrdersList()
            binding.activetxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))

        }

        binding.txtexpire.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            ordertype="Expired"
            getOrdersList()
            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.expiretxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
        }

        binding.txtcancelled.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            ordertype="Cancelled"
            getOrdersList()
            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
        }


        binding.txtall.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            ordertype="All"
            getOrdersList2()
            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.alltext.setTextColor(Color.parseColor("#2bb77a"))
            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
        }


    }

    private fun getOrdersList2() {

        binding.progressBar.visibility = View.VISIBLE
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
            if(ordertype.equals("") && ordertype!=null){
                ordertype="Active"
                viewModel.getCustomerOrdersByMobileNo(mobile)
            }else{
                viewModel.getCustomerOrdersByMobileNo(mobile)
            }

        }

    }

    private fun getOrdersList() {

        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = OrdersAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            mAdapter.setOrdersList(it,requireActivity())
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

        })
        mAdapter.setOnOrderItemClicked(object : OnOrderClickedHandler {
            override fun onOrderItemClicked(
                position: Int,
                orderNo: String,
                serviceType: String,
                service_url_image: String,
                locationLatitudeS: Double?,
                locationLongitudeS: Double?,
                ServiceCenterId: String,
            ) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, OrderDetailsFragment.newInstance(orderNo, serviceType,
                        service_url_image,locationLatitudeS,locationLongitudeS,ServiceCenterId
                    )).addToBackStack("OrdersFragment").commit();
            }

            override fun onOrderPaynowClicked(
                position: Int,
                orderNumberC: String,
                customerIdC: String,
                servicePlanNameC: String,
                orderValueWithTaxC: Double
            ) {
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("ORDER_NO", orderNumberC)
                intent.putExtra("ACCOUNT_NO", customerIdC)
                intent.putExtra("SERVICETYPE_NO", servicePlanNameC)
                intent.putExtra("PAYMENT", orderValueWithTaxC)
                activityResultLauncher.launch(intent)
            }
        })


        viewModel.errorMessage.observe(requireActivity(), Observer {

        })
        if (mobile != "-1") {
            if(ordertype.equals("") && ordertype!=null){
                ordertype="Active"
                viewModel.getCustomerOrdersByMobileNo(mobile, ordertype)
            }else{
                viewModel.getCustomerOrdersByMobileNo(mobile, ordertype)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}