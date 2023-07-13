package com.ab.hicareservices.ui.view.activities

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
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.getslots.TimeSlot
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.ActivitySlotDetailsBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.handler.onSlotSelection
import com.ab.hicareservices.ui.viewmodel.GetSlotViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2

class SlotDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlotDetailsBinding
    private val viewModel: GetSlotViewModel by viewModels()
    private lateinit var mAdapter: SlotsAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype = ""
    lateinit var progressDialog: ProgressDialog
    var paymentdone = ""

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
        setContentView(R.layout.activity_my_order)
        binding = ActivitySlotDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobile = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        paymentdone = SharedPreferenceUtil.getData(this, "Paymentback", "-1").toString()
        Service_Date = intent.getStringExtra("Service_Date").toString()
        scheduledatetext = intent.getStringExtra("scheduledatetext").toString()
        TaskId = intent.getStringExtra("TaskId").toString()

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
        mAdapter = SlotsAdapter(this@SlotDetailActivity, timeslotslist, TaskId)

        binding.recyclerView.adapter = mAdapter

        binding.btnSubmit.setOnClickListener {
            progressDialog.show()

            var data = HashMap<String, Any>()
            data["TaskId"] = TaskId
            data["AppointmentDate"] = Service_Date
            data["AppointmentStart"] = AppointmentStart
            data["AppointmentEnd"] = AppointmentEnd
            data["Source"] = "MobileApp"
            data["ServiceType"] = "Pest"
            viewModel.BookSlot(data)

            viewModel.bookSlotResponce.observe(this, Observer {
                Log.d("TAG", "onViewCreated: $it orders fragment")
//                ShowBookingDialog(it)
                if (it.IsSuccess == true) {
                    if (it.Data!!.IsSuccess == true) {

                        Toast.makeText(this, it.Data!!.ResponseMessage, Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()
                        finish()
                    } else {
                        Toast.makeText(this, it.Data!!.ResponseMessage, Toast.LENGTH_SHORT)
                            .show()
                        progressDialog.dismiss()

                        finish()
                    }

                } else {

                    progressDialog.dismiss()
                    finish()


                }

            })


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
        val intent = Intent(this@SlotDetailActivity, SlotComplinceActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
}