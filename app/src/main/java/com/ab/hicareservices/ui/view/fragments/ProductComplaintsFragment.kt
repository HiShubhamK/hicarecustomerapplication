package com.ab.hicareservices.ui.view.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityProductComplaintsBinding
import com.ab.hicareservices.databinding.FragmentOrdersBinding
import com.ab.hicareservices.ui.adapter.ProductComplaintsAdapter
import com.ab.hicareservices.ui.view.fragments.AccountFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2

class ProductComplaintsFragment : Fragment() {
    private val TAG = "ProductComplaintsActivity"
    var mobileNo = ""
    private lateinit var imageList:ArrayList<String>
    lateinit var binding: ActivityProductComplaintsBinding
    private val viewModel: ComplaintsViewModel by viewModels()
    private lateinit var mAdapter: ProductComplaintsAdapter
    private val viewModeld: OtpViewModel by viewModels()
    private var mobile = ""
    lateinit var progressDialog: ProgressDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        AppUtils2.mobileno = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        progressDialog = ProgressDialog(requireContext(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)



        imageList=ArrayList()
        Handler(Looper.getMainLooper()).postDelayed({
            getAllComplaints()
        }, 500)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        arguments?.let {
//            isfromMenu = it.getBoolean("isfromMenu")

        }

    }

    private fun getAllComplaints() {
        try {
            progressDialog.show()

            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            mAdapter = ProductComplaintsAdapter(requireActivity())

            viewModel.procuctcomplaintList.observe(this, Observer {
                Log.d(TAG, "onViewCreated: $it")
                mAdapter.setComplaintsList(it,imageList,requireActivity())
                progressDialog.dismiss()
                binding.recyclerView.visibility=View.VISIBLE
                binding.txtnotfound.visibility=View.GONE
                progressDialog.dismiss()

            })

            viewModel.responseMessage.observe(this, Observer {
                binding.recyclerView.visibility=View.GONE
                binding.txtnotfound.visibility=View.VISIBLE
                binding.txtnotfound.text=it.toString()
                progressDialog.dismiss()
            })

            viewModel.errorMessage.observe(this, Observer {
                Toast.makeText(requireContext(),"Something went wrong!",Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            })

            binding.recyclerView.adapter = mAdapter

//        viewModel.getAllComplaints("9967994682")
            if (mobile != "-1") {
                viewModel.ProductComplaintListByUserId(9)
            }
            binding.progressBar.visibility= View.GONE

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            ProductComplaintsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityProductComplaintsBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }
}