package com.ab.hicareservices.ui.view.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentOrdersBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.view.activities.OrderDetailActivity
import com.ab.hicareservices.ui.view.activities.PaymentActivity
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import org.json.JSONObject

class OrdersFragment() : Fragment() {
    private val TAG = "OrdersFragment"
    lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype = ""
    lateinit var homeActivity: HomeActivity
    lateinit var orderactivityforadapter: FragmentActivity
    lateinit var options: JSONObject
    lateinit var progressDialog: ProgressDialog

    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { activityResult ->
            val result = activityResult.resultCode
            val data = activityResult.data
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrdersFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(
//                    R.id.container, HomeFragment.newInstance()
//                ).addToBackStack("OrdersFragment").commit()
//        }

//        val progressBar = ProgressBar(requireActivity())
//        //setting height and width of progressBar
//        progressBar.layoutParams = LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )

        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        getOrdersList(progressDialog)

//        getOrdersList2()
        Handler(Looper.getMainLooper()).postDelayed({
//            getOrdersList()
        }, 1000)

        binding.activetxt.setTextColor(Color.parseColor("#2bb77a"))

        binding.txtactive.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
//                binding.progressBar.visibility=View.VISIBLE
                progressDialog.show()
            }, 1000)

            ordertype = "Active"

            binding.activetxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
            getOrdersList(progressDialog)
        }

        binding.txtexpire.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
//                binding.progressBar.visibility=View.VISIBLE
                progressDialog.show()
            }, 1000)
            ordertype = "Expired"
            getOrdersList(progressDialog)
            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.expiretxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
        }

        binding.txtcancelled.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
//                binding.progressBar.visibility=View.VISIBLE
                progressDialog.show()
            }, 1000)

            ordertype = "Cancelled"
            getOrdersList(progressDialog)
            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
        }


        binding.txtall.setOnClickListener {
//            binding.progressBar13.visibility = View.VISIBLE

            progressDialog.show()

//            binding.progressBar.visibility = View.VISIBLE
            ordertype = "All"
            getOrdersList2(progressDialog)
            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.alltext.setTextColor(Color.parseColor("#2bb77a"))
            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
        }
    }

    private fun getOrdersList2(progressDialog: ProgressDialog) {

//        progressDialog.show()
        binding.recyclerView2.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        nAdapter = OrderMenuAdapter()

        binding.recyclerView2.adapter = nAdapter

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            if (it != null) {
                nAdapter.setOrdersList(it)
                binding.textnotfound.visibility = View.GONE
                progressDialog.dismiss()
            } else {
                progressDialog.dismiss()
                binding.recyclerView.visibility = View.GONE
                binding.recyclerView2.visibility = View.GONE
                binding.textnotfound.visibility = View.VISIBLE
            }

//            this.progressDialog.dismiss()
            //            binding.recyclerView2.visibility = View.VISIBLE
        })


        viewModel.responseMessage.observe(requireActivity(), Observer {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            binding.textnotfound.visibility = View.VISIBLE
            binding.textnotfound.text = it.toString()
            progressDialog.dismiss()
        })


        viewModel.errorMessage.observe(requireActivity(), Observer {
            if (it != null) {
                binding.textnotfound.visibility = View.VISIBLE
            }
        })
        if (mobile != "-1") {
            if (ordertype.equals("") && ordertype != null) {
                ordertype = "Active"
                viewModel.getCustomerOrdersByMobileNo(mobile, progressDialog)
            } else {
                viewModel.getCustomerOrdersByMobileNo(mobile, progressDialog)
            }
        }
    }


    private fun getOrdersList( progressDialog: ProgressDialog) {

        progressDialog.show()
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = OrdersAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            if (it != null) {
                mAdapter.setOrdersList(it, requireActivity())
                binding.recyclerView.visibility = View.VISIBLE
                progressDialog.dismiss()
            } else {
//                progressDialog.dismiss()
                binding.textnotfound.visibility = View.VISIBLE
            }
        })
        mAdapter.setOnOrderItemClicked(object : OnOrderClickedHandler {

            override fun onOrderItemClicked(
                position: Int,
                orderNo: String,
                serviceType: String,
                service_url_image: String,
                locationLatitudeS: Double?,
                locationLongitudeS: Double?,
                ServiceCenterId: String,
            ) {

                val intent = Intent(requireActivity(), OrderDetailActivity::class.java)
                intent.putExtra("orderNo", orderNo)
                intent.putExtra("serviceType", serviceType)
                intent.putExtra("service_url_image", service_url_image)
                intent.putExtra("locationLatitudeS", locationLatitudeS.toString())
                intent.putExtra("locationLongitudeS", locationLongitudeS.toString())
                intent.putExtra("ServiceCenterId", ServiceCenterId)

                startActivity(intent)

//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(
//                        R.id.container, OrderDetailsFragment.newInstance(
//                            orderNo,
//                            serviceType,
//                            service_url_image,
//                            locationLatitudeS,
//                            locationLongitudeS,
//                            ServiceCenterId
//                        )
//                    ).addToBackStack("OrdersFragment").commit()

            }

            override fun onOrderPaynowClicked(
                position: Int,
                orderNumberC: String,
                customerIdC: String,
                servicePlanNameC: String,
                orderValueWithTaxC: Double,
                serviceType: String,
                standardValueC: Double?
            ) {
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("ORDER_NO", orderNumberC)
                intent.putExtra("ACCOUNT_NO", customerIdC)
                intent.putExtra("SERVICETYPE_NO", servicePlanNameC)
                intent.putExtra("PAYMENT", orderValueWithTaxC)
                intent.putExtra("SERVICE_TYPE",serviceType)
                intent.putExtra("Standard_Value__c",standardValueC)
                activityResultLauncher.launch(intent)
            }
        })


        viewModel.responseMessage.observe(requireActivity(), Observer {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            binding.textnotfound.visibility = View.VISIBLE
            binding.textnotfound.text = it.toString()
            progressDialog.dismiss()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            binding.textnotfound.visibility = View.VISIBLE
            binding.textnotfound.text = it.toString()
            progressDialog.dismiss()
        })
//        binding.progressBar13.visibility = View.GONE
//        binding.progressBar.visibility = View.GONE
        if (mobile != "-1") {
            if (ordertype.equals("") && ordertype != null) {
                ordertype = "Active"
                viewModel.getCustomerOrdersByMobileNo(mobile, ordertype, progressDialog)
            } else {
                viewModel.getCustomerOrdersByMobileNo(mobile, ordertype, progressDialog)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}