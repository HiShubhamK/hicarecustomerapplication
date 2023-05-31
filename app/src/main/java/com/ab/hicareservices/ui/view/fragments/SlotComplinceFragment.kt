package com.ab.hicareservices.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.FragmentComplainceBinding
import com.ab.hicareservices.ui.adapter.SlotCompliceAdapater
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.view.activities.PaymentActivity
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
    private var SkillId = ""
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
            SkillId = it.getString("SkillId").toString()
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
            SkillId: String,
            latitude: String,
            longitude: String,
            serviceType: String
        ) =
            SlotComplinceFragment().apply {
                arguments = Bundle().apply {
                    this.putString("ServiceCenter_Id", ServiceCenter_Id)
                    this.putString("SlotDate", SlotDate)
                    this.putString("TaskId", TaskId)
                    this.putString("SkillId", SkillId)
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
        binding.calendarView.setOnDateChangeListener(OnDateChangeListener { CalendarView, year, month, dayOfMonth ->
            val date = "$year/$month/$dayOfMonth"
            Log.e(TAG, "onSelectedDayChange: yyyy/mm/dd:$date")
//            val intent = Intent(this@CalendarActivity, MainActivity::class.java)
//            intent.putExtra("date", date)
//            startActivity(intent)
            Handler(Looper.getMainLooper()).postDelayed({
                getOrdersList(date)
            }, 1000)

        })


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


    private fun getOrdersList(date: String) {


        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.recyclerView.layoutManager =
            GridLayoutManager(context, 2)
//        binding.recyclerView.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
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
            data["SlotDate"] = date
            data["TaskId"] = TaskId
            data["SkillId"] = SkillId
            data["Lat"] = Lat
            data["Long"] = Long
            data["ServiceType"] = ServiceType
            viewModel.getComplainceData(data)
        }, 1000)
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
//                {
//                    "Pincode": "string",
//                    "Service_Code": "string",
//                    "Service_Date": "2023-05-31T12:37:17.019Z",
//                    "Service_Subscription": "string",
//                    "Unit": "string",
//                    "Lat": "string",
//                    "Long": "string",
//                    "ServiceType": "string"
//                }
                var data = HashMap<String, Any>()
                data["Pincode"] = ServiceCenter_Id
                data["Service_Code"] = date
                data["Service_Date"] = TaskId
                data["Service_Subscription"] = SkillId
                data["Unit"] = Lat
                data["Lat"] = Long
                data["Long"] = Long
                data["ServiceType"] = ServiceType
                viewModel.GetSlots(data)
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(
//                        R.id.container, SlotComplinceFragment.newInstance(orderNo, serviceType,
//                        service_url_image,locationLatitudeS,locationLongitudeS,ServiceCenterId
//                    )).addToBackStack("OrdersFragment").commit();
            }

            override fun onOrderPaynowClicked(
                position: Int,
                orderNumberC: String,
                customerIdC: String,
                servicePlanNameC: String,
                orderValueWithTaxC: Double
            ) {
//                val intent = Intent(requireContext(), PaymentActivity::class.java)
//                intent.putExtra("ORDER_NO", orderNumberC)
//                intent.putExtra("ACCOUNT_NO", customerIdC)
//                intent.putExtra("SERVICETYPE_NO", servicePlanNameC)
//                intent.putExtra("PAYMENT", orderValueWithTaxC)
//                activityResultLauncher.launch(intent)
            }
        })




        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            binding.recyclerView.visibility = View.GONE
//            binding.txterrormessage.visibility = View.VISIBLE
        })
        Log.e(
            "TAG",
            "Data11: " + ServiceCenter_Id + ", " + SlotDate + ", " + TaskId + ", "+ SkillId + Lat + ", " + Long + ", " + ServiceType
        )

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}