package com.ab.hicareservices.ui.view.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.databinding.ActivityComplaintsBinding
import com.ab.hicareservices.ui.adapter.ComplaintsAdapter
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel

class ComplaintsActivity : AppCompatActivity() {
    private val TAG = "ComplaintsActivity"

    lateinit var binding: ActivityComplaintsBinding
    private val viewModel: ComplaintsViewModel by viewModels()
    private lateinit var mAdapter: ComplaintsAdapter
    private val viewModeld: OtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAdapter = ComplaintsAdapter()

        viewModeld.validateAccount("9967994682")

        binding.backIv.setOnClickListener {
            finish()
        }

//        binding.addComplaintsBtn.setOnClickListener {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            startActivity(intent)
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            getAllComplaints()

        }, 1000)
    }


    private fun getAllComplaints() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)


        viewModel.complaintList.observe(this, Observer {
            Log.d(TAG, "onViewCreated: $it")
            if (it.isNotEmpty()){
                mAdapter.setComplaintsList(it)

            }

        })

        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.getAllComplaints("9967994682")

//        viewModel.getAllComplaints(SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString())
    }
}