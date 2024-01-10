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
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
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
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import org.json.JSONObject

class OrdersFragment() : Fragment() {
    private val TAG = "OrdersFragment"
    lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype = ""
    private var isfromMenu = false
    lateinit var homeActivity: HomeActivity
    lateinit var options: JSONObject
    lateinit var progressDialog: ProgressDialog
    private val viewProductModel: ProductViewModel by viewModels()


    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { activityResult ->
            val result = activityResult.resultCode
            val data = activityResult.data
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isfromMenu = it.getBoolean("isfromMenu")
        }
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
        fun newInstance(isfromMenu: Boolean) =
            OrdersFragment().apply {
                arguments = Bundle().apply {
                    this.putBoolean("isfromMenu", isfromMenu)

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        progressDialog.show()

        Handler(Looper.getMainLooper()).postDelayed({

            getOrdersList(progressDialog,"No Active Orders")

        }, 500)

        if (isfromMenu){
            binding.headerView.visibility=View.VISIBLE

        }else {
            binding.headerView.visibility=View.GONE

        }

        binding.activetxt.setTextColor(Color.parseColor("#2bb77a"))

        binding.txtactive.setOnClickListener {

            ordertype = "Active"

            binding.activetxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.expiretxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
            getOrdersList(progressDialog, "No Active Orders")
        }

        binding.txtexpire.setOnClickListener {
            ordertype = "Expired"
            getOrdersList(progressDialog, "No Expired Orders")
            binding.activetxt.setTextColor(Color.parseColor("#5A5A5A"))
            binding.expiretxt.setTextColor(Color.parseColor("#2bb77a"))
            binding.alltext.setTextColor(Color.parseColor("#5A5A5A"))
            binding.cancelledtxt.setTextColor(Color.parseColor("#5A5A5A"))
        }

        binding.txtcancelled.setOnClickListener {

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
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        nAdapter = OrderMenuAdapter()

        binding.recyclerView2.adapter = nAdapter


        viewModel.responseMessage.observe(requireActivity(), Observer {
            if(it.equals("Success")){
                binding.textnotfound.visibility = View.GONE
            }else if(it.equals("No Orders")) {
                binding.textnotfound.visibility = View.VISIBLE
                binding.textnotfound.text = it.toString()
                progressDialog.dismiss()
            }
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            if (it != null) {
                binding.textnotfound.visibility = View.VISIBLE
            }
        })

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            var data=it
            if (it.isNotEmpty()) {
                nAdapter.setOrdersList(it)
                binding.textnotfound.visibility = View.GONE
                progressDialog.dismiss()
            } else {
                progressDialog.dismiss()
                binding.recyclerView.visibility = View.GONE
                binding.recyclerView2.visibility = View.GONE
                binding.textnotfound.visibility = View.VISIBLE
            }
        })

        if (mobile != "-1") {
            if (ordertype != null&& ordertype == "") {
                ordertype = "All"
                viewModel.getCustomerOrdersByMobileNo(mobile, progressDialog,requireActivity())
            } else {
                viewModel.getCustomerOrdersByMobileNo(mobile, progressDialog,requireActivity())
            }
        }
        progressDialog.dismiss()
    }


    private fun getOrdersList(progressDialog: ProgressDialog, s: String) {
        binding.textnotfound.visibility = View.GONE
        progressDialog.show()

        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = OrdersAdapter()

        binding.recyclerView.adapter = mAdapter

        viewModel.ordersList.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")

            if(it!=null) {

                if (it.isNotEmpty()) {
                    binding.textnotfound.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    mAdapter.setOrdersList(it, requireActivity())
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

        viewModel.responseMessage.observe(requireActivity(), Observer {
            if(it.equals("Success")){
                binding.textnotfound.visibility = View.GONE
            }else if(it.equals("No Orders")) {
                binding.recyclerView.visibility = View.GONE
                binding.textnotfound.visibility = View.VISIBLE
                binding.textnotfound.text =s
                progressDialog.dismiss()
            }
        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
//            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            binding.recyclerView.visibility = View.GONE
            binding.textnotfound.visibility = View.VISIBLE
            binding.textnotfound.text=s
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

                val intent = Intent(requireActivity(), OrderDetailActivity::class.java)

                SharedPreferenceUtil.setData(requireActivity(), "Orderno", "")
                SharedPreferenceUtil.setData(requireActivity(), "serviceType", "")
                SharedPreferenceUtil.setData(requireActivity(), "Orderno", orderNo)
                SharedPreferenceUtil.setData(requireActivity(), "serviceType", serviceType)
                AppUtils2.ordernumbers=orderNo
                AppUtils2.serviceType=serviceType
                intent.putExtra("orderNo", orderNo)
                intent.putExtra("serviceType", serviceType)
                intent.putExtra("service_url_image", service_url_image)
                intent.putExtra("locationLatitudeS", locationLatitudeS.toString())
                intent.putExtra("locationLongitudeS", locationLongitudeS.toString())
                intent.putExtra("ServiceCenterId", ServiceCenterId)

                startActivity(intent)

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
                progressDialog.show()
                viewProductModel.razorpayOrderIdResponse.observe(requireActivity(), Observer {

                    if (it.IsSuccess == true) {
                        progressDialog.dismiss()
                        AppUtils2.razorpayorderid = it.Data.toString()


                        Handler(Looper.getMainLooper()).postDelayed({

                            AppUtils2.paynowamount=orderValueWithTaxC.toString()

                            val intent = Intent(requireContext(), PaymentActivity::class.java)
                            intent.putExtra("ORDER_NO", orderNumberC)
                            intent.putExtra("ACCOUNT_NO", customerIdC)
                            intent.putExtra("SERVICETYPE_NO", servicePlanNameC)
                            intent.putExtra("PAYMENT", orderValueWithTaxC)
                            intent.putExtra("SERVICE_TYPE", serviceType)
                            intent.putExtra("Standard_Value__c", standardValueC)
                            intent.putExtra("Product", false)

                            activityResultLauncher.launch(intent)

                        },300)
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }
                })

                viewProductModel.CreateRazorpayOrderId(orderValueWithTaxC.toDouble(), 12342)
                progressDialog.dismiss()
            }

            override fun onNotifyMeclick(position: Int, orderNumberC: String, customerIdC: String) {
            }
        })
        if (mobile != "-1") {
            if (ordertype.equals("") && ordertype != null) {
                ordertype = "Active"
                viewModel.getCustomerOrdersByMobileNo(mobile, ordertype, progressDialog,requireActivity())
            } else {
                viewModel.getCustomerOrdersByMobileNo(mobile, ordertype, progressDialog,requireActivity())
            }
        }

        progressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}