package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    lateinit var binding: ActivityComplaintsBinding
    private val viewModel: ComplaintsViewModel by viewModels()
    private lateinit var mAdapter: ComplaintsAdapter
    private val viewModeld: OtpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        viewModeld.validateAccount(AppUtils2.mobileno)


        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)


        binding.imgLogo.setOnClickListener {
            finish()
        }

//        binding.addComplaintsBtn.setOnClickListener {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            startActivity(intent)
//        }

        progressDialog.show()
//        binding.progressBar.visibility= View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({

            getAllComplaints(progressDialog)
        }, 1000)

    }

    private fun getAllComplaints(progressDialog: ProgressDialog) {
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mAdapter = ComplaintsAdapter()


        viewModel.complaintList.observe(this, Observer {
            progressDialog.dismiss()
            Log.d(TAG, "onViewCreated: $it")
//            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext,"FAiles",Toast.LENGTH_SHORT).show()
//            mAdapter.setComplaintsList(it, imageList)

        })

        viewModel.errorMessage.observe(this, Observer {
            progressDialog.dismiss()
            Toast.makeText(applicationContext,"Something went wrong!",Toast.LENGTH_SHORT).show()
        })

        binding.recyclerView.adapter = mAdapter

        viewModel.getAllComplaints(AppUtils2.mobileno)
//        binding.progressBar.visibility= View.GONE

//        viewModel.getAllComplaints(SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString())
    }
}