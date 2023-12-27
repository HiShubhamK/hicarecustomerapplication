package com.ab.hicareservices.ui.view.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.getslots.Data
import com.ab.hicareservices.data.model.getslots.TimeSlot
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.ActivitySlotComplinceBinding
import com.ab.hicareservices.ui.adapter.SlotCompliceAdapater
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.handler.onSlotSelection
import com.ab.hicareservices.ui.handler.onSlotclick
import com.ab.hicareservices.ui.viewmodel.GetSlotViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

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
    private var Latt = ""
    private var Longg = ""
    private var ServiceType = ""
    private var Pincode = ""
    private var Service_Code = ""
    private var Unit = ""
    private var spcode = ""
    private var AppointmentDate = ""
    private var AppointmentStart = ""
    private var AppointmentEnd = ""
    private var Source = ""
    private var selectedTimePostion = 0
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

        val intent = intent

        ServiceCenter_Id = intent.getStringExtra("ServiceCenter_Id").toString()
        SlotDate = intent.getStringExtra("SlotDate").toString()
        TaskId = intent.getStringExtra("TaskId").toString()
        SkillId = intent.getStringExtra("SkillId").toString()
        Latt = intent.getStringExtra("Lat").toString()
        Longg = intent.getStringExtra("Long").toString()
        ServiceType = intent.getStringExtra("ServiceType").toString()
        Pincode = intent.getStringExtra("Pincode").toString()
        Service_Code = intent.getStringExtra("Service_Code").toString()
        Unit = intent.getStringExtra("Unit").toString()
        spcode = intent.getStringExtra("SPCode").toString()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

            // Set default date to current date +1
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, 1) // Advance to the next day
            val defaultDate = calendar.timeInMillis
            binding.calendarView.date = defaultDate
        
        binding.calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener { CalendarView, year, month, dayOfMonth ->
            var date = ""
            var months = month + 1
            if (months < 10) {
                date = "$year-0$months-$dayOfMonth"
            } else {
                date = "$year-$months-$dayOfMonth"

            }
            val datenew = AppUtils2.formatDateTime5(date)
//            Log.e(TAG, "onSelectedDayChange: yyyy/mm/dd:$date")
//            val intent = Intent(this@CalendarActivity, MainActivity::class.java)
//            intent.putExtra("date", date)
//            startActivity(intent)
//            Handler(Looper.getMainLooper()).postDelayed({
//            val date2: Date = Calendar.getInstance().time
//            var strDate1=date2.toString()
//            val df = SimpleDateFormat("dd/MM/yyyy")
////            val formattedDate = df.format(date2)
////            val formattedDate=AppUtils2.formatDateTime5(strDate1)
//
//            val sdfnew = SimpleDateFormat("dd/MM/yyyy")
//            val strDate =sdfnew.parse(datenew)
//            val currentdate =sdfnew.parse(strDate1)
//

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            var c = Calendar.getInstance()
            var c1 = Calendar.getInstance()
            var c2 = Calendar.getInstance()
            try {
                c.time = sdf.parse(datenew)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            c2.add(
                Calendar.DAY_OF_MONTH,
                8
            )
            c.add(
                Calendar.DAY_OF_MONTH,
                1
            ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE c is selected date
            val sdf1 = SimpleDateFormat("yyyy-MM-dd")
            val sdfnew = SimpleDateFormat("dd-MM-yyyy")
            var output = sdf1.format(c.time)
            val output1 = AppUtils2.formatDatetime(AppUtils2.getCurrentDateTimeplusone())
            var output2 = sdfnew.format(c2.time)
//            output2=AppUtils2.formatDate(output2)
            if (c1.time > c.time ||c2.time < c.time  ) {
                binding.recyclerView.visibility = View.GONE
                binding.lnrAvailableSlot.visibility = View.GONE
//                Toast.makeText(
//                    this,
//                    "Please select a valid date in the range of "+output1 + "to "+output2+" to schedule a slot.",
//                    Toast.LENGTH_SHORT
//                ).show()
                DesignToast.makeText(this, "Please select a valid date in the range of " + output1 + " to " + AppUtils2.getCurrentDateTimeminusone(c2.time) + " to schedule a slot", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show()

            } else {
                binding.recyclerView.visibility = View.VISIBLE
//                binding.lnrAvailableSlot.visibility=View.VISIBLE

                getOrdersList(output.toString())
            }
//

        })
        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }
//        binding.ivCalender.setOnClickListener{
//            val c = Calendar.getInstance()
//            c.add(Calendar.DAY_OF_YEAR, 1)
//            var _pickedDate=""
//            val dialog = DatePickerDialog(
//                this!!,
//                { view, year, month, dayOfMonth ->
//                    val _year = year.toString()
//                    val _month = if (month + 1 < 10) "0" + (month + 1) else (month + 1).toString()
//                    val _date = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
//                    _pickedDate = "$_year-$_month-$_date"
//                    Log.e("PickedDate: ", "Date: $_pickedDate") //2019-02-12
//
//                    val sdf = SimpleDateFormat("yyyy-MM-dd")
//                    val c = Calendar.getInstance()
//                    try {
//                        c.time = sdf.parse(_pickedDate)
//                    } catch (e: ParseException) {
//                        e.printStackTrace()
//                    }
//                    c.add(
//                        Calendar.DATE,
//                        1
//                    ) // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
//                    val sdf1 = SimpleDateFormat("yyyy-MM-dd")
//                    val output = sdf1.format(c.time)
//
//                    getOrdersList(output.toString())
//
//                }, c[Calendar.YEAR], c[Calendar.MONTH], c[Calendar.MONTH]
//            )
//            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
//            dialog.show()
//        }
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
                spcode,
                Unit,
                Latt,
                Longg,
                ServiceType,

                )
            try {
                if (AppUtils2.formatDateTime4(it[0].ScheduledDate.toString()).isNotEmpty()) {
                    binding.tvSelectedDateFrom.text =
                        AppUtils2.formatDateTime4(it[0].ScheduledDate.toString())
                    binding.tvSelectedDateTo.text =
                        AppUtils2.formatDateTime4(it[7].ScheduledDate.toString())
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.lnrAvailableSlot.visibility = View.VISIBLE
                    progressDialog.dismiss()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        })
        Handler(Looper.getMainLooper()).postDelayed({
            var data = HashMap<String, Any>()
            data["ServiceCenter_Id"] = ServiceCenter_Id
            data["SlotDate"] = date
            data["TaskId"] = TaskId
            data["SkillId"] = SkillId
            data["Lat"] = Latt
            data["Long"] = Longg
            data["ServiceType"] = ServiceType

            viewModel.requestcodes.observe(this, Observer {

                SharedPreferenceUtil.setData(this, "mobileNo", "-1")
                SharedPreferenceUtil.setData(this, "bToken", "")
                SharedPreferenceUtil.setData(this, "IsLogin", false)
                SharedPreferenceUtil.setData(this, "pincode", "")
                SharedPreferenceUtil.setData(this, "customerid", "")
                SharedPreferenceUtil.setData(this, "FirstName", "")
                SharedPreferenceUtil.setData(this, "MobileNo", "")

                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()

            })



            viewModel.getComplainceData(data)
        }, 500)


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
                scheduledate: String,
                scheduledatetext: String
            ) {
                progressDialog.show()
                DesignToast.makeText(
                    this@SlotComplinceActivity,
                    "Please wait while we fetch slot for you. This might take upto 15 seconds.",
                    Toast.LENGTH_SHORT,
                    DesignToast.TYPE_SUCCESS
                ).show()
//                Toast.makeText(
//                    this@SlotComplinceActivity,
//                    "Please wait while we fetch slot for you. This might take upto 30 seconds.",
//                    Toast.LENGTH_LONG
//                ).show()
                AppUtils2.ServiceDate = AppUtils2.formatDateTime(scheduledate)
                var data = HashMap<String, Any>()
                data["Pincode"] = Pincode
                data["Service_Code"] = Service_Code
                data["Service_Date"] = AppUtils2.formatDateTime(scheduledate)
                data["Service_Subscription"] = Service_Subscription.toString()
                data["Unit"] = unit.toString()
                data["Lat"] = Lat
                data["Long"] = Long
                data["ServiceType"] = ServiceType
                viewModel.GetSlots(data)
                viewModel.getSlotresponse.observe(this@SlotComplinceActivity, Observer {
//                    Log.d(TAG, "onViewCreated: $it orders fragment")
                    if (it.IsSuccess == true) {
                        progressDialog.dismiss()
                        AppUtils2.timeslotslist = it.TimeSlots
                        val intent =
                            Intent(this@SlotComplinceActivity, SlotDetailActivity::class.java)
                        intent.putExtra("Service_Date", AppUtils2.ServiceDate)
                        intent.putExtra("SkillId", SkillId)
                        intent.putExtra("ServiceCenter_Id", ServiceCenter_Id)
                        intent.putExtra("scheduledatetext", scheduledatetext)
                        intent.putExtra("TaskId", TaskId)
                        intent.putExtra("Lat", Latt)
                        intent.putExtra("Long", Longg)
                        intent.putExtra("ServiceType", ServiceType)
                        startActivity(intent)
                        finish()
//                        ShowBookingDialog(it, Service_Date, scheduledatetext, progressDialog)
                    }
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
            "Data11: " + ServiceCenter_Id + ", " + SlotDate + ", " + TaskId + ", " + SkillId + Latt + ", " + Longg + ", " + ServiceType + "" + Unit
        )


    }

    @SuppressLint("MissingInflatedId")
    private fun ShowBookingDialog(
        slotData: Data,
        Servicedate: String,
        scheduledatetext: String,
        progressDialog: ProgressDialog
    ) {
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.reschedule_layout, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()

//        alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        val btnSubmit = promptsView.findViewById<View>(R.id.btnSubmit) as Button
        val txtMonth = promptsView.findViewById<View>(R.id.txtMonth) as TextView
        val imgClose = promptsView.findViewById<View>(R.id.imgClose) as ImageView
        txtMonth.text = scheduledatetext
//        val recycleWeeks: RecyclerView =
//            promptsView.findViewById<View>(R.id.recycleView) as RecyclerView
        val recycleSlots: RecyclerView =
            promptsView.findViewById<View>(R.id.recycleSlots) as RecyclerView
//        recycleWeeks.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recycleWeeks.layoutManager = layoutManager
        recycleSlots.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recycleSlots.layoutManager = layoutManager

        mSlotAdapter = SlotsAdapter(this, slotData.TimeSlots, TaskId)
        recycleSlots.adapter = mSlotAdapter

        imgClose.setOnClickListener {
            alertDialog.dismiss()
            progressDialog.dismiss()

        }
        btnSubmit.setOnClickListener {
            progressDialog.show()

            var data = HashMap<String, Any>()
            data["TaskId"] = TaskId
            data["AppointmentDate"] = AppointmentDate
            data["AppointmentStart"] = AppointmentStart
            data["AppointmentEnd"] = AppointmentEnd
            data["Source"] = Source
            data["ServiceType"] = ServiceType

            viewModel.requestcodes.observe(this, Observer {

                    SharedPreferenceUtil.setData(this, "mobileNo", "-1")
                    SharedPreferenceUtil.setData(this, "bToken", "")
                    SharedPreferenceUtil.setData(this, "IsLogin", false)
                    SharedPreferenceUtil.setData(this, "pincode", "")
                    SharedPreferenceUtil.setData(this, "customerid", "")
                    SharedPreferenceUtil.setData(this, "FirstName", "")
                    SharedPreferenceUtil.setData(this, "MobileNo", "")

                    val intent= Intent(this@SlotComplinceActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()

            })

            viewModel.bookSlotResponce.observe(this, Observer {
                if (it.IsSuccess == true) {
                    alertDialog.dismiss()
                    Toast.makeText(this, it.Data!!.ResponseMessage, Toast.LENGTH_SHORT)
                        .show()
                    progressDialog.dismiss()
                    AppUtils2.eventCall(this,"Slot Booked : "+data)


                } else {
                    Toast.makeText(this, it.Data!!.ResponseMessage, Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()
                    progressDialog.dismiss()


                }

            })

            if (AppointmentStart!=""){
                viewModel.BookSlot(data)

            }else{
                DesignToast.makeText(
                    this@SlotComplinceActivity,
                    "Please select slot time to book a slot!",
                    Toast.LENGTH_SHORT,
                    DesignToast.TYPE_ERROR
                ).show()
//                Toast.makeText(this, "Please select slot time to book a slot!", Toast.LENGTH_SHORT).show()
            }


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
                serviceType: String,
                toString: String
            ) {


//                selectedTimePostion=position
                TaskId = taskid
                AppointmentDate = appointmentDate
                AppointmentStart = appointmentStart
                AppointmentEnd = appointmentEnd!!
                Source = source!!
            }
        })
        alertDialog.show()

    }

    override fun onResume() {
        super.onResume()
        getOrdersList(AppUtils2.getCurrentDateTimeplusone())

    }

}


private fun Bundle.putParcelableArrayList(s: String, timeSlots: ArrayList<TimeSlot>) {

}
