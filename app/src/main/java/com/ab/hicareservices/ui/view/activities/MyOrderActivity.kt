package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityMyOrderBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel

class MyOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrderBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype = ""
    lateinit var progressDialog: ProgressDialog
    var paymentdone=""

    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { activityResult ->
            val result = activityResult.resultCode
            val data = activityResult.data
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)
        binding = ActivityMyOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobile = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

         paymentdone = SharedPreferenceUtil.getData(this, "Paymentback", "-1").toString()

        getOrdersList(progressDialog, "No Active Orders")

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

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
            getOrdersList(progressDialog,"No Active Orders")
        }

        binding.txtexpire.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
//                binding.progressBar.visibility=View.VISIBLE
                progressDialog.show()
            }, 1000)
            ordertype = "Expired"
            getOrdersList(progressDialog, "No Expire Orders")
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
            getOrdersList(progressDialog, "No Cancelled Orders")
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

        progressDialog.show()
        binding.recyclerView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        nAdapter = OrderMenuAdapter()

        binding.recyclerView2.adapter = nAdapter

        viewModel.ordersList.observe(this, Observer {
//            Log.d(TAG, "onViewCreated: $it orders fragment")
            var data=it
            if (it != null||data.isNotEmpty()) {
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


        viewModel.responseMessage.observe(this, Observer {
//            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            binding.textnotfound.visibility = View.VISIBLE
            binding.textnotfound.text = it.toString()
            progressDialog.dismiss()
        })


        viewModel.errorMessage.observe(this, Observer {
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


    private fun getOrdersList(progressDialog: ProgressDialog, s: String) {

        progressDialog.show()
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = OrdersAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.ordersList.observe(this, Observer {
//            Log.d(TAG, "onViewCreated: $it orders fragment")

            if(it!=null) {

                if (it.isNotEmpty()) {
                    binding.textnotfound.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    mAdapter.setOrdersList(it, this)
                    progressDialog.dismiss()
                } else {
                    progressDialog.dismiss()
                    binding.recyclerView.visibility = View.GONE
                    binding.textnotfound.visibility = View.VISIBLE
                    binding.textnotfound.text=s
                }
            }else{
                progressDialog.dismiss()
                binding.recyclerView.visibility = View.GONE
                binding.textnotfound.visibility = View.VISIBLE
                binding.textnotfound.text=s
            }
            progressDialog.dismiss()

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

                val intent = Intent(this@MyOrderActivity, OrderDetailActivity::class.java)
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
                val intent = Intent(this@MyOrderActivity, PaymentActivity::class.java)
                intent.putExtra("ORDER_NO", orderNumberC)
                intent.putExtra("ACCOUNT_NO", customerIdC)
                intent.putExtra("SERVICETYPE_NO", servicePlanNameC)
                intent.putExtra("PAYMENT", orderValueWithTaxC)
                intent.putExtra("SERVICE_TYPE",serviceType)
                intent.putExtra("Standard_Value__c",standardValueC)
                intent.putExtra("Product", false)
                activityResultLauncher.launch(intent)
            }
        })


        viewModel.responseMessage.observe(this, Observer {
//            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            binding.textnotfound.visibility = View.VISIBLE
            binding.textnotfound.text = it.toString()
            progressDialog.dismiss()
        })

        viewModel.errorMessage.observe(this, Observer {
//            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            binding.textnotfound.visibility = View.VISIBLE
            binding.textnotfound.text = it.toString()
            progressDialog.dismiss()
        })

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

    override fun onBackPressed() {
        super.onBackPressed()
        if(paymentdone.equals("true")){
            SharedPreferenceUtil.setData(this@MyOrderActivity, "Paymentback","")
            val intent=Intent(this@MyOrderActivity,HomeActivity::class.java)
            startActivity(intent)
        }else{
            val intent=Intent(this@MyOrderActivity,HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
