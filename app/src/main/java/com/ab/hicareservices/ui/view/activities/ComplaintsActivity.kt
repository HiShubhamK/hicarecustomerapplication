package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityComplaintsBinding
import com.ab.hicareservices.ui.adapter.ComplaintsAdapter
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2

class ComplaintsActivity : AppCompatActivity() {
    private val TAG = "ComplaintsActivity"
    var mobileNo = ""
    private lateinit var imageList:ArrayList<String>
    lateinit var binding: ActivityComplaintsBinding
    private val viewModel: ComplaintsViewModel by viewModels()
    private lateinit var mAdapter: ComplaintsAdapter
    private val viewModeld: OtpViewModel by viewModels()
    private var mobile = ""
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

        imageList=ArrayList()
        Handler(Looper.getMainLooper()).postDelayed({
            getAllComplaints(progressDialog)
        }, 1500)
    }

    private fun getAllComplaints(progressDialog: ProgressDialog) {
        try {
            progressDialog.show()

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            mAdapter = ComplaintsAdapter(this)

            viewModel.complaintList.observe(this, Observer {
                Log.d(TAG, "onViewCreated: $it")
                mAdapter.setComplaintsList(it,imageList,this)
                progressDialog.dismiss()
                binding.recyclerView.visibility=View.VISIBLE
                binding.txtnotfound.visibility=View.GONE
            })

            viewModel.responseMessage.observe(this, Observer {
                binding.recyclerView.visibility=View.GONE
                binding.txtnotfound.visibility=View.VISIBLE
                binding.txtnotfound.text=it.toString()
                progressDialog.dismiss()
            })

            viewModel.errorMessage.observe(this, Observer {
                Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            })

            binding.recyclerView.adapter = mAdapter

//        viewModel.getAllComplaints("9967994682")
            if (mobile != "-1") {
                viewModel.getAllComplaints(AppUtils2.mobileno)
            }
            binding.progressBar.visibility= View.GONE

            progressDialog.dismiss()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}