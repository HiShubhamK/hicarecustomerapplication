package com.ab.hicareservices.ui.view.fragments

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.getslots.Data
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.FragmentComplainceBinding
import com.ab.hicareservices.ui.adapter.SlotCompliceAdapater
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.handler.onSlotSelection
import com.ab.hicareservices.ui.handler.onSlotclick
import com.ab.hicareservices.ui.viewmodel.GetSlotViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import java.util.Calendar


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
    private var Pincode = ""
    private var Service_Code = ""
    private var Unit = ""
    private var AppointmentDate = ""
    private var AppointmentStart = ""
    private var AppointmentEnd = ""
    private var Source = ""
    var service = mutableListOf<ServiceData>()
    private val viewModels: OtpViewModel by viewModels()
    lateinit var mSlotAdapter: SlotsAdapter
    lateinit var alertDialog: AlertDialog
    lateinit var progressDialog: ProgressDialog


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
            Pincode = it.getString("Pincode").toString()
            Service_Code = it.getString("Service_Code").toString()
            Unit = it.getString("Unit").toString()

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
            serviceType: String,
            pincode: String?,
            spcode: String?,
            serviceUnit: String?
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
                    this.putString("Pincode", pincode)
                    this.putString("Service_Code", spcode)
                    this.putString("Service_Date", "")
                    this.putString("Service_Subscription", "")
                    this.putString("Unit", serviceUnit)
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
        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        AppUtils2.mobileno = SharedPreferenceUtil.getData(requireActivity(), "mobileNo", "-1").toString()
//        viewModels.validateAccount(AppUtils2.mobileno)

//        getOrdersList2()
//        mcurrentDate.add(Calendar.DATE, 1);
        binding.ivCalender.setOnClickListener{
            val c = Calendar.getInstance()
            val dialog = DatePickerDialog(
                context!!,
                { view, year, month, dayOfMonth ->
                    val _year = year.toString()
                    val _month = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
                    val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                    val _pickedDate = "$year-$_month-$_date"
                    Log.e("PickedDate: ", "Date: $_pickedDate") //2019-02-12
                }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.MONTH]
            )
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        }
//        binding.calendarView.setOnDateChangeListener(OnDateChangeListener { CalendarView, year, month, dayOfMonth ->
//            val date = "$year-$month-$dayOfMonth"
//            Log.e(TAG, "onSelectedDayChange: yyyy/mm/dd:$date")
////            val intent = Intent(this@CalendarActivity, MainActivity::class.java)
////            intent.putExtra("date", date)
////            startActivity(intent)
//            Handler(Looper.getMainLooper()).postDelayed({
//                getOrdersList(date)
//            }, 1000)
//
//        })


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

        progressDialog.show()

        binding.recyclerView.visibility = View.GONE
        binding.recyclerView.layoutManager =
            GridLayoutManager(context, 2)
//        binding.recyclerView.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = SlotCompliceAdapater()

        binding.recyclerView.adapter = mAdapter

        viewModel.getcomplainceresponse.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            mAdapter.serComplainceList(
                it,
                requireActivity(),
                Pincode,
                Service_Code,
                Unit,
                Lat,
                Long,
                ServiceType
            )
            progressDialog.dismiss()


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


        mAdapter.onSlotclick(object : onSlotclick {
            override fun onSlotItemclicked(
                position: Int,
                Pincode: String,
                Service_Code: String,
                Service_Date: String,
                Service_Subscription: String?,
                unit: String?,
                Lat: String,
                Long: String,
                ServiceType: String,
                toString: String,
                datetxt: String
            ) {
                progressDialog.show()
                var data = HashMap<String, Any>()
                data["Pincode"] = Pincode
                data["Service_Code"] = Service_Code
                data["Service_Date"] = Service_Date
                data["Service_Subscription"] = ""
                data["Unit"] = unit.toString()
                data["Lat"] = Lat
                data["Long"] = Long
                data["ServiceType"] = ServiceType
                viewModel.GetSlots(data)
                viewModel.getSlotresponse.observe(requireActivity(), Observer {
                    Log.d(TAG, "onViewCreated: $it orders fragment")
                    ShowBookingDialog(it)

                })

            }


        })




        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            binding.recyclerView.visibility = View.GONE
//            binding.txterrormessage.visibility = View.VISIBLE
        })
        Log.e(
            "TAG",
            "Data11: " + ServiceCenter_Id + ", " + SlotDate + ", " + TaskId + ", " + SkillId + Lat + ", " + Long + ", " + ServiceType
        )

    }

    private fun ShowBookingDialog(slotData: Data) {
        val li = LayoutInflater.from(activity)
        val promptsView = li.inflate(R.layout.reschedule_layout, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(promptsView)
        alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        val btnSubmit = promptsView.findViewById<View>(R.id.btnSubmit) as Button
        val imgClose = promptsView.findViewById<View>(R.id.imgClose) as ImageView
        val recycleWeeks: RecyclerView =
            promptsView.findViewById<View>(R.id.recycleView) as RecyclerView
        val recycleSlots: RecyclerView =
            promptsView.findViewById<View>(R.id.recycleSlots) as RecyclerView
        recycleWeeks.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycleWeeks.layoutManager = layoutManager
        recycleSlots.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recycleSlots.layoutManager = layoutManager

        mSlotAdapter = SlotsAdapter(requireActivity(), slotData.TimeSlots, TaskId)
        recycleSlots.adapter = mSlotAdapter
        alertDialog.show()
        imgClose.setOnClickListener{
            alertDialog.dismiss()
        }
        btnSubmit.setOnClickListener {
            progressDialog.show()

            var data = HashMap<String, Any>()
            data["TaskId"] = TaskId
            data["AppointmentDate"] = AppointmentDate
            data["AppointmentStart"] = AppointmentStart
            data["AppointmentEnd"] =AppointmentEnd
            data["Source"] = Source
            data["ServiceType"] = ServiceType
            viewModel.BookSlot(data)

            viewModel.bookSlotResponce.observe(requireActivity(), Observer {
                Log.d(TAG, "onViewCreated: $it orders fragment")
//                ShowBookingDialog(it)
                if (it.IsSuccess==true) {
                    Toast.makeText(requireContext(), it.Data!!.ResponseMessage , Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()

                }else{
                    Toast.makeText(requireContext(), it.ResponseMessage , Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()

                }

            })

            progressDialog.dismiss()

        }

        mSlotAdapter.setOnSlotSelection(object : onSlotSelection {
//

            override fun onSlotBookSelect(
                position: Int,
                taskid: String,
                appointmentDate: String,
                appointmentStart: String,
                appointmentEnd: String?,
                source: String?,
                serviceType: String
            ) {

                TaskId=taskid
                AppointmentDate=appointmentDate
                AppointmentStart=appointmentStart
                AppointmentEnd=appointmentEnd!!
                Source=source!!

            }


        })
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}