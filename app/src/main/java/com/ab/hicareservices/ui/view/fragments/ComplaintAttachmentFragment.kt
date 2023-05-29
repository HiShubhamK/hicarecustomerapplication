package com.ab.hicareservices.ui.view.fragments

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
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityComplaintsAttachmentBinding
import com.ab.hicareservices.ui.adapter.ComplaintAttachmentAdapter
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel

class ComplaintAttachmentFragment() : Fragment() {
    private val TAG = "ComplaintsActivity"
    var mobileNo = ""
    private lateinit var imageList:ArrayList<String>




    lateinit var binding: ActivityComplaintsAttachmentBinding
    private val viewModel: ComplaintsViewModel by viewModels()
    private lateinit var mAdapter: ComplaintAttachmentAdapter
    private val viewModeld: OtpViewModel by viewModels()
    private var mobile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageList = it.getStringArrayList("attachmentlist") as ArrayList<String>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityComplaintsAttachmentBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(imageListnew: ArrayList<String>) =
            ComplaintAttachmentFragment().apply {
                arguments = Bundle().apply {
                    this.putStringArrayList("attachmentlist",imageListnew)
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

        binding.backIv.setOnClickListener {
            requireActivity().finish()
        }

//        binding.addComplaintsBtn.setOnClickListener {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            startActivity(intent)
//        }

        binding.progressBar.visibility= View.VISIBLE
            getAllComplaints()

    }


    private fun getAllComplaints() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        mAdapter = ComplaintAttachmentAdapter()

//        viewModel.attachments.observe(requireActivity(),{
//
//           if (it!=null){
//               imageList.addAll(it)
//           }
//        })
//        viewModel.attachments.observe(requireActivity(), Observer {
//            Log.d(TAG, "onViewCreated: $it")
//            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext,"FAiles",Toast.LENGTH_SHORT).show()
            mAdapter.setAttachment(imageList)
        Log.e("TAG", "Attachments: $imageList")
            Toast.makeText(requireContext(),"attacchmnt"+imageList,Toast.LENGTH_SHORT).show()

//        })



        binding.recyclerView.adapter = mAdapter

//        viewModel.getAllComplaints("9967994682")

        binding.progressBar.visibility= View.GONE

//        viewModel.getAllComplaints(SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString())
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}