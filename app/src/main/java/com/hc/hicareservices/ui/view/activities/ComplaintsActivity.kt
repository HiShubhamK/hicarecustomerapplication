package com.hc.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hc.hicareservices.data.SharedPreferenceUtil
import com.hc.hicareservices.databinding.ActivityComplaintsBinding
import com.hc.hicareservices.ui.adapter.ComplaintsAdapter
import com.hc.hicareservices.ui.viewmodel.ComplaintsViewModel

class ComplaintsActivity : AppCompatActivity() {
    private val TAG = "ComplaintsActivity"

    lateinit var binding: ActivityComplaintsBinding
    private val viewModel: ComplaintsViewModel by viewModels()
    private lateinit var mAdapter: ComplaintsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.addComplaintsBtn.setOnClickListener {
            val intent = Intent(this, AddComplaintsActivity::class.java)
            startActivity(intent)
        }

        getAllComplaints()
    }

    private fun getAllComplaints() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ComplaintsAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.complaintList.observe(this, Observer {
            Log.d(TAG, "onViewCreated: $it")
            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
            mAdapter.setComplaintsList(it)
        })

        viewModel.errorMessage.observe(this, Observer {

        })

        viewModel.getAllComplaints("9967994682")

//        viewModel.getAllComplaints(SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString())
    }
}