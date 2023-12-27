package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.ActivityOrderDetailBinding
import com.ab.hicareservices.ui.adapter.ServiceRequestAdapter
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.adapter.WeeksAdapter
import com.ab.hicareservices.ui.handler.OnServiceRequestClickHandler
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailBinding
    private val viewModel: ServiceViewModel by viewModels()
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    private lateinit var mAdapter: ServiceRequestAdapter
    lateinit var mWeeksAdapter: WeeksAdapter
    lateinit var mSlotAdapter: SlotsAdapter
    private val taskId = ""
    private var date = ""
    var orderNo = ""
    var serviceType = ""
    private val ORDER_NO = "ORDER_NO"
    private val SERVICE_TYPE = "SERVICE_TYPE"
    private val SERVICE_TYPE_IMG = "SERVICE_TYPE_IMG"
    lateinit var options: JSONObject
    private val viewModels: OtpViewModel by viewModels()
    var service_url_image: String = ""
    var accountId = ""
    var service = ""
    var orderValueWithTax = 00.00
    var discount = ""
    var orderValueWithTaxAfterDiscount = ""
    var locationLatitudeS: String = ""
    var locationLongitudeS: String = ""
    var ServiceCenterId: String = ""
    var stdvalue = ""
    var service_SP_Code__c=""
    var unit=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { activityResult ->
                val result = activityResult.resultCode
                val data = activityResult.data
            })

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)


        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }


        val intent = intent
        orderNo = intent.getStringExtra("orderNo").toString()
        serviceType = intent.getStringExtra("serviceType").toString()
        service_url_image = intent.getStringExtra("service_url_image").toString()
        locationLatitudeS = intent.getStringExtra("locationLatitudeS").toString()
        locationLongitudeS = intent.getStringExtra("locationLongitudeS").toString()
        ServiceCenterId = intent.getStringExtra("ServiceCenterId").toString()
        stdvalue = intent.getStringExtra("Standard_Value__c").toString()

        Picasso.get().load(service_url_image).into(binding.imgType)
        getServiceDetails(orderNo, serviceType)

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

        if (orderNo != null) {
            binding.bottomheadertext.visibility = View.VISIBLE
            binding.bottomheadertext.text = orderNo
        } else {
            binding.bottomheadertext.visibility = View.GONE
        }

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.show()
            getServiceLists(progressDialog)
        }, 500)

        binding.help.setOnClickListener {

            val intent = Intent(this, AddComplaintsActivity::class.java)
            intent.putExtra("complaint",false)
            intent.putExtra("orderNo", orderNo)
            intent.putExtra("serviceType", serviceType)
            intent.putExtra("service_url_image", service_url_image)

            startActivity(intent)
        }

        try {

            binding.payNowBtn.setOnClickListener {
                try {

                    AppUtils2.Checkpayment=""
                    AppUtils2.Checkpayment="orderdeatils"
                    val intent = Intent(this, PaymentActivity::class.java)
                    intent.putExtra("ORDER_NO", orderNo)
                    intent.putExtra("ACCOUNT_NO", accountId)
                    intent.putExtra("SERVICETYPE_NO", service)
                    intent.putExtra("SERVICE_TYPE", serviceType)
                    intent.putExtra("PAYMENT", orderValueWithTax)
                    intent.putExtra("Standard_Value__c", stdvalue)
                    intent.putExtra("Product", false)
                    activityResultLauncher.launch(intent)

//                    val co = Checkout()
//                    co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
//                    co.open(requireActivity(), options)
                } catch (e: Exception) {
                    Log.d("TAG", "$e")
                }
            }
        } catch (e: Exception) {

        }

    }

    private fun getServiceLists(progressDialog: ProgressDialog) {

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        mAdapter = ServiceRequestAdapter()

        binding.recycleView.adapter = mAdapter

        viewModel.serviceList.observe(this, androidx.lifecycle.Observer {
            progressDialog.dismiss()
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "onViewCreated: $it")
            if(it!=null){
            mAdapter.setServiceList(it)
            progressDialog.dismiss()
            }else{
                progressDialog.dismiss()
            }
        })

        mAdapter.setOnServiceItemClicked(object : OnServiceRequestClickHandler {

            override fun onViewServiceClicked(position: Int) {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MyServiceDetailsFragment.newInstance())
//                    .addToBackStack("OrderDetailsFragment").commit()
                val intent = Intent(this@OrderDetailActivity, MyServiceDetailsActivity::class.java)
                startActivity(intent)
            }

            override fun onRescheduleServiceClicked(position: Int, service: ServiceData) {

                val intent = Intent(this@OrderDetailActivity, SlotComplinceActivity::class.java)
                intent.putExtra("ServiceCenter_Id", ServiceCenterId)
                intent.putExtra("SlotDate", getCurrentDate())
                intent.putExtra("TaskId", service.ParentTaskId.toString())
                intent.putExtra("SkillId", service.Parent_Task_Skill_Id)
                intent.putExtra("Lat", locationLatitudeS)
                intent.putExtra("Long", locationLongitudeS)
                intent.putExtra("ServiceType", serviceType)
                intent.putExtra("Pincode", service.Pincode)
                intent.putExtra("SPCode", service_SP_Code__c)
                intent.putExtra("ServiceUnit", service.ServiceUnit)
                intent.putExtra("Unit",unit)

                startActivity(intent)

//                supportFragmentManager.beginTransaction()
//                    .replace(
//                        R.id.container,
//                        SlotComplinceFragment.newInstance(
//                            ServiceCenterId,
//                            getCurrentDate(),
//                            service.ParentTaskId.toString(),
//                            service.Parent_Task_Skill_Id.toString(),
//                            locationLatitudeS,
//                            locationLongitudeS,
//                            serviceType,
//                            service.Pincode,
//                            service.SPCode,
//                            service.ServiceUnit
//
//                        )
//                    ).addToBackStack("SlotComplinceFragment").commit()
            }
        })

        viewModel.errorMessage.observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "onViewCreated: $it")
            binding.recycleView.visibility = View.GONE
            binding.txterrormessage.visibility = View.VISIBLE
        })

        viewModel.getServiceRequest(orderNo, serviceType,this@OrderDetailActivity)

    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        return sdf.format(Date())
    }

    private fun getServiceDetails(orderNo: String, serviceType: String) {
        try {
            progressDialog.dismiss()
            orderDetailsViewModel.orderDetailsData.observe(this) {
                if (it != null) {
                    val data = it[0]
                    accountId = data.account_Name__r?.customer_id__c.toString()
                    service = data.service_Plan_Name__c.toString()
                    orderValueWithTax = data.order_Value_with_Tax__c.toString().toDouble()
                    discount = (orderValueWithTax.toString().toDouble() * 0.05).toString()
//                 orderValueWithTaxAfterDiscount =
//                     (data.order_Value_with_Tax__c.toString().toDouble() - discount)..toString()
                    binding.orderNameTv.text = data.service_Plan_Name__c
                    binding.orderNoTv.text = ": " + orderNo

                    if (data.appointmentStartDateTime__c!=null){
                        binding.dateTv.text = ": " + AppUtils2.formatDateTime4(data!!.createdDate.toString())
                    }else{
                        binding.dateTv.visibility=View.GONE
                    }

//                    if (data?.appointmentStartDateTime__c.toString()!=null){
////                        binding.dateTv.text =
////                            ": " + AppUtils2.formatDateTime4(data?.appointmentStartDateTime__c.toString())
//                    }

                    binding.txtaddress.text = data.account_Name__r?.accountAddress ?: "N/A"
//                binding.orderDateTv.text  = data.createdDateText
//                binding.statusTv.text = data.status__c
                    binding.apartmentSizeTv.text = "Selected Apartment Size - ${data.unit1__c}"
                    binding.quantityTv.text = "QTY: ${data.quantity__c}"
//                binding.paymentStatusTv.text = if (data.enable_Payment_Link == false) "Paid" else "Unpaid"
                    binding.totalTv.text = "₹ ${data.standard_Value__c}"
                    binding.priceTv.text = "₹ ${Math.round(data.standard_Value__c!!.toDouble())}"
                    binding.discountTv.text =
                        if (data.orderDiscountValue != null) "- ₹ ${Math.round(data.orderDiscountValue.toDouble())}" else "- ₹ 0"
                    binding.totalAmountTv.text =
                        "₹ ${Math.round(data.order_Value_with_Tax__c!!.toDouble())}"
//                binding.completionDateTv.text = data.end_Date__c ?: "N/A"
                    binding.contactDetailsTv.text =
                        "${data.account_Name__r?.name} | ${data.account_Name__r?.mobile__c}"
                    binding.addressTv.text = data.account_Name__r?.accountAddress ?: "N/A"
                    binding.textaddress.text = data.account_Name__r?.accountAddress ?: "N/A"
                    binding.textserviceperiod.text = data.service_Period
                    binding.textdatestart.text =
                        AppUtils2.formatDateTime4(data.start_Date__c.toString())
                    binding.textdateend.text = AppUtils2.formatDateTime4(data.end_Date__c.toString())
                    service_SP_Code__c=data.service_SP_Code__c.toString()
                    unit=data.unit1__c.toString()
//                val notes = prepareNotes(
//                    accountId,
//                    orderNo,
//                    service,
//                    serviceType,
//                    orderValueWithTax?.toDouble(),
//                    orderValueWithTaxAfterDiscount
//                )
//                options = prepareOption(
//                    notes,
//                    data.service_Plan_Name__c.toString(),
//                    orderValueWithTaxAfterDiscount.toString()
//                )
                    if (data.enable_Payment_Link == true) {
                        binding.payNowBtn.visibility = View.VISIBLE
                        binding.txtamountpaidornot.text = "Amount To be Paid"
                    } else {
                        binding.payNowBtn.visibility = View.GONE
                        binding.txtamountpaidornot.text = "Amount Paid"
                    }

                    binding.statusTv.text =" "+data.status__c+" "

//
//                if(data.status__c.equals("Active")){
//                    binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
//                }else {
//                    binding.statusTv.text = data.status__c
//                    binding.statusTv.setTextColor(Color.parseColor("#B71C1C"))
//                }
//

                    if (data.status__c.equals("Expired")) {

                        if (data.enable_Payment_Link == false) {
                            binding.paymentStatusTv.text = "Paid"
                        } else {
                            binding.paymentStatusTv.text = "Unpaid"
                        }
                        binding.statusTv.setTextColor(Color.parseColor("#D50000"))

                    } else if (data.status__c.equals("Short Close")) {

                        if (data.enable_Payment_Link == false) {
                            binding.paymentStatusTv.text = "Paid"
                        } else {
                            binding.paymentStatusTv.text = "Unpaid"
                        }
                        binding.statusTv.setTextColor(Color.parseColor("#FB8C00"))

                    } else if (data.status__c.equals("Cancelled")) {
                        binding.lnrStatus.visibility= View.GONE
                        binding.payNowBtn.visibility = View.GONE
                        binding.statusTv.setTextColor(Color.parseColor("#ff9e9e9e"))

                    } else if (data.status__c.equals("Active")) {

                        if (data.enable_Payment_Link == false) {
                            binding.paymentStatusTv.text = "Paid"
                        } else {
                            binding.paymentStatusTv.text = "Unpaid"
                        }
                        binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))

                    } else if (data.status__c.equals("Rejected")) {

                        if (data.enable_Payment_Link == false) {
                            binding.paymentStatusTv.text = "Paid"
                        } else {
                            binding.paymentStatusTv.text = "Unpaid"
                        }
                        binding.statusTv.setTextColor(Color.parseColor("#FFAB00"))

                    } else {

                    }
//
//                if (data.enable_Payment_Link == false) {
//                    binding.paymentStatusTv.text = "Paid"
//                } else {
//                    binding.paymentStatusTv.text = "Unpaid"
//                }
                }
            }

            orderDetailsViewModel.requestcodes.observe(this){
//                Toast.makeText(this,"Session Expired", Toast.LENGTH_LONG).show()
                DesignToast.makeText(
                    this@OrderDetailActivity,
                    "Session Expired",
                    Toast.LENGTH_SHORT,
                    DesignToast.TYPE_ERROR
                ).show()

                SharedPreferenceUtil.setData(this, "mobileNo", "-1")
                SharedPreferenceUtil.setData(this, "bToken", "")
                SharedPreferenceUtil.setData(this, "IsLogin", false)
                SharedPreferenceUtil.setData(this, "pincode", "")
                SharedPreferenceUtil.setData(this, "customerid", "")
                SharedPreferenceUtil.setData(this, "FirstName", "")
                SharedPreferenceUtil.setData(this, "MobileNo", "")

                val intent= Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }

            orderDetailsViewModel.getOrderDetailsByOrderNo(orderNo, serviceType)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}

