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
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.FragmentComplainceBinding
import com.ab.hicareservices.ui.adapter.SlotCompliceAdapater
import com.ab.hicareservices.ui.viewmodel.GetSlotViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2

class SlotComplinceFragment() : Fragment() {
    private val TAG = "SlotComplinceFragment"
    lateinit var binding: FragmentComplainceBinding
    private val viewModel: GetSlotViewModel by viewModels()
    private lateinit var mAdapter: SlotCompliceAdapater
    private var mobile = ""
    private var ordertype = ""
    private var ServiceCenter_Id = ""
    private var SlotDate = ""
    private var TaskId = ""
    private var Lat = ""
    private var Long = ""
    private var ServiceType = ""
    var service = mutableListOf<ServiceData>()
    private val viewModels: OtpViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            ServiceCenter_Id = it.getString("ServiceCenter_Id").toString()
            SlotDate = it.getString("SlotDate").toString()
            TaskId = it.getString("TaskId").toString()
            Lat = it.getString("Lat").toString()
            Long = it.getString("Long").toString()
            ServiceType = it.getString("ServiceType").toString()

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComplainceBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(
            ServiceCenter_Id: String,
            SlotDate: String,
            TaskId: String,
            latitude: String,
            longitude: String,
            serviceType: String
        ) =
            SlotComplinceFragment().apply {
                arguments = Bundle().apply {
                    this.putString("ServiceCenter_Id", ServiceCenter_Id)
                    this.putString("SlotDate", SlotDate)
                    this.putString("TaskId", TaskId)
                    this.putString("Lat", latitude)
                    this.putString("Long", longitude)
                    this.putString("ServiceType", serviceType)
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
        viewModels.validateAccount(AppUtils2.mobileno)

//        getOrdersList2()
        Handler(Looper.getMainLooper()).postDelayed({
            getOrdersList()
        }, 1000)


//        binding.txtactive.setOnClickListener{
//            binding.progressBar.visibility = View.VISIBLE
//            ordertype="Active"
//            getOrdersList()
//            binding.activetxt.setTextColor(Color.parseColor("#2bb77a"))
//            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
//
//        }
//
//        binding.txtexpire.setOnClickListener{
//            binding.progressBar.visibility = View.VISIBLE
//            ordertype="Expired"
//            getOrdersList()
//            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.expiretxt.setTextColor(Color.parseColor("#2bb77a"))
//            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
//        }
//
//        binding.txtcancelled.setOnClickListener{
//            binding.progressBar.visibility = View.VISIBLE
//            ordertype="Cancelled"
//            getOrdersList()
//            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.cancelledtxt.setTextColor(Color.parseColor("#2bb77a"))
//            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
//        }
//
//
//        binding.txtall.setOnClickListener{
//            binding.progressBar.visibility = View.VISIBLE
//            ordertype="All"
//            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.alltext.setTextColor(Color.parseColor("#2bb77a"))
//            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
//            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
//        }


    }


    private fun getOrdersList() {

        binding.progressBar.visibility = View.VISIBLE

        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = SlotCompliceAdapater()

        binding.recyclerView.adapter = mAdapter

        viewModel.getcomplainceresponse.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            mAdapter.serComplainceList(it, requireActivity())
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE

        })
        Handler(Looper.getMainLooper()).postDelayed({
            var data = HashMap<String, Any>()
            data["ServiceCenter_Id"] = ServiceCenter_Id
            data["SlotDate"] = SlotDate
            data["TaskId"] = TaskId
            data["Lat"] = Lat
            data["Long"] = Long
            data["ServiceType"] = ServiceType
            viewModel.getComplainceData(data)
        }, 1000)


        Log.e("TAG","Data11: "+ServiceCenter_Id+", "+SlotDate+", "+TaskId+", "+Lat+", "+Long+", "+ServiceType)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}