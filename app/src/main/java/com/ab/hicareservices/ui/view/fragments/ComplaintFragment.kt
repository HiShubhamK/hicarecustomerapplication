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

        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.imgLogo.setOnClickListener {
            requireActivity().finish()
        }

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

            viewModel.complaintList.observe(requireActivity(), Observer {
                Log.d(TAG, "onViewCreated: $it")
                mAdapter.setComplaintsList(it,imageList,requireActivity())
                progressDialog.dismiss()
                binding.recyclerView.visibility=View.VISIBLE
                binding.txtnotfound.visibility=View.GONE
            })

            viewModel.responseMessage.observe(requireActivity(), Observer {
                binding.recyclerView.visibility=View.GONE
                binding.txtnotfound.visibility=View.VISIBLE
                binding.txtnotfound.text=it.toString()
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
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}