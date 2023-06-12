package com.ab.hicareservices.ui.view.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityComplaintsBinding
import com.ab.hicareservices.ui.adapter.ComplaintsAdapter
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel

class ComplaintFragment() : Fragment() {
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
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityComplaintsBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ComplaintFragment().apply {
                arguments = Bundle().apply {
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

//        viewModeld.validateAccount(mobile)


        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)



        binding.imgLogo.setOnClickListener {
            requireActivity().finish()
        }

//        binding.addComplaintsBtn.setOnClickListener {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            startActivity(intent)
//        }
        imageList=ArrayList()

        progressDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            getAllComplaints(progressDialog)
        }, 1500)

    }


    private fun getAllComplaints(progressDialog: ProgressDialog) {
        try {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            mAdapter = ComplaintsAdapter()
//            imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg")

//            viewModel.attachments.observe(requireActivity()) {
//                if (it != null) {
//                    imageList.addAll(it)
//                }
//            }
            viewModel.complaintList.observe(requireActivity(), Observer {
                Log.d(TAG, "onViewCreated: $it")

//            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext,"FAiles",Toast.LENGTH_SHORT).show()
                mAdapter.setComplaintsList(it,imageList,requireActivity())

                progressDialog.dismiss()
            })

            viewModel.errorMessage.observe(requireActivity(), Observer {
                Toast.makeText(requireActivity(),"Something went wrong!",Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            })

            binding.recyclerView.adapter = mAdapter

//        viewModel.getAllComplaints("9967994682")
            if (mobile != "-1") {
                viewModel.getAllComplaints(mobile)
            }
            binding.progressBar.visibility= View.GONE

        }catch (e:Exception){
            e.printStackTrace()
        }

//        viewModel.getAllComplaints(SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString())
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}