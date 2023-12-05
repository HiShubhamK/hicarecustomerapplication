package com.ab.hicareservices.ui.view.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.getslots.TimeSlot
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.ActivityBookingSlotDetailBinding
import com.ab.hicareservices.databinding.ActivitySlotDetailsBinding
import com.ab.hicareservices.ui.adapter.BookingSlotsAdapter
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.handler.onSlotSelection
import com.ab.hicareservices.ui.viewmodel.GetSlotViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager
import com.ab.hicareservices.utils.UserData

class BookingSlotDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingSlotDetailBinding
    private val viewModel: GetSlotViewModel by viewModels()
    private lateinit var mAdapter: BookingSlotsAdapter
    private var mobile = ""
    private var ordertype = ""
    lateinit var progressDialog: ProgressDialog
    var paymentdone = ""
    private val viewProductModel: ProductViewModel by viewModels()

    private var Service_Date = ""
    private var scheduledatetext = ""
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
    private var ServiceCenter_Id = ""
    private var SkillId = ""
    var service = mutableListOf<ServiceData>()
    private val viewModels: OtpViewModel by viewModels()
    lateinit var mSlotAdapter: SlotsAdapter
    lateinit var alertDialog: AlertDialog

    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { activityResult ->
            val result = activityResult.resultCode
            val data = activityResult.data
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_slot_detail)
        binding = ActivityBookingSlotDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mobile = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        paymentdone = SharedPreferenceUtil.getData(this, "Paymentback", "-1").toString()
        Service_Date = intent.getStringExtra("Service_Date").toString()
        scheduledatetext = intent.getStringExtra("scheduledatetext").toString()
        ServiceCenter_Id = intent.getStringExtra("ServiceCenter_Id").toString()
        SkillId = intent.getStringExtra("SkillId").toString()
        TaskId = intent.getStringExtra("TaskId").toString()
        Lat = intent.getStringExtra("Lat").toString()
        Long = intent.getStringExtra("Long").toString()
        ServiceType = intent.getStringExtra("ServiceType").toString()

        binding.bottomheadertext.text =
            scheduledatetext + ", " + AppUtils2.formatDateTime4(Service_Date)

        var data = AppUtils2.timeslotslist
        getOrdersList(data, Service_Date, scheduledatetext)

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

//        getOrdersList2()
        Handler(Looper.getMainLooper()).postDelayed({
//            getOrdersList()
        }, 1000)


    }


    private fun getOrdersList(

        timeslotslist: ArrayList<TimeSlot>,
        Service_Date: String,
        scheduledatetext: String
    ) {

        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingSlotsAdapter(this@BookingSlotDetailActivity, timeslotslist, TaskId)

        binding.recyclerView.adapter = mAdapter

        binding.btnSubmit.setOnClickListener {
//            progressDialog.show()
//
//            var data = HashMap<String, Any>()
//            data["TaskId"] = TaskId
//            data["AppointmentDate"] = Service_Date
//            data["AppointmentStart"] = AppointmentStart
//            data["AppointmentEnd"] = AppointmentEnd
//            data["Source"] = "MobileApp"
//            data["ServiceType"] = "Pest"


            SharedPreferenceUtil.setData(this, "AppointmentStartDateTime", "")
            SharedPreferenceUtil.setData(this, "AppointmentEndDateTime", "")


            AppUtils2.AppointmentStart = AppointmentStart.toString()
            AppUtils2.AppointmentEnd = AppointmentEnd.toString()
            SharedPreferenceUtil.setData(
                this,
                "AppointmentStartDateTime",
                AppUtils2.formatDate(Service_Date) + " " + AppUtils2.AppointmentStart
            )
            SharedPreferenceUtil.setData(
                this,
                "AppointmentEndDateTime",
                AppUtils2.formatDate(Service_Date) + " " + AppUtils2.AppointmentEnd
            )

//            val userData = UserData()
//            userData.AppointmentStartDateTime = AppointmentStart.toString()
//            userData.AppointmentEndDateTime = AppointmentEnd.toString()
//            userData.OrderCreatedDatetime = Service_Date
//            SharedPreferencesManager(this).saveUserData(userData)

//            Log.d("Sharepreferncdata",SharedPreferencesManager(this).saveUserData(userData).toString())

            val intent = Intent(this@BookingSlotDetailActivity, BookingServiceCheckout::class.java)
            startActivity(intent)

        }

        mAdapter.setOnSlotSelection(object : onSlotSelection {
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

                TaskId = taskid
                AppointmentDate = appointmentDate
                AppointmentStart = appointmentStart
                AppointmentEnd = appointmentEnd!!
                Source = source!!

            }


        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        val intent = Intent(this@SlotDetailActivity, SlotComplinceActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(intent)
        val intent =
            Intent(this@BookingSlotDetailActivity, BookingSlotComplinceActivity::class.java)
        intent.putExtra("Service_Date", Service_Date)
        intent.putExtra("ServiceCenter_Id", "")
        intent.putExtra("scheduledatetext", scheduledatetext)
        intent.putExtra("TaskId", "")
        intent.putExtra("SkillId", "")
        intent.putExtra("Lat", Lat)
        intent.putExtra("Long", Long)
        intent.putExtra("ServiceType", ServiceType)
        startActivity(intent)
//        if(paymentdone.equals("true")){
//            SharedPreferenceUtil.setData(this@SlotDetailActivity, "Paymentback","")
//            val intent=Intent(this@SlotDetailActivity,HomeActivity::class.java)
//            startActivity(intent)
//        }else{
//            val intent=Intent(this@SlotDetailActivity,HomeActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun getClearchache() {
        viewProductModel.getClearCache(AppUtils2.mobileno)
    }
}
