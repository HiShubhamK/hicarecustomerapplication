package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.CODOrders
import com.ab.hicareservices.data.model.dashboard.UpcomingService
import com.ab.hicareservices.databinding.ActivityUpcomingServicesBinding
import com.ab.hicareservices.databinding.LayoutBinding
import com.ab.hicareservices.ui.adapter.ComplaintsAdapter
import com.ab.hicareservices.ui.adapter.UpcomingServicesAdapter
import com.ab.hicareservices.ui.handler.onResceduleInterface
import com.ab.hicareservices.ui.viewmodel.ServiceViewModel
import com.ab.hicareservices.utils.AppUtils2

class UpcomingServicesActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpcomingServicesBinding
    private val viewModel: ServiceViewModel by viewModels()
    private lateinit var mAdapter: UpcomingServicesAdapter
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_services)
        binding= ActivityUpcomingServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog= ProgressDialog(this,R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
//        viewModel.ScheduledService.observe(this, Observer {
//            Log.d("TAG", "onViewCreated: $it")
////            mAdapter.setServiceList(it)
////            progressDialog.dismiss()
//        })
//        viewModel.getUpcomingServices(AppUtils2.mobileno)
        progressDialog.show()
        getAllComplaints(progressDialog)
        binding.imgLogo.setOnClickListener{
            onBackPressed()
        }
    }
    private fun getAllComplaints(progressDialog: ProgressDialog) {
        try {
            binding.recUpcomingData.layoutManager = LinearLayoutManager(this)
            mAdapter = UpcomingServicesAdapter()


            viewModel.responseMessage.observe(this, Observer {
                binding.recUpcomingData.visibility=View.GONE
                binding.upcomingservices.visibility=View.VISIBLE
            })

            viewModel.ScheduledService.observe(this, Observer {
                Log.d("TAG", "onViewCreated: $it")
                if(it!=null){
                mAdapter.setUpcomingService(it.UpcommingService)
                binding.recUpcomingData.visibility=View.VISIBLE
                binding.upcomingservices.visibility=View.GONE
                progressDialog.dismiss()
                }else{
                    binding.recUpcomingData.visibility=View.GONE
                    binding.upcomingservices.visibility=View.VISIBLE
                }
            })
            viewModel.errorMessage.observe(this, Observer {
                Toast.makeText(this,"Something went wrong!", Toast.LENGTH_SHORT).show()
            })

            binding.recUpcomingData.adapter = mAdapter
            mAdapter.setRescudullClick(object :onResceduleInterface{
                override fun onRecheduleClick(position: Int, upcomingdata: ArrayList<UpcomingService>) {
                    val intent = Intent(this@UpcomingServicesActivity, SlotComplinceActivity::class.java)
                    intent.putExtra("ServiceCenter_Id", upcomingdata[position].HRRegion_r!!.Id)
                    if (upcomingdata[position].AppointmentDate!=null){
                        intent.putExtra("SlotDate",upcomingdata[position].AppointmentDate)
                    }else{
                        intent.putExtra("SlotDate",upcomingdata[position].SRPlanDate)
                    }
                    intent.putExtra("TaskId", upcomingdata[position].Id)
                    intent.putExtra("SkillId", upcomingdata[position].TaskSkill_c)
                    intent.putExtra("Lat", upcomingdata[position].GoogleLat_c)
                    intent.putExtra("Long", upcomingdata[position].GoogleLong_c)
                    intent.putExtra("ServiceType", "Pest")
                    intent.putExtra("Pincode", upcomingdata[position].HRZipPostalCode_c)
                    intent.putExtra("SPCode", upcomingdata[position].OrderSPCode)
                    intent.putExtra("ServiceUnit", upcomingdata[position].OrderServiceArea_r!!.Unit_c)
                    intent.putExtra("Unit",upcomingdata[position].Unit)
                    startActivity(intent)
                }

                override fun onPaymentClick(position: Int, offers: ArrayList<CODOrders>) {
                    TODO("Not yet implemented")
                }

                override fun onPaymentitemsClick(position: Int, offers: ArrayList<CODOrders>) {
                    TODO("Not yet implemented")
                }

                override fun onToadaysClick(position: Int, offers: ArrayList<UpcomingService>) {
                    TODO("Not yet implemented")
                }

            })

//        viewModel.getAllComplaints("9967994682")
            if (AppUtils2.mobileno != "-1") {
                viewModel.getUpcomingServices(AppUtils2.mobileno)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}