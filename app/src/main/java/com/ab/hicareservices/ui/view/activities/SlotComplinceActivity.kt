package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.getslots.Data
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.ActivitySlotComplinceBinding
import com.ab.hicareservices.ui.adapter.SlotCompliceAdapater
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.handler.onSlotSelection
import com.ab.hicareservices.ui.handler.onSlotclick
import com.ab.hicareservices.ui.viewmodel.GetSlotViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2

class SlotComplinceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlotComplinceBinding
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
        setContentView(R.layout.activity_slot_complince)
        binding = ActivitySlotComplinceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent=intent

        ServiceCenter_Id = intent.getStringExtra("ServiceCenter_Id").toString()
        SlotDate = intent.getStringExtra("SlotDate").toString()
        TaskId =intent.getStringExtra("TaskId").toString()
        SkillId = intent.getStringExtra("SkillId").toString()
        Lat = intent.getStringExtra("Lat").toString()
        Long = intent.getStringExtra("Long").toString()
        ServiceType = intent.getStringExtra("ServiceType").toString()
        Pincode = intent.getStringExtra("Pincode").toString()
        Service_Code = intent.getStringExtra("Service_Code").toString()
        Unit = intent.getStringExtra("Unit").toString()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { CalendarView, year, month, dayOfMonth ->
            val date = "$year-$month-$dayOfMonth"
//            Log.e(TAG, "onSelectedDayChange: yyyy/mm/dd:$date")
//            val intent = Intent(this@CalendarActivity, MainActivity::class.java)
//            intent.putExtra("date", date)
//            startActivity(intent)
            Handler(Looper.getMainLooper()).postDelayed({
                getOrdersList(date)
            }, 1000)

        })
    }

    private fun getOrdersList(date: String) {

        progressDialog.show()

        binding.recyclerView.visibility = View.GONE
        binding.recyclerView.layoutManager =
            GridLayoutManager(this, 2)
//        binding.recyclerView.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = SlotCompliceAdapater()

        binding.recyclerView.adapter = mAdapter

        viewModel.getcomplainceresponse.observe(this, Observer {
//            Log.d(TAG, "onViewCreated: $it orders fragmentent")
            mAdapter.serComplainceList(
                it,
                this,
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
                toString: String
            ) {
                progressDialog.show()
                var data = HashMap<String, Any>()
                data["Pincode"] = Pincode
                data["Service_Code"] = Service_Code
                data["Service_Date"] = AppUtils2.formatDateTimeApi(Service_Date)
                data["Service_Subscription"] = ""
                data["Unit"] = unit.toString()
                data["Lat"] = Lat
                data["Long"] = Long
                data["ServiceType"] = ServiceType
                viewModel.GetSlots(data)
                viewModel.getSlotresponse.observe(this@SlotComplinceActivity, Observer {
//                    Log.d(TAG, "onViewCreated: $it orders fragment")
                    ShowBookingDialog(it)

                })

            }


        })




        viewModel.errorMessage.observe(this, Observer {
//            Log.d(TAG, "onViewCreated: $it")
            binding.recyclerView.visibility = View.GONE
//            binding.txterrormessage.visibility = View.VISIBLE
        })
        Log.e(
            "TAG",
            "Data11: " + ServiceCenter_Id + ", " + SlotDate + ", " + TaskId + ", " + SkillId + Lat + ", " + Long + ", " + ServiceType
        )


    }

    private fun ShowBookingDialog(slotData: Data) {
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.reschedule_layout, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        val btnSubmit = promptsView.findViewById<View>(R.id.btnSubmit) as Button
        val recycleWeeks: RecyclerView =
            promptsView.findViewById<View>(R.id.recycleView) as RecyclerView
        val recycleSlots: RecyclerView =
            promptsView.findViewById<View>(R.id.recycleSlots) as RecyclerView
        recycleWeeks.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleWeeks.layoutManager = layoutManager
        recycleSlots.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recycleSlots.layoutManager = layoutManager

        mSlotAdapter = SlotsAdapter(this, slotData.TimeSlots, TaskId)
        recycleSlots.adapter = mSlotAdapter

        btnSubmit.setOnClickListener {
            progressDialog.show()

            var data = HashMap<String, Any>()
            data["TaskId"] = TaskId
            data["AppointmentDate"] = AppointmentDate
            data["AppointmentStart"] = AppointmentStart
            data["AppointmentEnd"] =AppointmentEnd
            data["Source"] = Source
            data["ServiceType"] = ServiceType

            viewModel.bookSlotResponce.observe(this, Observer {
                if (it.IsSuccess) {
                    alertDialog.dismiss()
                }

            })

            viewModel.BookSlot(data)

            Toast.makeText(this, "Appointment Booked" , Toast.LENGTH_SHORT)
                .show()
            alertDialog.dismiss()
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
}